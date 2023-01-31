package com.xgf.constant;

import com.xgf.common.LogUtil;
import com.xgf.exception.CustomExceptionEnum;

/**
 * @author strive_day
 * @create 2023-01-25 12:19
 * @description 整形常量工具类
 */
public class IntegerConstantUtil {

    /**
     * 字符串转 int，捕获异常返回0
     *
     * @param str String
     * @return str转int，如果转换出现异常，返回0
     */
    public static int convertStrToIntCatch(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            LogUtil.warn("convertStrToInt exception, param = {}, message = {}", str, e.getLocalizedMessage(), e);
        }
        return 0;
    }

    /**
     * 字符串转 int，异常按指定文案抛出 + 异常信息内容
     *
     * @param str String
     * @return str转int，如果转换出现异常，返回0
     */
    public static int convertStrToIntThrow(String str, String exceptionMessage) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            throw CustomExceptionEnum.DATA_CONVERT_EXCEPTION.generateCustomMessageException(exceptionMessage + ", ", e.getLocalizedMessage());
        }
    }


}
