package com.xgf.designpattern.create.factory;

import com.xgf.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xgf.designpattern.create.factory.*;

import javax.annotation.Resource;

/**
 * @author xgf
 * @create 2021-11-13 15:02
 * @description 工厂方法模式测试类
 **/


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class FactoryTest {

    @Resource
    private ShapeFactory shapeFactory;

    @Test
    public void test(){
        shapeFactory.getShape(ShapeTypeEnum.RECTANGLE).draw();
        shapeFactory.getShape(ShapeTypeEnum.SQUARE).draw();
        shapeFactory.getShape(ShapeTypeEnum.CIRCLE).draw();
    }

}
