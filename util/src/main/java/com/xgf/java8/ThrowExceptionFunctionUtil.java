package com.xgf.java8;


import com.xgf.exception.CustomExceptionEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xgf
 * @create 2022-07-25 23:43
 * @description 抛出异常函数式工具
 **/

public class ThrowExceptionFunctionUtil {

    /**
     * 函数式接口：满足条件，异常抛出
     **/
    @FunctionalInterface
    public interface ThrowExceptionFunction {

        /**
         * 抛出异常信息
         *
         * @param message 异常信息内容
         **/
        void throwMessage(String message);
    }

    /**
     * 为 true 抛出异常
     *
     * @param flag Boolean 值
     * @return true：抛出异常 {@linkplain com.xgf.exception.CustomExceptionEnum#CUSTOM_FUNCTION_EXCEPTION}
     */
    public static ThrowExceptionFunction isTureThrow(Boolean flag) {
        return message -> {
            if (flag != null && flag) {
                throw CustomExceptionEnum.CUSTOM_FUNCTION_EXCEPTION.generateCustomMessageException(message);
            }
        };
    }

    /**
     * 为 false 抛出异常
     *
     * @param flag Boolean值
     * @return false：抛出异常 {@linkplain com.xgf.exception.CustomExceptionEnum#CUSTOM_FUNCTION_EXCEPTION}
     */
    public static ThrowExceptionFunction isFalseThrow(Boolean flag) {
        return message -> {
            if (flag != null && !flag) {
                throw CustomExceptionEnum.CUSTOM_FUNCTION_EXCEPTION.generateCustomMessageException(message);
            }
        };
    }

    /**
     * 为 null 抛出异常
     *
     * @param obj Object
     * @return null：抛出异常 {@linkplain com.xgf.exception.CustomExceptionEnum#CUSTOM_FUNCTION_EXCEPTION}
     */
    public static ThrowExceptionFunction isNullThrow(Object obj) {
        return message -> {
            if (obj == null) {
                throw CustomExceptionEnum.CUSTOM_FUNCTION_EXCEPTION.generateCustomMessageException(message);
            }
        };
    }

    /**
     * 为 null 抛出异常
     *
     * @param str String
     * @return 空字符串：抛出异常 {@linkplain com.xgf.exception.CustomExceptionEnum#CUSTOM_FUNCTION_EXCEPTION}
     */
    public static ThrowExceptionFunction isBlankThrow(String str) {
        return message -> {
            if (StringUtils.isBlank(str)) {
                throw CustomExceptionEnum.CUSTOM_FUNCTION_EXCEPTION.generateCustomMessageException(message);
            }
        };
    }

}
