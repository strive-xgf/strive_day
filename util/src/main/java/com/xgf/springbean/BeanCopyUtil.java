package com.xgf.springbean;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import java.util.ArrayList;
import java.util.List;


/**
 * @author xgf
 * @create 2021-06-18 15:27
 * @description 使用 org.springframework.beans.BeanUtils.copyProperties 方法进行对象之间属性的赋值，避免通过get、set方法一个一个属性的赋值
 */

public class BeanCopyUtil {

    /**
     * 对象属性拷贝，将源对象的属性拷贝到目标对象
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void convertObject2Object(Object source, Object target) {
        try {
            BeanUtils.copyProperties(source, target);
        } catch (BeansException e) {
            System.err.println("=== BeanUtil property copy  failed，caused by：BeansException" + e);
        } catch (Exception e) {
            System.err.println("=== BeanUtil property copy failed，caused by：Exception" + e);
        }
    }

    /**
     * List集合属性拷贝，将集合对象拷贝给指定类，生成对应类的集合
     *
     * @param inputList 需要转换的集合
     * @param classObject 目标集合对象的类型 class
     * @param <T>       输入集合类型
     * @param <V>       输出集合类型
     * @return 返回目标集合
     */
    public static <T, V>List<V> convertList2List(List<T> inputList, Class<V> classObject) {
        List<V> outputList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(inputList)) {
            for (T source : inputList) {
                // instantiateClass 使用其主要构造函数（对于Kotlin类，可能具有声明的默认参数）
                // 或其默认构造函数（对于常规Java类，期望标准的无参数设置）来实例化类
                V targetObject = BeanUtils.instantiateClass(classObject);
                BeanUtils.copyProperties(source, targetObject);
                outputList.add(targetObject);
            }
        }
        return outputList;
    }

}
