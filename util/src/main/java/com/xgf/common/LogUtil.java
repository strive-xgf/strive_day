package com.xgf.common;

import com.xgf.constant.StringConstantUtil;
import com.xgf.java8.BooleanFunctionUtil;
import com.xgf.system.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * @author xgf
 * @create 2022-06-30 14:19
 * @description 日志工具类
 **/

@Component
public class LogUtil {

    private static final Logger log = LoggerFactory.getLogger(LogUtil.class);

    /**
     * 关闭转换为json字符串标识（参数转换，yml文件读取）,为null/false 则直接打印参数对象，为true，转换为特殊json打印
     */
    private static Boolean CLOSE_CONVERT_JSON;

    /**
     * 给静态变量 CLOSE_CONVERT_JSON 读取赋值
     */
    @Value("${custom.config.log.closeConvertJson:false}")
    public void setStaticCloseConvertJson(Boolean closeConvertJson) {
        LogUtil.CLOSE_CONVERT_JSON = closeConvertJson;
    }

    /**
     * 打印参数
     */
    public static void traceParam(Object ...objects) {
        log.trace(getLogParamInfo(getStackTrace(3),objects));
    }

    public static void debugParam(Object ...objects) {
        log.debug(getLogParamInfo(getStackTrace(3),objects));
    }

    public static void infoParam(Object ...objects) {
        log.info(getLogParamInfo(getStackTrace(3),objects));
    }

    public static void warnParam(Object ...objects) {
        log.warn(getLogParamInfo(getStackTrace(3),objects));
    }

    public static void errorParam(Object...objects) {
        log.error(getLogParamInfo(getStackTrace(3),objects));
    }

    /**
     * 满足 true / false 条件 则记录日志
     */
    public static void trueInfo(Boolean flag, String template, Object ...objects) {
        BooleanFunctionUtil.trueRunnable(flag).run(() ->log.info(convertTemplate(template), convertParamToArray(objects)));
    }
    public static void falseInfo(Boolean flag, String template, Object ...objects) {
        BooleanFunctionUtil.falseRunnable(flag).run(() ->log.info(convertTemplate(template), convertParamToArray(objects)));

    }

    public static void trueWarn(Boolean flag, String template, Object ...objects) {
        BooleanFunctionUtil.trueRunnable(flag).run(() ->log.warn(convertTemplate(template), convertParamToArray(objects)));
    }
    public static void falseWarn(Boolean flag, String template, Object ...objects) {
        BooleanFunctionUtil.falseRunnable(flag).run(() ->log.warn(convertTemplate(template), convertParamToArray(objects)));
    }


    /**
     * 基于模板打印参数
     */
    public static void trace(String template, Object ...objects) {
        log.trace(convertTemplate(template), convertParamToArray(objects));
    }

    public static void debug(String template, Object ...objects) {
        log.debug(convertTemplate(template), convertParamToArray(objects));
    }

    public static void info(String template, Object ...objects) {
        log.info(convertTemplate(template), convertParamToArray(objects));
    }

    public static void warn(String template, Object ...objects) {
        log.warn(convertTemplate(template), convertParamToArray(objects));
    }

    public static void error(String template, Object ...objects) {
        log.error(convertTemplate(template), convertParamToArray(objects));
    }

    public static void error(String template, Throwable throwable, Object ...objects) {
        log.error(convertTemplate(template), convertParamToArray(objects), throwable);
    }


    /**
     * 转换模板，添加前缀trace信息
     *
     * @param template 模板
     * @return trace信息
     */
    private static String convertTemplate(String template) {
        if (template == null) {
            return getStackTraceLogInfo(getStackTrace(4));
        }

        return getStackTraceLogInfo(getStackTrace(4)) + StringConstantUtil.CHANGE_SEPARATOR + template;
    }

    private static Object[] convertParamToArray(Object... params) {
        if (params.length == 0) {
            return new String[0];
        }

        Object[] paramArray = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            if (CLOSE_CONVERT_JSON != null && CLOSE_CONVERT_JSON) {
                paramArray[i] = params[i];
            } else {
                paramArray[i] = convertToStr(params[i]);
            }

        }

        return paramArray;
    }

    /**
     * 获取日志信息
     *
     * @param stackTrace 堆栈信息
     * @param params 参数
     * @return 打印的日志信息
     */
    private static String getLogParamInfo(StackTraceElement stackTrace, Object... params) {
        StringBuilder sb = new StringBuilder(getStackTraceLogInfo(stackTrace));

        // 打印参数
        if (params.length == 0) {
            return sb.toString();
        }

        sb.append(StringConstantUtil.CHANGE_SEPARATOR).append(StringConstantUtil.PARAM).append(StringConstantUtil.LEFT_MIDDLE_BRACKET);
        for(Object param : params) {
            if (CLOSE_CONVERT_JSON != null && CLOSE_CONVERT_JSON) {
                sb.append(param);
            } else {
                sb.append(convertToStr(param));
            }
            sb.append(StringConstantUtil.COMMA);
        }
        // 去除最后一个间隔符号逗号
        sb.deleteCharAt(sb.length()-1);
        sb.append(StringConstantUtil.RIGHT_MIDDLE_BRACKET);

        return sb.toString();
    }

    /**
     * 获取方法堆栈日志信息
     *
     * @param stackTrace StackTraceElement
     * @return 文件信息、类信息、方法信息、行数
     */
    private static String getStackTraceLogInfo(StackTraceElement stackTrace) {
        return StringConstantUtil.CHINESE_LEFT_MIDDLE_BRACKET +
                stackTrace.getFileName() +
                StringConstantUtil.CHANGE_SEPARATOR +
                stackTrace.getClassName() +
                StringConstantUtil.DOT +
                stackTrace.getMethodName() +
                StringConstantUtil.COLON +
                stackTrace.getLineNumber() +
                StringConstantUtil.CHINESE_RIGHT_MIDDLE_BRACKET;
    }

    /**
     * 获取当前线程方法调用的堆栈信息
     *
     * @param n 栈数组数
     * @return 调用日志处的堆栈信息
     */
    private static StackTraceElement getStackTrace(int n) {
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        // 堆栈信息，第一个元素: Thread.java，第二个元素: 当前方法
        if (stackTraces.length < n) {
            return stackTraces[stackTraces.length - 1];
        }

        return stackTraces[n];
    }

    /**
     * 参数转字符串处理
     *
     * @param object 参数对象
     * @return 字符串信息
     */
    private static String convertToStr(Object object) {

        if (object == null) {
            log.info("====== {} getValue in param is null", log.getName());
            return StringConstantUtil.EMPTY;
        }

        StringBuilder sb = new StringBuilder();

        // 异常类打印异常堆栈信息
        if (object instanceof Exception) {
            Exception exception = (Exception) object;
            sb.append(StringConstantUtil.EXIST).append(StringConstantUtil.BLANK).append(StringConstantUtil.EXCEPTION)
                    .append(StringConstantUtil.CHINESE_COLON).append(exception.getLocalizedMessage()).append(SystemUtil.getLineSeparator());
            StringWriter sw = new StringWriter();
            // 异常详细信息输出
            PrintWriter pw = new PrintWriter(sw,true);
            exception.printStackTrace(pw);
            pw.flush();
            sw.flush();
            sb.append(sw);
            return sb.toString();
        }

        // 转换 json 字符串
        return JsonUtil.toJsonString(object);
    }

}