package com.xgf.designpattern.structure.proxy.dynamicproxy.cglib;

import com.alibaba.fastjson.JSON;
import com.xgf.annotation.aspcet.common.CustomAspect;
import com.xgf.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-04-16 16:12
 * @description CGLIB 动态代理
 *  CGLIB（Code Generation Library）是一个高性能开源的代码生成包
 *  采用非常底层的字节码技术，对指定的目标类生成一个子类，并对子类进行增强 [Spring Core包中已经集成了CGLIB所需要的JAR包]
 *  相比JDK动态代理：运行效率高于JDK代理，不需要实现接口
 *
 **/

@Slf4j
@Component("cglibDynamicProxyHandler")
public class CglibDynamicProxyHandler implements MethodInterceptor {

    /**
     * 被代理类（真实委托类） 对象
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
     * @param <T>
     */
    public <T> Object createProxy(Class<T> clz) {
        // 代理类实例化
        byProxy = SpringUtil.getBean(clz);

        // 通过Enhancer创建代理类
        Enhancer enhancer = new Enhancer();
        // 设置代理对象继承类 cls = byProxy.getClass()
        enhancer.setSuperclass(clz);
        // 回调（增强逻辑实现，可多个）【实现MethodInterceptor的类】
        enhancer.setCallback(this);
        // 生成代理
        return enhancer.create();
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

        if (Objects.nonNull(proxyAspect)) {
            proxyAspect.before();
        }

        // 处理特殊逻辑增强
//        if(method.getName().contains("e")){
//            log.info("====== CglibDynamicProxyHandler check param");
//        }

        Object obj = method.invoke(byProxy, objects);

        if (Objects.nonNull(proxyAspect)) {
            proxyAspect.after();
        }

        // 将方法返回结果返回
        return obj;
    }

}
