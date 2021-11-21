package com.xgf.designpattern.create.builder.product.dineway;

/**
 * @author xgf
 * @create 2021-11-20 23:41
 * @description 堂食
 **/
public interface DineIn extends Dining {

    /**
     * @return 就餐方式 - 堂食
     */
    @Override
    public String dineWay();
}
