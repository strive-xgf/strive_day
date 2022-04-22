package com.xgf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author xgf
 * @create 2021-10-31 17:46
 * @description
 **/

@SpringBootApplication
// proxyTargetClass = true 生成代理对象时强制使用CGLIB的方式
// exposeProxy = true 暴露代理对象，也就是可以通过设置这个属性，为true可以解决同一类中两个方法相互调用使aop不生效的问题，也可以用AopContext.currentProxy() 的方式获取到当前的代理对象
//@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableAspectJAutoProxy(exposeProxy = true)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
