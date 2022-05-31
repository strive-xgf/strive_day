package com.xgf.reflect;

import com.xgf.collection.AssemblyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2022-05-08 20:54
 * @description 反射工具类
 **/

@Slf4j
public class CommonReflectUtil {

    /**
     * 反射获取指定名称的字段信息Field
     * 注意：会一直向上遍历父类找到对应字段（直到遍历到Object）
     *
     * @param obj 目标对象
     * @param fieldName 目标属性
     * @return 找到返回 目标属性值（未找到返回null）
     */
    public static Field getField(Object obj, String fieldName) {
        // 从当前对象找字段名对应属性值，未找到向上（父类）遍历，直到遍历到返回，未找到返回null
        for (Class<?> clz = obj.getClass(); clz != Object.class; clz = clz.getSuperclass()) {
            try {
                // 获取类本身的属性成员（包括私有、共有、受到保护）
                return clz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // 不用特殊处理该异常，子类没有该字段会向上找对应的父类，都没有就返回null。
            }
        }
        // 未找到 返回null
        return null;
    }

    /**
     * 反射获取所有字段信息（包含继承父类）
     * 注意：会存在重复字段名（父类子类都有的属性字段）
     *
     * @param obj 对象
     * @return 所有字段信息
     */
    public static List<Field> getAllField(Object obj) {

        return getAllField(obj.getClass());
    }

    /**
     * 反射获取所有字段信息（包含继承父类）
     * 注意：会存在重复字段名（父类子类都有的属性字段）
     *
     * @param clz class 对象
     * @return 所有字段信息
     */
    public static List<Field> getAllField(Class<?> clz) {
        List<Field> fieldList = new ArrayList<>();
        // 从当前对象往上遍历到 Object 对象，获取所有的字段信息
        for (; clz != Object.class; clz = clz.getSuperclass()) {
            fieldList.addAll(Arrays.asList(clz.getDeclaredFields()));
        }
        return fieldList;
    }

    /**
     * 反射获取所有字段信息（包含继承父类）【通过名称字段去重】
     *
     * @param clz class 对象
     * @return 去除重复字段后的所有字段信息
     */
    public static List<Field> getAllFieldByNameDistinct(Class<?> clz) {
        return Optional.of(getAllField(clz)).orElseGet(ArrayList::new).stream()
                .filter(Objects::nonNull)
                .filter(AssemblyUtil.distinctByCondition(Field::getName))
                .collect(Collectors.toList());
    }

    /**
     * 反射获取字段值【包含获取父类字段值】
     *
     * @param obj 对象
     * @param fieldName 字段名
     * @return fieldName 的字段值
     */
    public static Object getFieldValue(Object obj, String fieldName) {

        if (obj == null) {
            return null;
        }

        Field field = CommonReflectUtil.getField(obj, fieldName);
        if (field == null) {
            // 不存在/值为null
            return null;
        }

        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.warn("====== CommonReflectUtil getFieldValue exception message = {}", e.getMessage(), e);
        }
        return null;

    }


    public static Map<String, Object> getFieldName2ValueMap(Object obj, String... paramFieldNames) {
        return getFieldName2ValueMap(obj, Arrays.stream(paramFieldNames).collect(Collectors.toList()));
    }

    /**
     * 反射获取指定字段列表的字段值 【包含获取父类字段值】
     * 如果不存在指定字段名，则Map中不会有对应的值
     * 从子类往上取，父类不会覆盖子类字段属性值（同名属性字段）
     *
     * @param obj 对象
     * @param paramFieldNameList 字段名
     * @return Map > key : 字段名 value : 字段值
     */
    public static Map<String, Object> getFieldName2ValueMap(Object obj, List<String> paramFieldNameList) {

        if (obj == null || CollectionUtils.isEmpty(paramFieldNameList)) {
            return new HashMap<>(1);
        }

        // 获取所有字段 List （包含继承类父类）
        List<Field> fieldList = CommonReflectUtil.getAllField(obj);

        // 获取对象的所有字段名称（去重，父子含有同名属性字段）
        List<String> fieldNameList = fieldList.stream().map(Field::getName).distinct().collect(Collectors.toList());

        // 复制入参集合（防止取交集时抛出异常: UnsupportedOperationException）【入参List是Arrays.asList等方法生成的集合，是内部类的ArrayList，而不是java.util.ArrayList，会抛出异常】
        ArrayList<String> paramFieldNameListCopy = new ArrayList<>(paramFieldNameList);

        // 和入参字段名集合取交集
        paramFieldNameListCopy.retainAll(fieldNameList);

        if (CollectionUtils.isEmpty(paramFieldNameListCopy)) {
            log.info("====== CommonReflectUtil getFieldValue not exist param field");
            return new HashMap<>(1);
        }

        // 创建对应大小的返参map
        HashMap<String, Object> fieldName2ValueMap = new HashMap<>(paramFieldNameListCopy.size());

        for (Class<?> clz = obj.getClass(); clz != Object.class; clz = clz.getSuperclass()) {
            // 获取当前类的所有字段属性
            Field[] currFieldList = clz.getDeclaredFields();
            // 将 columnEntry 参数列对应的 对象值 赋值到 valueEntry 中
            for (Field field : currFieldList) {
                if (paramFieldNameListCopy.contains(field.getName())) {
                    field.setAccessible(true);
                    try {
                        fieldName2ValueMap.put(field.getName(), field.get(obj));
                        // 【注意】需要将获取到的值，从 paramFieldNameListCopy 移除，不再判断，防止父类含有导致父类字段属性覆盖（不然父类属性值会覆盖子类属性值）
                        paramFieldNameListCopy.remove(field.getName());
                    } catch (IllegalAccessException e) {
                        log.warn("====== CommonReflectUtil getFieldValue 【IllegalAccessException】, message = {}", e.getLocalizedMessage(), e);
                    }
                }
            }
        }

        return fieldName2ValueMap;
    }


    /**
     * 反射设置指定对象的指定属性指定值【包含获取父类字段值】
     *
     * @param obj 目标对象
     * @param fieldName 目标属性
     * @param fieldValue 目标值
     */
    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
        Field field = CommonReflectUtil.getField(obj, fieldName);
        if (field == null) {
            return;
        }
        try {
            field.setAccessible(true);
            field.set(obj, fieldValue);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
