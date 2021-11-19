package com.xgf.designpattern.create.singleton;

import java.util.Objects;

/**
 * @author xgf
 * @create 2021-11-17 15:30
 * @description 懒汉式，线程安全，第一次调用初始化，其它情况都不需要，加锁影响性能， 浪费内存
 **/

public class LazySingleton implements Singleton{

    private static LazySingleton instance;

    private LazySingleton(){}

    public static synchronized LazySingleton getInstance(){
        if(Objects.isNull(instance)){
            instance = new LazySingleton();
        }
        return instance;
    }

}
