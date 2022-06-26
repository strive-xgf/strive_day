package com.xgf.compare;

import com.google.common.collect.Lists;
import com.xgf.common.JsonUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.date.DateUtil;
import com.xgf.reflect.CommonReflectUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2022-06-21 23:19
 * @description 通用字段比较工具类
 **/

public class CommonFieldCompareUtil {


    public static <T> String getFieldChangeInfo(T before, T after) {
        return getFieldChangeInfo(before, after, null, null);
    }

    public static <T> String getFieldChangeInfo(T before, T after, Map<String, String> includeFieldName2DescMap) {
        return getFieldChangeInfo(before, after, includeFieldName2DescMap, null);
    }

    public static <T> String getFieldChangeInfo(T before, T after, Set<String> excludeFieldSet) {
        return getFieldChangeInfo(before, after, null, excludeFieldSet);
    }

    /**
     * 获取对象字段前后值变化信息
     *
     * @param before 改变前
     * @param after 改变后
     * @param includeFieldName2DescMap 包含的对应字段的中文描述信息（只返回map中的字段信息）（为 空null 则直接用字段名信息），为空map则返回空字符串
     * @param excludeFieldSet 排除哪些字段不作为比较输出
     *                         注意：includeFieldName2DescMap 有值，但是不在这个map中的字段不比较，在 excludeFieldSet 中的字段不比较
     * @param <T> 泛型类型
     * @return 字段值变化信息前后值
     */
    public static <T> String getFieldChangeInfo(T before, T after, Map<String, String> includeFieldName2DescMap, Set<String> excludeFieldSet) {
        if (before == null || after == null) {
            return StringConstantUtil.EMPTY;
        }

        if (includeFieldName2DescMap == null) {
            includeFieldName2DescMap = Optional.of(CommonReflectUtil.getAllField(before.getClass()))
                    .orElseGet(Lists::newArrayList)
                    .stream().collect(Collectors.toMap(Field::getName, Field::getName, (v1, v2) -> v1));
        }

        StringBuilder sb = new StringBuilder();

        Map<String, Object> beforeName2ValueMap = CommonReflectUtil.getAllFieldName2ValueMap(before);
        Map<String, Object> afterName2ValueMap = CommonReflectUtil.getAllFieldName2ValueMap(after);


        for (Map.Entry<String, String> entry : includeFieldName2DescMap.entrySet()) {
            // 数据中没有的字段返回
            if (!beforeName2ValueMap.containsKey(entry.getKey())) {
                continue;
            }

            // 排除字段
            if (excludeFieldSet != null && excludeFieldSet.contains(entry.getKey())) {
                continue;
            }

            // 对应字段新旧值转换为 String 类型进行比较和数据字段描述
            String oldFieldValue = convert2String(beforeName2ValueMap.get(entry.getKey()));
            String newFieldValue = convert2String(afterName2ValueMap.get(entry.getKey()));

            // 新旧值，不一样（字段变更）
            if (!StringUtils.equals(oldFieldValue, newFieldValue)) {
                // 字段描述： 旧值 => 新值；
                sb.append(entry.getValue())
                        .append(StringConstantUtil.CHINESE_COLON)
                        .append(oldFieldValue)
                        .append(StringConstantUtil.CHANGE_SEPARATOR)
                        .append(newFieldValue)
                        .append(StringConstantUtil.CHINESE_SEMICOLON);
            }
        }

        return sb.toString();

    }


    /**
     * 数据转换为字符串
     *
     * @param o Object
     * @return 转换为对应字符串
     */
    private static String convert2String(Object o) {
        if (o == null) {
            return StringConstantUtil.EMPTY;
        }

        if (o instanceof String) {
            return String.valueOf(o);
        }

        if (o instanceof Integer || o instanceof BigDecimal) {
            return o.toString();
        }

        if (o instanceof Date) {
            return DateUtil.dateFormatString((Date) o, DateUtil.FORMAT_SECOND);
        }

        if (o instanceof Boolean) {
            return Boolean.TRUE.equals(o) ? "是" : "否";
        }

        if (o instanceof List) {
            return StringConstantUtil.listToString((List<?>) o);
        }

        return JsonUtil.toJsonString(o);
    }

}
