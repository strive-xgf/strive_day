package com.xgf.designpattern.structure.bridge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-12-09 20:53
 * @description 基于mysql实现
 **/

@Slf4j
@Component("mySqlDriver")
public class MySqlDriver implements Driver {


    @Override
    public void executeSql(String sql) {
        log.info("====== mysql execute sql = {}", sql);
    }
}
