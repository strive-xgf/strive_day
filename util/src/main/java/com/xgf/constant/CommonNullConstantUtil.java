package com.xgf.constant;

import com.xgf.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xgf
 * @create 2022-05-04 17:26
 * @description 通用空值常量工具类（对应类型传一下默认值，当空值处理，去相关操作（eg: 清空数据库））
 * 搭配对应数据类型的自定义枚举验证器 Validate 来处理
 **/

@Slf4j
public class CommonNullConstantUtil {

    /**
     * short 默认空值
     */
    public static final short SHORT_SPECIAL_NULL_VALUE = -32768;

    /**
     * int 默认空值
     */
    public static final int INT_SPECIAL_NULL_VALUE = -999999999;

    /**
     * long 默认空值
     */
    public static final long LONG_SPECIAL_NULL_VALUE = -999999999L;

    /**
     * float 默认空值
     */
    public static final float FLOAT_SPECIAL_NULL_VALUE = -999999999.999999999F;

    /**
     * double 默认空值
     */
    public static final double DOUBLE_SPECIAL_NULL_VALUE = -999999999.999999999D;

    /**
     * BigDecimal 默认空值
     */
    public static final BigDecimal BIG_DECIMAL_SPECIAL_NULL_VALUE = new BigDecimal("-999999999.999999999");

    /**
     * Date 默认空值
     */
    public static final Date DATE_SPECIAL_NULL_VALUE = DateUtil.getDateNullInstance();


    /**
     * 校验对象是否满足特殊空值
     * @param o 对象
     * @return 满足返回 true
     */
    public static boolean checkSpecialValue(Object o) {

        if (o == null) {
            return false;
        }

        if (o instanceof Short) {
            return o.equals(CommonNullConstantUtil.SHORT_SPECIAL_NULL_VALUE);
        }

        if (o instanceof Integer) {
            return o.equals(CommonNullConstantUtil.INT_SPECIAL_NULL_VALUE);
        }

        if (o instanceof Long) {
            return o.equals(CommonNullConstantUtil.LONG_SPECIAL_NULL_VALUE);
        }

        if (o instanceof Float) {
            return o.equals(CommonNullConstantUtil.FLOAT_SPECIAL_NULL_VALUE);
        }

        if (o instanceof Double) {
            return o.equals(CommonNullConstantUtil.DOUBLE_SPECIAL_NULL_VALUE);
        }

        if (o instanceof BigDecimal) {
            return o.equals(CommonNullConstantUtil.BIG_DECIMAL_SPECIAL_NULL_VALUE);
        }

        if (o instanceof Date) {
            return o.equals(CommonNullConstantUtil.DATE_SPECIAL_NULL_VALUE);
        }

        log.warn("====== CommonNullConstantUtil checkSpecialValue, {} this type is not supported", o.getClass().getName());
        return false;
    }


    /**
     * 对应类型默认特殊值转null，否则返回当前入参对象
     * @param o 对象
     * @return 满足特殊值返回 null，否则返回入参
     */
    public static Object convertNullBySpecialValue(Object o) {
        return BooleanUtils.isTrue(checkSpecialValue(o)) ? null : o;
    }



    /**
     * 获取对应类型的特殊值
     * @param clz 对象
     * @return 满足返回 true
     */
    public static Object getSpecialValueByClassType(Class<?> clz) {

        if (clz == null) {
            return null;
        }

        if (clz.isAssignableFrom(Short.class)) {
            return CommonNullConstantUtil.SHORT_SPECIAL_NULL_VALUE;
        }

        if (clz.isAssignableFrom(Integer.class)) {
            return CommonNullConstantUtil.INT_SPECIAL_NULL_VALUE;
        }

        if (clz.isAssignableFrom(Long.class)) {
            return CommonNullConstantUtil.LONG_SPECIAL_NULL_VALUE;
        }

        if (clz.isAssignableFrom(Float.class)) {
            return CommonNullConstantUtil.FLOAT_SPECIAL_NULL_VALUE;
        }

        if (clz.isAssignableFrom(Double.class)) {
            return CommonNullConstantUtil.DOUBLE_SPECIAL_NULL_VALUE;
        }

        if (clz.isAssignableFrom(BigDecimal.class)) {
            return CommonNullConstantUtil.BIG_DECIMAL_SPECIAL_NULL_VALUE;
        }

        if (clz.isAssignableFrom(Date.class)) {
            return CommonNullConstantUtil.DATE_SPECIAL_NULL_VALUE;
        }

        return null;
    }


}
