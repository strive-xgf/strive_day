package com.xgf.designpattern.structure.decorator;

import com.xgf.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xgf
 * @create 2022-01-03 22:00
 * @description
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class DecoratorTest {

    @Resource(name = "source")
    private SourceAble source;

    @Resource(name = "foodDecorator")
    private Decorator foodDecorator;

    @Test
    public void test() {
        source.eat();
        System.out.println("\n>>>>>>>>\n");
        foodDecorator.setSource(source);
        foodDecorator.eat();
    }

}
