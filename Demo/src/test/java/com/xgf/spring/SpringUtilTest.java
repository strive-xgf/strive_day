package com.xgf.spring;

import com.xgf.DemoApplication;
import com.xgf.designpattern.create.factory.ShapeFactory;
import com.xgf.designpattern.create.factory.ShapeTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author xgf
 * @create 2021-11-13 15:52
 * @description SpringUtil工具类测试
 **/


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class SpringUtilTest {

    @Test
    public void test(){
        // NPE空指针
        ShapeFactory shapeFactory = new ShapeFactory();
        shapeFactory.getShape(ShapeTypeEnum.RECTANGLE).draw();
        shapeFactory.getShape(ShapeTypeEnum.SQUARE).draw();
        shapeFactory.getShape(ShapeTypeEnum.CIRCLE).draw();
    }

    @Test
    public void testSpringUtilGetBean(){
        ShapeFactory shapeFactory = SpringUtil.getBean(ShapeFactory.class);
        shapeFactory.getShape(ShapeTypeEnum.RECTANGLE).draw();
        shapeFactory.getShape(ShapeTypeEnum.SQUARE).draw();
        shapeFactory.getShape(ShapeTypeEnum.CIRCLE).draw();
    }


}
