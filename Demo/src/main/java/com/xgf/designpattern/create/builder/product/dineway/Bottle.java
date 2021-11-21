package com.xgf.designpattern.create.builder.product.dineway;

import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-20 22:30
 * @description
 **/

@Component(value = "bottle")
public class Bottle implements Packing {

    /**
     * @return 打包方式 - 装瓶
     */
    @Override
    public String dineWay() {
        return "打包 - 瓶装";
    }
}
