package com.xgf.designpattern.create.abstract_factory;

import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-15 10:59
 * @description
 **/

@Component(value = "red")
public class Red implements Color{
    @Override
    public void fill() {
        System.out.println("=== fill Red");
    }
}
