package com.xgf.designpattern.structure.bridge;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xgf
 * @create 2021-12-09 20:59
 * @description 桥接模式
 **/

@Slf4j
public abstract class DriverManageBridge {

    /**
     * 驱动对象
     */
    private Driver driver;

    /**
     * 根据驱动对象driver执行对应的sql
     */
    public void execute(String sql){
        driver.executeSql(sql);
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
