package com.xgf.exception.handler.global;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.xgf.constant.StringConstantUtil;
import com.xgf.constant.reqrep.CommonResponse;
import com.xgf.date.DateUtil;
import com.xgf.exception.CustomException;
import com.xgf.file.FileUtil;
import com.xgf.task.TaskUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * @author xgf
 * @create 2022-02-11 10:02
 * @description 自定义 全局异常处理
 * https://blog.csdn.net/qq_40542534/article/details/110691409
 **/

@ControllerAdvice(basePackages="com.xgf")
public class CustomGlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(CustomGlobalExceptionHandler.class);

//	@InitBinder
//	private void initBinder(DataBinder dataBinder) {
//		// 将Spring DataBinder配置为使用直接字段访问【默认值：dataBinder.initBeanPropertyAccess()】
//		log.info("====== CustomGlobalExceptionHandler 应用到所有 @RequestMapping 注解方法，在其执行之前初始化数据绑定器");
//		dataBinder.initDirectFieldAccess();
//	}

	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public Object defaultExceptionHandler(Exception e, HttpServletRequest req, HttpServletResponse resp) throws Exception {

		// ContentCachingRequestWrapper 是为了解决 HttpServletRequest inputStream  只能读取一次的问题
		if (Objects.nonNull(req) && req instanceof ContentCachingRequestWrapper) {
			ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) req;
			log.warn("====== CustomGlobalExceptionHandler bad request body = 【{}】", new String(wrapper.getContentAsByteArray(), Charset.forName(wrapper.getCharacterEncoding())));
		}

		// 响应
		CommonResponse response = CommonResponse.custom(CommonResponse.CommonResponseCodeEnum.SERVICE_EXCEPTION);
		// 错误信息
		String errorInfo = e.getLocalizedMessage();
		// 错误类型
		String errorType = "exception";

		try {
			ExceptionInfoAssembly assembly = doDealException(e, response, errorInfo, req);
			response = assembly.getResponse();
			errorInfo = assembly.getErrorInfo();
			errorType = assembly.getErrorType();
		} catch (Exception ex) {
			log.error("====== CustomGlobalExceptionHandler doDealException catch exception message = 【{}】", e.getLocalizedMessage(), e);
		} finally {
			log.warn("====== CustomGlobalExceptionHandler there is "
					+ StringConstantUtil.stringAppendChineseMidBracket(errorType) + " happening, url = 【{}】, message = 【{}】",
					req.getRequestURL(), errorInfo, e);
		}

		// 设置错误提示信息
		response.setResponseMessage(strJoin(errorType, errorInfo));

		// 记录异常信息到文件中 todo 22-5-3 待完善
		String finalErrorType = errorType;
		TaskUtil.runAsync(() -> {
			try {
				// 默认写入路径: 项目根路径 > log > sysTimeLog > 年 > 月 > 日 + 时.txt
				FileUtil.fileAppendData(FileUtil.createFileBySysTime(),
						StringConstantUtil.LINE_FEED + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_MILL)
								+ StringConstantUtil.stringAppendChineseMidBracket(finalErrorType) + StringConstantUtil.LINE_FEED + getExceptionMessage(e));
			} catch (Exception ex) {
				log.error("====== CustomGlobalExceptionHandler save exception info log error, message = 【{}】", e.getLocalizedMessage(), e);
			}
		});

		return response;
	}

	/**
	 * 字符串连接
	 * @param args 参数
	 * @return 拼接后字符串
	 */
	private String strJoin(Object ...args) {
		return StringUtils.join(args, (StringConstantUtil.COMMA + StringConstantUtil.BLANK));
	}

	/**
	 * 处理异常
	 *
	 * @param e 异常信息
	 * @param response 通用响应结果
	 * @param errorInfo 错误信息封装
	 * @param req 请求内容
	 * @return ExceptionInfoAssembly
	 */
	public ExceptionInfoAssembly doDealException(Exception e, CommonResponse response, String errorInfo,
												 HttpServletRequest req) {
		String errorType;
		if(e instanceof CustomException){
			errorType = "CustomException";
			response.setResponseCode(((CustomException) e).getErrorCode());
			// e.getMessage() 即为枚举 CustomException 的value
			errorInfo = e.getLocalizedMessage();

		} else if (e instanceof MethodArgumentNotValidException){
			errorType = "MethodArgumentNotValidException";
			// 获取 spring校验器 的校验结果
			List<ObjectError> errors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
			// 获取第一条校验信息
			if(CollectionUtils.isNotEmpty(errors)){
				// 暂时只使用字段校验（字段注解校验），所以都是FieldError
				FieldError error = (FieldError) errors.get(0);
				errorInfo = "参数验证错误, 参数名 = " + error.getField() + ", 参数值 = " + error.getRejectedValue() + ", 错误信息 = " + error.getDefaultMessage();
				response = CommonResponse.custom(CommonResponse.CommonResponseCodeEnum.PARAM_VALID_EXCEPTION);
			}

		}else if(e instanceof NullPointerException){
			errorType = "NullPointerException";
			// 获取异常相关信息（异常栈首元素）
			StackTraceElement stackTraceElement = e.getStackTrace()[0];
			// 出错信息组装（类路径 + 方法名 + 行数）
			errorInfo = " NPE, " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ", line = " + stackTraceElement.getLineNumber();
			response = CommonResponse.custom(CommonResponse.CommonResponseCodeEnum.PARAM_VALID_EXCEPTION);

		}else if(e instanceof UnknownHostException){
			errorType = "UnknownHostException";
			errorInfo = "服务网络异常";
			response = CommonResponse.custom(CommonResponse.CommonResponseCodeEnum.ENVIRONMENT_EXCEPTION);

		} else if (e instanceof MethodArgumentTypeMismatchException) {
			errorType = "MethodArgumentTypeMismatchException";
			// 参数类型不匹配
			errorInfo = strJoin("参数类型不匹配", "Param value = " + StringConstantUtil.stringAppendChineseMidBracket(req.getQueryString()));
			response = CommonResponse.custom(CommonResponse.CommonResponseCodeEnum.PARAM_TYPE_MISMATCH_EXCEPTION);

		} else if (e instanceof JsonMappingException) {
			errorType = "JsonMappingException";
			errorInfo = strJoin("JSON格式化异常", e.getLocalizedMessage());
			response = CommonResponse.custom(CommonResponse.CommonResponseCodeEnum.JSON_MAPPING_EXCEPTION);

		} else if (e instanceof HttpMessageNotReadableException) {
			errorType = "HttpMessageNotReadableException";
			// 一般是序列化异常
			errorInfo = strJoin("请求体格式错误", e.getLocalizedMessage());

		} else if (e instanceof MissingServletRequestParameterException) {
			errorType = "MissingServletRequestParameterException";
			String paramName = ((MissingServletRequestParameterException) e).getParameterName();
			errorInfo = paramName + "不能为空";
			response = CommonResponse.custom(CommonResponse.CommonResponseCodeEnum.COMMON_PARAM_NULL_EXCEPTION);

		} else {
			errorType = "OtherException";
		}

		return ExceptionInfoAssembly.convert(response, errorInfo, errorType);
	}


	/**
	 * 获取异常Exception的相信信息
	 * @param e 异常
	 * @return 异常详细信息
	 */
	public String getExceptionMessage(Exception e) {
		// JDK1.7 try内流创建自动关闭
		try {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter= new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			StringBuffer buffer= stringWriter.getBuffer();
			return buffer.toString();
		} catch (Exception ex) {
			throw new RuntimeException(e);
		}
	}


	@Data
	@ApiModel("异常信息结果返回封装")
	static class ExceptionInfoAssembly {

		/**
		 * 通用响应
		 */
		CommonResponse response;

		/**
		 * 异常信息内容
		 */
		String errorInfo;

		/**
		 * 异常类型
		 */
		String errorType;

		public static ExceptionInfoAssembly convert(CommonResponse response, String errorInfo, String errorType) {
			ExceptionInfoAssembly assembly = new ExceptionInfoAssembly();
			assembly.setResponse(response);
			assembly.setErrorInfo(errorInfo);
			assembly.setErrorType(errorType);
			return assembly;
		}

	}


}
