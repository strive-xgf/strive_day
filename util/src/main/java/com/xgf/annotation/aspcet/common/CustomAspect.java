package com.xgf.annotation.aspcet.common;

/**
 * @author xgf
 * @create 2022-01-21 14:18
 * @description 自定义切面通用抽象
 **/

public interface CustomAspect {

    /**
     * 前置通知
     */
    public void before();

    /**
     * 后置（最后）通知
     */
    public void after();

    /**
     * 环绕通知
     */
    public void around();

    /**
     * 后置返回通知
     */
    public void afterReturning();

    /**
     * 异常通知
     */
    public void afterThrowing();


}
