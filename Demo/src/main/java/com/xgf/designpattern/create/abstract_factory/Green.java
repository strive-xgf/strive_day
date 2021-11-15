package com.xgf.designpattern.create.abstract_factory;

import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-15 10:58
 * @description
 **/

@Component(value = "green")
public class Green implements Color{
    @Override
    public void fill() {
        System.out.println("=== fill Green");
    }
}
