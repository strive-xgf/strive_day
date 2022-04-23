package com.xgf.exception.handler.global;

import com.xgf.constant.StringConstantUtil;
import com.xgf.constant.reqrep.CommonResponse;
import com.xgf.exception.CustomException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
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
//	public void initBinder(WebDataBinder binder) {
//		log.info("====== CustomGlobalExceptionHandler 应用到所有 @RequestMapping 注解方法，在其执行之前初始化数据绑定器");
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
		CommonResponse response = new CommonResponse();
		// 错误信息
		String errorInfo = e.getMessage();

		if(e instanceof CustomException){
			log.warn("====== CustomGlobalExceptionHandler there is customException happening, message = 【{}】", e.getMessage(), e);
			response.setResponseCode(((CustomException) e).getErrorCode());
			response.setResponseType(CommonResponse.ResponseTypeEnum.EXCEPTION);
			// e.getMessage() 即为枚举 CustomException 的value
			errorInfo = e.getMessage();

		} else if (e instanceof MethodArgumentNotValidException){
			log.warn("====== CustomGlobalExceptionHandler there is 【MethodArgumentNotValidException】 happening, message = 【{}】", e.getMessage(), e);
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
			// 获取异常相关信息（异常栈首元素）
			StackTraceElement stackTraceElement = e.getStackTrace()[0];
			// 出错信息组装（类路径 + 方法名 + 行数）
			errorInfo = " NPE, " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ", line = " + stackTraceElement.getLineNumber();
			log.warn("====== CustomGlobalExceptionHandler there is 【NullPointerException】 happening, message = 【{}】", errorInfo, e);

			response = CommonResponse.custom(CommonResponse.CommonResponseCodeEnum.PARAM_VALID_EXCEPTION);

		}else if(e instanceof UnknownHostException){
			log.warn("====== CustomGlobalExceptionHandler there is 【UnknownHostException】 happening, message = 【{}】", e.getMessage(), e);
			errorInfo = "服务网络异常";
			response = CommonResponse.custom(CommonResponse.CommonResponseCodeEnum.ENVIRONMENT_EXCEPTION);
		}
		else{
			log.warn("====== CustomGlobalExceptionHandler there is 【not catch Exception】 happening, message = 【{}】", e.getMessage(), e);
			response = CommonResponse.custom(CommonResponse.CommonResponseCodeEnum.SERVICE_EXCEPTION);
		}

		// 设置错误提示信息
		response.setResponseMessage(strJoin(req.getRequestURL(), errorInfo));

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

}
