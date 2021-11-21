package com.xgf.designpattern.create.builder.product;

import com.xgf.designpattern.create.builder.constant.UnitEnum;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-21 21:33
 * @description 牛肉汉堡
 **/

@Component("beefBurger")
public class BeefBurger extends Burger {
    @Override
    public String name() {
        return "beefBurger - 牛肉汉堡";
    }

    @Override
    public double price() {
        return 28.88;
    }

    @Override
    public String unit() {
        return UnitEnum.UNIT_ONE_NUMBER.getValue();
    }

}
