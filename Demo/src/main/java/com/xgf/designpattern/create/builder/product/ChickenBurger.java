package com.xgf.designpattern.create.builder.product;

import com.xgf.designpattern.create.builder.constant.UnitEnum;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-21 20:57
 * @description 鸡肉汉堡
 **/

@Component(value = "chickenBurger")
public class ChickenBurger extends Burger {




    @Override
    public String name() {
        return "chickenBurger - 肌肉汉堡";
    }

    @Override
    public double price() {
        return 12.88;
    }

    @Override
    public String unit() {
        return UnitEnum.UNIT_ONE_NUMBER.getValue();
    }

}
