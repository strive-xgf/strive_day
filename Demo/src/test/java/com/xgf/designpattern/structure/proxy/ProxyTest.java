package com.xgf.designpattern.structure.proxy;

import com.alibaba.fastjson.JSON;
import com.xgf.DemoApplication;
import com.xgf.designpattern.structure.proxy.dynamicproxy.ProxyAspect;
import com.xgf.designpattern.structure.proxy.dynamicproxy.cglib.CglibDynamicProxyHandler;
import com.xgf.designpattern.structure.proxy.dynamicproxy.cglib.custom.CaseMethodService;
import com.xgf.designpattern.structure.proxy.dynamicproxy.cglib.custom.CglibDynamicProxyMultiHandler;
import com.xgf.designpattern.structure.proxy.dynamicproxy.jdk.JdkDynamicProxyHandler;
import com.xgf.designpattern.structure.proxy.staticproxy.StaticProxyEmploy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xgf
 * @create 2022-01-20 14:57
 * @description 代理模式测试
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class ProxyTest {

    @Resource(name = "staticProxyEmploy")
    private StaticProxyEmploy staticProxyEmploy;

    @Resource(name = "jdkDynamicProxyHandler")
    private JdkDynamicProxyHandler jdkDynamicProxyHandler;

    @Resource(name = "cglibDynamicProxyHandler")
    private CglibDynamicProxyHandler cglibDynamicProxyHandler;

    @Resource(name = "cglibDynamicProxyMultiHandler")
    private CglibDynamicProxyMultiHandler cglibDynamicProxyMultiHandler;

    @Test
    public void testStatic() {
        List<Employee> employeeList = staticProxyEmploy.employPeople("Java");
        printList(employeeList);
    }

    @Test
    public void testJdkDynamic() throws InterruptedException {
        // JDK 动态代理，只能通过上级抽象接口调用，下述用法会报错
//        StaticProxyEmploy companyEmploy = (StaticProxyEmploy) jdkDynamicProxyHandler.createProxy(StaticProxyEmploy.class);
        // 创建代理对象【上级抽象接口】，内部自动注入对象为 null
//        CompanyEmploy companyEmploy = (CompanyEmploy) jdkDynamicProxyHandler.createProxy(StaticProxyEmploy.class);
        CompanyEmploy companyEmploy = (CompanyEmploy) jdkDynamicProxyHandler.createProxy(StaticProxyEmploy.class, new ProxyAspect());
        printList(companyEmploy.employPeople("Python"));
        Thread.sleep(2000);
        printList(companyEmploy.employPeople("Java"));
        printList(companyEmploy.employPeople("Java"));
        printList(companyEmploy.employPeople("Java"));
        printList(companyEmploy.employPeople("Java"));
    }

    @Test
    public void testCglibDynamic(){

//        CompanyEmploy companyEmploy = (CompanyEmploy) cglibDynamicProxyHandler.createProxy(StaticProxyEmploy.class, new ProxyAspect());
        StaticProxyEmploy companyEmploy = (StaticProxyEmploy) cglibDynamicProxyHandler.createProxy(StaticProxyEmploy.class, new ProxyAspect());
        // 方法返回结果，强转为空
        System.out.println(">>>>>> companyEmploy= " + JSON.toJSONString(companyEmploy));

        printList(companyEmploy.employPeople("Python"));
        printList(companyEmploy.employPeople("Java"));

    }

    @Test
    public void testCglibDynamicFilter() {
        CaseMethodService caseMethodService = (CaseMethodService) cglibDynamicProxyMultiHandler.createProxy(CaseMethodService.class, new ProxyAspect());

        // 调用自定义回调函数
        caseMethodService.saveOneObject();
        // 不满足条件，继续调用一般过滤CglibDynamicProxyHandler
        caseMethodService.batchSaveObject();
        // 调用自定义回调函数
        caseMethodService.deleteOneObject();
        // 调用自定义回调函数
        System.out.println("caseMethodService.saveOneObjectReturnResult() = " + caseMethodService.saveOneObjectReturnResult());;

    }

    private <T> void printList(List<T> list) {
        list.forEach(p -> System.out.println(p.toString()));
    }


}
