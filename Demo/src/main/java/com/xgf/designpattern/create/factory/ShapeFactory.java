package com.xgf.designpattern.create.factory;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author xgf
 * @create 2021-11-13 14:50
 * @description 工厂模式
 **/

@Component
public class ShapeFactory {

    @Resource(name = "rectangle")
    private Shape rectangle;

    @Resource(name = "square")
    private Shape square;

    @Resource(name = "rectangle")
    private Shape circle;

    /**
     * 根据入参返回绘制对象
     * @param typeEnum 类型
     * @return Shape
     */
    public Shape getShape(ShapeTypeEnum typeEnum){
        if(Objects.isNull(typeEnum)){
            return null;
        }

        switch (typeEnum){
            case RECTANGLE:
                return rectangle;
            case SQUARE:
                return square;
            case CIRCLE:
                return circle;
            default:
                return null;
        }

    }

}
