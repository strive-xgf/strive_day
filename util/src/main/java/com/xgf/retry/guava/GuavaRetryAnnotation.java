package com.xgf.retry.guava;

import com.github.rholder.retry.RetryListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xgf
 * @create 2022-07-03 19:36
 * @description Guava Retry 注解
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GuavaRetryAnnotation {

    /**
     * 异常类（配置该值，则只有满足指定异常才重试）
     */
    Class[] exceptionClass() default {Exception.class};

    /**
     * @return 重试次数 【注意: attemptNumber配置大于0 则duration无效】
     */
    int attemptNumber() default 0;

    /**
     * @return 等待时间（重试间隔等待策略）
     */
    long waitStrategySleepTime() default 0;

    /**
     * @return 持续时间 【注意: duration 生效需要将 attemptNumber 配置小于等于0】
     */
    long duration() default 0;

    /**
     * @return 重试方法限制执行时长，单位毫秒，默认3s
     */
    long limitDuration() default  3000;

    /**
     * @return 重试监听（处理特殊逻辑）
     */
    Class<? extends RetryListener> retryListenerClass() default GuavaRetryListener.class;

}
