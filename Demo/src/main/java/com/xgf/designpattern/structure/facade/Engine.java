package com.xgf.designpattern.structure.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2022-01-04 14:04
 * @description 汽车引擎、发动机
 **/

@Slf4j
@Component("engine")
public class Engine implements CarGeneral {

    @Override
    public void startUp() {
        log.info("====== Engine startUp");
    }

    @Override
    public void shutDown() {
        log.info("====== Engine shutDown");
    }
}
