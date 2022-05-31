package com.xgf.excel.bean;

import com.google.common.collect.Maps;
import com.xgf.excel.aspect.ExcelFieldAnnotation;
import com.xgf.reflect.CommonReflectUtil;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author xgf
 * @create 2022-05-18 01:10
 * @description Excel字段信息排序类，和注解 {@link com.xgf.excel.aspect.ExcelFieldAnnotation} 强相关
 **/

@Data
public class ExcelFieldInfo {

    /**
     * 字段名（属性字段名）
     */
    private String fieldName;

    /**
     * 注解字段名称（导出是表单第一行标题信息）
     */
    private String annotationFieldName;

    /**
     * 在excel中排序
     */
    private Integer order;

    /**
     * 是否隐藏不展示
     */
    private Boolean hidden;

    public static ExcelFieldInfo valueOf(String fieldName, String annotationFieldName, Integer order, Boolean hidden) {
        ExcelFieldInfo excelField = new ExcelFieldInfo();
        excelField.setFieldName(fieldName);
        excelField.setAnnotationFieldName(annotationFieldName);
        excelField.setOrder(order);
        excelField.setHidden(hidden);
        return excelField;
    }

    /**
     * 根据注解 ExcelFieldAnnotation 排序顺序给出，获取类的字段名和注解名称信息（有序map）
     * 注解配置hidden = true 隐藏则不返回
     * 字段上没有注解ExcelFieldAnnotation则默认返回，取值字段名称
     * 字段上无注解，则往后排（order空值排后面），ExcelFieldAnnotation 注解 order 值一致（或者多个没有该注解）则根据类的字段顺序有序
     * 和注解 {@link com.xgf.excel.aspect.ExcelFieldAnnotation} 强相关
     *
     * @param dataClz 类
     * @return 有序map, key : 字段名, value : 字段上注解名name（注解为空，则为字段名）【根据注解order排序）
     */
    public static Map<String, String> getField2AnnotationNameLinkedMap(Class<?> dataClz) {
        List<Field> allFieldList = CommonReflectUtil.getAllFieldByNameDistinct(dataClz);
        List<ExcelFieldInfo> excelFiledList = new ArrayList<>();
        for (Field field : allFieldList) {
            // 获取字段上注解
            ExcelFieldAnnotation annotation = field.getAnnotation(ExcelFieldAnnotation.class);
            if (Objects.nonNull(annotation)){
                excelFiledList.add(ExcelFieldInfo.valueOf(field.getName(), annotation.name(), annotation.order(), annotation.hidden()));
            } else {
                // 为空则直接根据字段属性名显示
                excelFiledList.add(ExcelFieldInfo.valueOf(field.getName(), field.getName(), null, Boolean.FALSE));
            }
        }

        if (CollectionUtils.isEmpty(excelFiledList)) {
            return Maps.newHashMap();
        }

        // 有序map, key : 字段名, value : 字段上注解名name（注解为空，则为字段名）
        Map<String, String> resultMap = Maps.newLinkedHashMap();
        excelFiledList.stream()
                .filter(Objects::nonNull)
                .filter(p -> !BooleanUtils.isTrue(p.getHidden()))
                .sorted(Comparator.comparing(ExcelFieldInfo::getOrder, Comparator.nullsLast(Comparator.naturalOrder())))
                .forEach(p ->resultMap.put(p.getFieldName(), p.getAnnotationFieldName()));
        return resultMap;
    }
    
    
}
