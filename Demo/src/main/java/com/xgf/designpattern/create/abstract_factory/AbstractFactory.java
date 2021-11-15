package com.xgf.designpattern.create.abstract_factory;

import com.xgf.designpattern.create.factory.Shape;
import com.xgf.designpattern.create.factory.ShapeTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-11-15 11:03
 * @description 抽象工厂
 **/

public abstract class AbstractFactory {

    /**
     * 获取形状
     * @param typeEnum
     * @return
     */
    public abstract Shape getShape(ShapeTypeEnum typeEnum);

    /**
     * 获取颜色
     * @param colorEnum
     * @return
     */
    public abstract Color getColor(ColorEnum colorEnum);

}
