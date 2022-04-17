package com.xgf.designpattern.structure.proxy.dynamicproxy.cglib.custom;

import com.xgf.annotation.aspcet.common.CustomAspect;
import com.xgf.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-04-17 10:32
 * @description CGLIB 自定义特殊回调
 *
 **/

@Slf4j
@Component("cglibCustomSpecCallback")
public class CglibCustomSpecCallback implements MethodInterceptor {

    @Resource(name = "customAspectSelector")
    private CustomAspectSelector customAspectSelector;

    /**
     * 被代理类（真实委托类） 对象
     * 目标对象实例（通过 createProxy 方法初始化）
     */
    private Object byProxy;

    private void setByProxy(Object byProxy) {
        this.byProxy = byProxy;
    }

    /**
     * 获取 CglibCustomSpecCallback 实例对象
     * @param byProxy 被代理类
     * @return CglibCustomSpecCallback 实例对象
     */
    public static CglibCustomSpecCallback getCglibCustomSpecCallback(Object byProxy) {
        // 使用 Spring 获取bean，否则注入为 null
        CglibCustomSpecCallback cglibCustomSpecCallback = SpringUtil.getBean(CglibCustomSpecCallback.class);
        cglibCustomSpecCallback.setByProxy(byProxy);
        return cglibCustomSpecCallback;
    }

    /**
     * 对目标类进行增强
     * @param object 代理对象
     * @param method 执行方法
     * @param objects 方法参数
     * @param methodProxy 方法代理
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        // 代理切面对象，这里处理特殊逻辑增强【进入此方法默认切面（不再由用户指定）】
        CustomAspect proxyAspect = customAspectSelector.selectorAspect(method.getName());

        if (Objects.nonNull(proxyAspect)) {
            proxyAspect.before();
        }

        Object obj = method.invoke(byProxy, objects);

        if (Objects.nonNull(proxyAspect)) {
            proxyAspect.after();
        }

        // 将方法返回结果返回
        return obj;
    }

}
