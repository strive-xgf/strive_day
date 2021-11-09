package com.xgf.create;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2021-11-10 00:01
 * @description 创建随机对象工具类
 **/


/**
 * @author xgf
 * @create 2021-11-05 15:12
 * @description MapStructTest
 **/


@Slf4j
public class CreateRandomObjectUtil {

    private static final String DEFAULT_RANDOM_STRING = "1234567890";

    /**
     * 创建对象并给字段随机赋值
     * @param classObject class
     * @param <T>
     * @return
     */
    public static <T> T createData(Class<T> classObject) {
        return createData(classObject, Boolean.FALSE, DEFAULT_RANDOM_STRING, DEFAULT_RANDOM_STRING.length()/3);
    }
    public static <T> T createData(Class<T> classObject, boolean junpFlag) {
        return createData(classObject, junpFlag, DEFAULT_RANDOM_STRING, DEFAULT_RANDOM_STRING.length()/3);
    }
    public static <T> T createData(Class<T> classObject, String randomString, Integer randomSize) {
        return createData(classObject, Boolean.FALSE, randomString, randomSize);
    }

    /**
     * 创建对象并给字段随机赋值
     * @param classObject 类class
     * @param jumpFlag 嵌套对象是否赋值
     * @param randomString 随机字符串（String类型赋值）
     * @param randomSize 随机字符串大小
     * @param <T>
     * @return
     */
    public static <T> T createData(Class<T> classObject, Boolean jumpFlag,  String randomString, Integer randomSize){
        T target = null;
        try {
            // 如果非定义类型，也找不到对应的类（则返回null） 比如：java.math.BigDecimal，捕获异常同时防止一直循环递归调用
            target = classObject.newInstance();
        } catch (Exception e) {
            log.warn("====== createData newInstance Exception, e = ", e);
            return null;
        }
        List<Method> methodList = Arrays.stream(classObject.getDeclaredMethods()).filter(Objects::nonNull).collect(Collectors.toList());
        List<String> methodNameList = methodList.stream().map(Method::getName).collect(Collectors.toList());
        List<Field> fieldList = Arrays.stream(classObject.getDeclaredFields()).filter(Objects::nonNull).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(methodList) || CollectionUtils.isEmpty(fieldList)) {
            return target;
        }

        for (Field field : fieldList) {
            String assembleSetMethodeName = assembleSetMethodeName(field.getName(), "set", 0);
            if (StringUtils.isBlank(assembleSetMethodeName) || !methodNameList.contains(assembleSetMethodeName)) {
                continue;
            }
            try {
                Method fieldSetMethod = classObject.getMethod(assembleSetMethodeName, field.getType());
                setValueToField(target, fieldSetMethod, field, jumpFlag, randomString, randomSize);
            } catch (Exception e) {
                log.warn("====== getMethod NoSuchMethodException, e = ", e);
            }
        }
        return target;
    }

    /**
     * 调用某个 set 方法 给对象属性赋值
     */
    private static void setValueToField(Object bean, Method fieldSetMethod, Field field, Boolean jumpFlag,  String randomString, Integer randomSize) throws InvocationTargetException, IllegalAccessException {
        Object value = null;
        value = getRandomInfo(field, jumpFlag, randomString, randomSize);
        fieldSetMethod.invoke(bean, value);
    }

    /**
     * 组装方法名称（set、get方法等）
     * @param fieldName 字段属性名
     * @param prefix 组装前缀
     * @param ignorePrefixNum 忽略fieldName字符数（有些方法字段对应的set、get方法没有部分前缀）
     * @return prefix + fieldName.substring(ignorePrefixNum)
     */
    private static String assembleSetMethodeName(String fieldName, String prefix, Integer ignorePrefixNum) {
        if (StringUtils.isBlank(fieldName)) {
            return null;
        }
        return prefix + fieldName.substring(ignorePrefixNum, ++ignorePrefixNum).toUpperCase() + fieldName.substring(ignorePrefixNum);
    }

    /**
     * 按类型生成随机数据
     */
    private static Object getRandomInfo(Field field, Boolean jumpFlag, String randomString, Integer randomSize) {

        switch (field.getType().getSimpleName()) {
            case "String":
                return getRandomStringInParam(randomString, randomSize, field.getName());
            case "Date":
                return getRandomTimeInParam(7);
            case "Byte":
            case "byte":
                return new Random().nextInt(Byte.MAX_VALUE);
            case "Short":
            case "short":
                return new Random().nextInt(Short.MAX_VALUE);
            case "Integer":
            case "int":
                return new Random().nextInt();
            case "Long":
            case "long":
                return new Random().nextLong();
            case "Float":
            case "float":
                return new Random().nextFloat();
            case "Double":
            case "double":
                return new Random().nextDouble();
            case "Boolean":
            case "boolean":
                return new Random().nextBoolean();
            case "BigDecimal":
                return BigDecimal.valueOf(new Random().nextDouble());
            case "Char":
            case "char":
                return getRandomStringInParam(randomString, 1, null);

            default:
                try {
                    if(BooleanUtils.isFalse(jumpFlag)){
                        // 递归调用，如果数据类型不能生成对应的对象返回null
                        return createData(field.getType(), jumpFlag, randomString, randomSize);
                    }
                } catch (Exception e) {
                    log.error("====== getRandomInfo subClass created exception, class = " + field.getType() + "，暂不支持该类型初始化 exception = ", e);
                }
                return null;
        }
    }

    /**
     * 获取当前时间的指定天数内的随机时间
     * @param inDayNum 当前时间的多少天内（往前数）【默认7天】
     * @return
     */
    private static Date getRandomTimeInParam(Integer inDayNum) {
        if (Objects.isNull(inDayNum) || inDayNum <= 0) {
            inDayNum = 7;
        }
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -inDayNum);
        return new Date((long) (Math.random() * (currentDate.getTime() - calendar.getTime().getTime()) + 1) + calendar.getTime().getTime());
    }

    /**
     * 获取随机字符串
     * @param randomStr 随机字符串
     * @param randomSize 从 randomStr 中随机取多少位
     * @param fieldName 字段名称（存在会拼接在前面，以_下划线拼接随机字符串）
     * @return
     */
    private static String getRandomStringInParam(String randomStr, Integer randomSize, String fieldName){

        if (StringUtils.isBlank(randomStr)) {
            return fieldName;
        }

        randomSize = Objects.isNull(randomSize) || randomSize <= 0 ? randomStr.length()/2 : randomSize;

        if(randomStr.length() <= randomSize){
            return StringUtils.isBlank(fieldName) ? randomStr : fieldName + "_" + randomStr;
        }

        int randomNum = new Random().nextInt(randomStr.length() - randomSize);

        return StringUtils.isBlank(fieldName)
                ? randomStr.substring(randomNum, randomNum += randomSize)
                : fieldName + "_" + randomStr.substring(randomNum, randomNum += randomSize);
    }


}