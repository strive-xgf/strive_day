package com.xgf.designpattern.structure.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2022-01-04 14:06
 * @description 汽车自动检查装置
 **/

@Slf4j
@Component("autoCheckDevice")
public class AutoCheckDevice implements CarGeneral {
    @Override
    public void startUp() {
        log.info("====== AutoCheckDevice startUp");
    }

    @Override
    public void shutDown() {
        log.info("====== AutoCheckDevice shutDown");
    }
}
