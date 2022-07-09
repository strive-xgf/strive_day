package com.xgf.retry.guava.config;

import com.github.rholder.retry.RetryListener;
import com.xgf.retry.guava.GuavaRetryListener;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-07-03 20:05
 * @description 读取 yml 配置属性
 **/

@Component
@ConfigurationProperties(prefix = "custom.config.guava.retry")
public class GuavaRetryProperties {
    /**
     * 重试次数
     */
    @Getter
    @Setter
    private int attemptNumber = 0;

    /**
     * 等待时间（重试间隔等待策略）
     */
    @Getter
    @Setter
    private long waitStrategySleepTime = 0;

    /**
     * 持续时间（在这段时间内一直重试）
     */
    @Getter
    @Setter
    private long duration = 0;

    /**
     * 重试拦截接口
     */
    @Getter
    @Setter
    private List<String> apis;

    /**
     * 重试方法限制执行时长，单位毫秒，默认3s
     */
    @Getter
    @Setter
    private long limitDuration = 3000;


    @Setter
    private String[] exceptionClassNames;

    @Setter
    private String retryListenerClassName;

    Class<? extends Throwable>[] exceptionClass;

    Class<? extends RetryListener> retryListenerClass = GuavaRetryListener.class;

    public Class<?>[] getExceptionClass() {
        if (Objects.isNull(exceptionClassNames)){
            return exceptionClass;
        }
        List<Class<?>> names=new ArrayList<>();
        for (String className : exceptionClassNames){
            final Class<?> clazz = forName(className);
            if (Objects.nonNull(clazz)){
                if (Throwable.class.isAssignableFrom(clazz)){
                    names.add(clazz);
                }
            }
        }
        return names.toArray(new Class[0]);
    }

    public Class<? extends RetryListener> getRetryListenerClass() {
        Class clazz=forName(retryListenerClassName);
        if (Objects.nonNull(clazz)){
            if (RetryListener.class.isAssignableFrom(clazz)){
                return clazz;
            }
        }
        return retryListenerClass;
    }
    private static Class<?> forName(String name) {
        if (StringUtils.isNotBlank(name)){
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
