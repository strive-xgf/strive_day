package com.xgf.designpattern.structure.bridge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2021-12-09 20:56
 * @description 基于sqlSever实现
 **/

@Slf4j
@Component("sqlServerDriver")
public class SqlServerDriver implements Driver {

    @Override
    public void executeSql(String sql) {
        log.info("====== sqlServer execute sql = {}", sql);
    }
}
