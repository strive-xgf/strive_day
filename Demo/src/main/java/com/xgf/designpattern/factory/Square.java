package com.xgf.designpattern.factory;

import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-13 14:48
 * @description
 **/

@Component("square")
public class Square implements Shape{
    @Override
    public void draw() {
        System.out.println("=== draw Square");
    }
}
