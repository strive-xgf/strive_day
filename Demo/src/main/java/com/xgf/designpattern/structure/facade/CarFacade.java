package com.xgf.designpattern.structure.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author xgf
 * @create 2022-01-04 14:07
 * @description 外观模式（门面模式） - 汽车 Facade 提供给用户使用
 **/

@Slf4j
@Controller
public class CarFacade {

    @Resource(name = "dashBoard")
    private CarGeneral dashBoard;

    @Resource(name = "engine")
    private CarGeneral engine;

    @Resource(name = "autoCheckDevice")
    private CarGeneral autoCheckDevice;

    public void startUp() {
        System.out.println("====== CarFacade startUp start ======");
        engine.startUp();
        dashBoard.startUp();
        autoCheckDevice.startUp();
        System.out.println(">>>>>> CarFacade startUp success end <<<<<<");
    }

    public void shutDown() {
        System.out.println("====== AutoCheckDevice shutDown start ======");
        engine.shutDown();
        autoCheckDevice.shutDown();
        dashBoard.shutDown();
        System.out.println(">>>>>> AutoCheckDevice shutDown success end <<<<<<");
    }

}
