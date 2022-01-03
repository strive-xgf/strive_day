package com.xgf.designpattern.structure.decorator;

import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2022-01-03 19:19
 * @description
 **/


@Component("source")
public class Source implements SourceAble {

    @Override
    public void eat() {
        System.out.println("====== eat rice");
    }
}
