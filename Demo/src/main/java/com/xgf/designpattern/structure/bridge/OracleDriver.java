package com.xgf.designpattern.structure.bridge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-12-09 20:57
 * @description 基于oracle实现
 **/

@Slf4j
@Component("oracleDriver")
public class OracleDriver implements Driver {
    @Override
    public void executeSql(String sql) {
        log.info("====== oracle execute sql = {}", sql);
    }
}
