package com.xgf.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xgf
 * @create 2021-11-13 15:36
 * @description spring获取对象，解决某些情况下 @Autowired 和 @Resource 为空null
 **/

@Component
@Slf4j
public class SpringUtil implements ApplicationContextAware, DisposableBean {
    private static ApplicationContext applicationContext = null;

    public SpringUtil() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
        log.info("====== SpringUtil setApplicationContext applicationContext = " + applicationContext);
    }

    @Override
    public void destroy() throws Exception {
        applicationContext = null;
        log.info("====== SpringUtil destroy applicationContext ");

    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> Map<String, T> getBeans(Class<T> classType) {
        return applicationContext.getBeansOfType(classType);
    }

}
