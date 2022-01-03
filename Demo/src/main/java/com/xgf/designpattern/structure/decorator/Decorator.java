package com.xgf.designpattern.structure.decorator;

import lombok.Data;

/**
 * @author xgf
 * @create 2022-01-03 19:20
 * @description 装饰器
 **/

@Data
public abstract class Decorator implements SourceAble {

    protected SourceAble source;

    @Override
    public void eat() {
        source.eat();
    }
}
