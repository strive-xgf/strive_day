package com.xgf.designpattern.create.abstract_factory;

import com.xgf.designpattern.create.factory.Shape;
import com.xgf.designpattern.create.factory.ShapeFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author xgf
 * @create 2021-11-15 11:08
 * @description 获取对应工厂
 **/

@Component
public class FactoryProducer {

    @Resource
    private ShapeFactory shapeFactory;

    @Resource
    private ColorFactory colorFactory;

    public AbstractFactory getFactory(FactoryTypeEnum factoryTypeEnum){
        if(Objects.isNull(factoryTypeEnum)){
            return null;
        }

        switch (factoryTypeEnum){
            case FACTORY_SHAPE:
                return shapeFactory;
            case FACTORY_COLOR:
                return colorFactory;
            default:
                return null;
        }
    }
}
