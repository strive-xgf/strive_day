package com.xgf.constant;

import com.xgf.date.DateUtil;
import com.xgf.reflect.CommonReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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
     * @return 满足返回对应对象特殊值
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

        // 字符串情况，赋值空字符串
        if (clz.isAssignableFrom(String.class)) {
            return StringConstantUtil.EMPTY;
        }


        return null;
    }


    /**
     * 将对象 source 以反射形式转换为对应类，且对应空值转换为特殊值 com.xgf.constant.CommonNullConstantUtil
     * 目标类中的字段在 source 中不存在，则赋null
     *
     * @param source 数据源对象
     * @param clz 目标类
     * @param <T> 泛型
     * @param <V> 泛型
     * @return 目标类实例对象，且对空值做特殊值处理
     */
    public static <T, V> T copyConvertSpecialWithNull(V source, Class<T> clz) {
        if (source == null) {
            return null;
        }

        // 源对象的所有字段信息
        Map<String, Field> fieldName2ValueMap = CommonReflectUtil.getAllField(source)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Field::getName, Function.identity(), (v1, v2) -> v1));

        // 创建目标类实例
        T result = CommonReflectUtil.createInstance((clz));

        // 目标类所有字段信息（同名去重）
        List<Field> resultAllFieldList = CommonReflectUtil.getAllFieldByNameDistinct(clz);

        // 遍历目标类，给对象赋值（空值赋 CommonNullConstantUtil 特殊值）
        for (Field targetField : resultAllFieldList) {

            Field sourceField = fieldName2ValueMap.get(targetField.getName());
            // 源数据没有该字段，继续遍历
            if (sourceField == null) {
                continue;
            }

            try {
                sourceField.setAccessible(true);
                Object o = sourceField.get(source);
                // 源对象值为空，获取对应数据类型的特殊值
                if (o == null) {
                    o = CommonNullConstantUtil.getSpecialValueByClassType(sourceField.getType());
                }
                CommonReflectUtil.setFieldValue(targetField, result, o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


}
