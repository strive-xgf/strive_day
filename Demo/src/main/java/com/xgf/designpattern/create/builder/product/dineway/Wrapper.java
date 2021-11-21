package com.xgf.designpattern.create.builder.product.dineway;

import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-20 22:27
 * @description
 **/

@Component(value = "wrapper")
public class Wrapper implements Packing {

    /**
     * @return 打包方式，打包盒
     */
    @Override
    public String dineWay() {
        return "打包 - 盒装";
    }
}
