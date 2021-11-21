package com.xgf.designpattern.create.builder.product;

import com.xgf.designpattern.create.builder.constant.UnitEnum;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-21 21:39
 * @description 百事可乐
 **/

@Component(value = "pepsi")
public class Pepsi extends ColdDrink {
    @Override
    public String name() {
        return "pepsi - 百事可乐";
    }

    @Override
    public double price() {
        return 3.8;
    }

    @Override
    public String unit() {
        return UnitEnum.UNIT_ONE_BOTTLE.getValue();
    }

}
