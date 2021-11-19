package com.xgf.designpattern.create.singleton;

/**
 * @author xgf
 * @create 2021-11-17 15:34
 * @description 饿汉式
 **/

public class HungrySingleton implements Singleton{

    private static HungrySingleton instance = new HungrySingleton();

    private HungrySingleton(){}

    public static HungrySingleton getInstance(){
        return instance;
    }

}
