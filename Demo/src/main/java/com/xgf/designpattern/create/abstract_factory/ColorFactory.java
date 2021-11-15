package com.xgf.designpattern.create.abstract_factory;

import com.xgf.designpattern.create.factory.Shape;
import com.xgf.designpattern.create.factory.ShapeTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author xgf
 * @create 2021-11-15 11:00
 * @description
 **/

@Component
public class ColorFactory extends AbstractFactory{

    @Resource(name = "red")
    private Color red;

    @Resource(name = "green")
    private Color green;

    @Resource(name = "blue")
    private Color blue;

    @Override
    public Shape getShape(ShapeTypeEnum typeEnum) {
        return null;
    }

    /**
     * 根据入参返回绘制对象
     * @param colorEnum 类型
     * @return Shape
     */
    @Override
    public Color getColor(ColorEnum colorEnum){
        if(Objects.isNull(colorEnum)){
            return null;
        }

        switch (colorEnum){
            case RED:
                return red;
            case GREEN:
                return green;
            case BLUE:
                return blue;
            default:
                return null;
        }

    }

}
