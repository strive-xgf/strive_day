package com.xgf.designpattern.create.builder.product;

import com.xgf.designpattern.create.builder.constant.UnitEnum;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-21 21:38
 * @description
 **/

@Component("coke")
public class Coke extends ColdDrink {
    @Override
    public String name() {
        return "coke - 可口可乐";
    }

    @Override
    public double price() {
        return 5;
    }

    @Override
    public String unit() {
        return UnitEnum.UNIT_ONE_BOTTLE.getValue();
    }

}
