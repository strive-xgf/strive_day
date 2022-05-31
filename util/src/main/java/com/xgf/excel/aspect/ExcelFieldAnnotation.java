package com.xgf.excel.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xgf
 * @create 2022-05-17 10:32
 * @description excel字段注解（注解在字段属性上，用来导出excel文档时的标题信息和顺序内容）
 **/

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelFieldAnnotation {

    /**
     * @return 字段名称 属性字段标识名（导出标题）
     */
    String name();

    /**
     * @return 字段排序，默认排序到9999
     */
    int order() default 99999;

    /**
     * @return 是否隐藏，默认展示false, true : 隐藏，false : 展示
     */
    boolean hidden() default false;

}
