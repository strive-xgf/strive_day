package com.xgf.designpattern.structure.bridge;

/**
 * @author xgf
 * @create 2021-12-09 20:51
 * @description
 **/
public interface Driver {

    /**
     * 执行sql语句
     * @param sql sal语句
     */
    void executeSql(String sql);

}
