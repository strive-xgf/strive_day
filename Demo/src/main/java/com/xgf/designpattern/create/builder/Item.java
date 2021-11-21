package com.xgf.designpattern.create.builder;


import com.xgf.designpattern.create.builder.product.dineway.Dining;

/**
 * @author xgf
 * @create 2021-11-21 17:04
 * @description 食品条目（每样食品属性组合）
 **/
public interface Item {

    /**
     * 食品名称
     * @return 名称
     */
    public String name();

//    /**
//     * 就餐方式
//     * @param way 就餐方式（枚举code）
//     * @return 方式
//     */
//    public Dining dining(String way);

    public Dining dining();

    /**
     * 单价
     * @return 价格
     */
    public double price();

    /**
     * 单位
     * @return 单位
     */
    public String unit();

}
