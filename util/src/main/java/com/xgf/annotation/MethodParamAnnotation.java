package com.xgf.annotation;

import java.lang.annotation.*;

/**
 * @author xgf
 * @create 2022-04-167 18:25
 * @description 方法参数注解
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodParamAnnotation {
    /**
     * #{paramName.xx.xx} 动态参数解析
     * @return 方法参数解析
     */
    String methodParam() default "";

    /**
     * 解析返回数组，按什么类型转换数据（默认String返回）
     * 传这个的区别可参考方法：{@link com.xgf.util.annotation.MethodParamAnnotationService#testParamTypeDiff()}
     * @return 根据paramType值，转换List解析
     */
    Class<?> paramType() default String.class;

}
