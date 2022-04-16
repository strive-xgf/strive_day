package com.xgf.designpattern.structure.proxy.dynamicproxy.jdk;

import com.xgf.annotation.aspcet.common.CustomAspect;
import com.xgf.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-01-21 13:58
 * @description JDK 动态代理（接口代理）
 *  是 java.lang.reflect.* 包提供的方式，
 *  它必须借助一个接口才能产生代理对象（案例：对于使用业务接口的类，Spring默认使用JDK动态代理实现AOP)
 **/

@Slf4j
@Component("jdkDynamicProxyHandler")
public class JdkDynamicProxyHandler implements InvocationHandler {

    /**
     * 被代理类（真实委托类） 对象 [Proxy.newProxyInstance()生成的代理类对象]
     * 目标对象实例（通过 createProxy 方法初始化）
     */
    private Object byProxy;

    /**
     * 代理切面对象
     */
    private CustomAspect proxyAspect;

    public void setProxyAspect(CustomAspect proxyAspect) {
        this.proxyAspect = proxyAspect;
    }

    /**
     * 创建代理对象 byProxy
     * @param clz 被代理类
     * @param <T> 代理对象
     */
    public <T> Object createProxy(Class<T> clz) {

        // 实例初始化，如果直接实例化，类的内部自动注入为null
//        try {
//            target = clz.newInstance();
//        } catch (InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//        }

        // 代理类实例化
        byProxy = SpringUtil.getBean(clz);

        // 参2: interfaces 被代理对象的实现接口
        return Proxy.newProxyInstance(clz.getClassLoader(), clz.getInterfaces(), this);
    }

    /**
     * 创建代理对象，并设置代理切面
     * @param clz 代理类
     * @param aspect 切面
     * @param <T>
     * @return 代理对象
     */
    public <T> Object createProxy(Class<T> clz, CustomAspect aspect) {
        setProxyAspect(aspect);
        return createProxy(clz);
    }

    /**
     * 调用执行方法
     *
     * @param proxy 通过Proxy.newProxyInstance()生成的代理类对象
     * @param method 表示代理对象被调用的函数
     * @param args 表示代理类对象被调用的函数的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (Objects.nonNull(proxyAspect)) {
            proxyAspect.before();
        }

        Object obj = method.invoke(byProxy, args);

        if (Objects.nonNull(proxyAspect)) {
            proxyAspect.after();
        }

        return obj;
    }
}
