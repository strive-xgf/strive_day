package com.xgf.designpattern.structure.bridge;

/**
 * @author xgf
 * @create 2021-12-09 21:03
 * @description
 **/

public class MyDriverBridge extends DriverManageBridge {


    @Override
    public void execute(String sql){
        getDriver().executeSql(sql);
    }

}
