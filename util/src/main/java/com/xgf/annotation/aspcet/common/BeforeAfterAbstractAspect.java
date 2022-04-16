package com.xgf.annotation.aspcet.common;

/**
 * @author xgf
 * @create 2022-01-21 14:18
 * @description 前后抽象切面
 **/

public abstract class BeforeAfterAbstractAspect implements CustomAspect {

    /**
     * 方法执行开始时间
     */
    protected long startTime;

    /**
     * 前置通知
     */
    @Override
    public abstract void before();

    /**
     * 后置（最后）通知
     */
    @Override
    public abstract void after();



    @Override
    public void around() {

    }

    @Override
    public void afterReturning() {

    }

    @Override
    public void afterThrowing() {

    }
}
