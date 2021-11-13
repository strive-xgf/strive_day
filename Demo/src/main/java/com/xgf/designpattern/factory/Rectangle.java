package com.xgf.designpattern.factory;

import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-13 14:47
 * @description
 **/

@Component("rectangle")
public class Rectangle implements Shape{
    @Override
    public void draw() {
        System.out.println("=== draw Rectangle");
    }
}
