package com.xgf.designpattern.structure.proxy.dynamicproxy;

import com.xgf.annotation.aspcet.common.BeforeAfterAbstractAspect;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xgf
 * @create 2022-01-21 14:18
 * @description 代理切面对象接口定义
 **/

@Slf4j
public class ProxyAspect extends BeforeAfterAbstractAspect {

    @Override
    public void before() {
        startTime = System.currentTimeMillis();
        log.info("====== ProxyAspect before, startTime = 【{}】", startTime);
    }

    @Override
    public void after() {
        long currentTime = System.currentTimeMillis();
        log.info("====== ProxyAspect after, cost = 【{}】, currentTime = 【{}】", currentTime - startTime, currentTime);

    }
}
