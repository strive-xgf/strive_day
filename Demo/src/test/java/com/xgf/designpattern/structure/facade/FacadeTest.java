package com.xgf.designpattern.structure.facade;

import com.xgf.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xgf
 * @create 2022-01-04 13:52
 * @description 外观模式 / 门面模式
 * 【向现有的系统添加一个（高层）接口，来隐藏系统的复杂性 （提供一个类，写死调用方，直接调用方法简单使用，降低访问复杂度】
 * 案例：转动钥匙启动汽车，后台就会自动完成引擎启动、仪表盘启动、车辆自检等过程。
 *      通过外观模式将汽车启动这一系列流程封装到启动按钮上，对于用户来说只需按下启动按钮即可（简化复杂操作）
 *      就是提供一个组合接口，用户只需要简单调用，不需要了解里面复杂逻辑，就完成了相应功能
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class FacadeTest {

    @Autowired
    private CarFacade carFacade;

    @Test
    public void test() {
        carFacade.startUp();

        System.out.println("\n--------------------\n");

        carFacade.shutDown();
    }
}
