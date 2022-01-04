package com.xgf.designpattern.structure.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2022-01-04 13:56
 * @description 汽车仪表盘
 **/

@Slf4j
@Component("dashBoard")
public class DashBoard implements CarGeneral {

    @Override
    public void startUp() {
        log.info("====== DashBoard startUp");
    }

    @Override
    public void shutDown() {
        log.info("====== DashBoard shutDown");
    }
}
