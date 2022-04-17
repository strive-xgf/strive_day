package com.xgf.designpattern.structure.proxy.dynamicproxy.cglib.custom;

import com.xgf.annotation.aspcet.common.CustomAspect;
import com.xgf.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-04-16 16:12
 * @description CGLIB 动态代理（多回调实现）
 * 单回调 {@link com.xgf.designpattern.structure.proxy.dynamicproxy.cglib.CglibDynamicProxyHandler}
 *
 **/

@Slf4j
@Component("cglibDynamicProxyMultiHandler")
public class CglibDynamicProxyMultiHandler implements MethodInterceptor {

    @Resource(name = "cglibCallbackFilter")
    private CglibCallbackFilter cglibCallbackFilter;

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
     * 定义多个回调函数用于实现
     * @param byProxy 被代理对象
     * @return 回调数组
     */
    private Callback[] getCallbacks(Object byProxy) {
        return new Callback[] {
                this,
                CglibCustomSpecCallback.getCglibCustomSpecCallback(byProxy)
        };
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
//        enhancer.setCallback(this);
        enhancer.setCallbacks(getCallbacks(byProxy));
        // 设置回调过滤器【实现不同的方法使用不同的回调方法】
        enhancer.setCallbackFilter(cglibCallbackFilter);

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

        Object obj = method.invoke(byProxy, objects);

        if (Objects.nonNull(proxyAspect)) {
            proxyAspect.after();
        }

        // 将方法返回结果返回
        return obj;
    }

}
