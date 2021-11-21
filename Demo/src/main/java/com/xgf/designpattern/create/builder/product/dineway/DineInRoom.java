package com.xgf.designpattern.create.builder.product.dineway;

import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-20 23:45
 * @description
 **/

@Component(value = "dineInRoom")
public class DineInRoom implements DineIn {

    @Override
    public String dineWay() {
        return "堂食 - 餐厅食用";
    }

}
