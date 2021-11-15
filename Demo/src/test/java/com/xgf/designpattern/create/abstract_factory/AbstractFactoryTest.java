package com.xgf.designpattern.create.abstract_factory;

import com.xgf.DemoApplication;
import com.xgf.designpattern.create.factory.ShapeFactory;
import com.xgf.designpattern.create.factory.ShapeTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xgf
 * @create 2021-11-15 11:31
 * @description 抽象工厂测试
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class AbstractFactoryTest {

    @Resource
    private FactoryProducer factoryProducer;

    @Resource
    private ShapeFactory shapeFactory;


    @Test
    public void test(){
        factoryProducer.getFactory(FactoryTypeEnum.FACTORY_SHAPE).getShape(ShapeTypeEnum.RECTANGLE).draw();
        factoryProducer.getFactory(FactoryTypeEnum.FACTORY_SHAPE).getShape(ShapeTypeEnum.SQUARE).draw();
        factoryProducer.getFactory(FactoryTypeEnum.FACTORY_SHAPE).getShape(ShapeTypeEnum.CIRCLE).draw();

        factoryProducer.getFactory(FactoryTypeEnum.FACTORY_COLOR).getColor(ColorEnum.RED).fill();
        factoryProducer.getFactory(FactoryTypeEnum.FACTORY_COLOR).getColor(ColorEnum.GREEN).fill();
        factoryProducer.getFactory(FactoryTypeEnum.FACTORY_COLOR).getColor(ColorEnum.BLUE).fill();

    }

}
