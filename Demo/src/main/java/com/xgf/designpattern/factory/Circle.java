package com.xgf.designpattern.factory;

import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-13 14:50
 * @description
 **/

@Component("circle")
public class Circle implements Shape{
    @Override
    public void draw() {
        System.out.println("=== draw Circle");
    }
}
