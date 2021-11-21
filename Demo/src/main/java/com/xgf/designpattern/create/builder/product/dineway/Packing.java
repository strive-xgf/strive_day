package com.xgf.designpattern.create.builder.product.dineway;

/**
 * @author xgf
 * @create 2021-11-20 22:24
 * @description 打包接口（打包方式）
 **/

public interface Packing extends Dining {


    /**
     * @return 就餐方式 - 打包
     */
    @Override
    public String dineWay();

    /**
     * 商品打包
     * @return 打包方式
     */
//    public String pack();
}
