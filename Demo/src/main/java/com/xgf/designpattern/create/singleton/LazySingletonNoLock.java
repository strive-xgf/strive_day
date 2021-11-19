package com.xgf.designpattern.create.singleton;

import java.util.Objects;

/**
 * @author xgf
 * @create 2021-11-17 15:26
 * @description 单例模式 - 懒汉式，不加锁，多线程不安全
 **/

public class LazySingletonNoLock implements Singleton{

    private static LazySingletonNoLock instance;

    /**
     * 构造函数私有化
     */
    private LazySingletonNoLock(){}

    public static LazySingletonNoLock getInstance() {
        if (Objects.isNull(instance)) {
            instance = new LazySingletonNoLock();
        }
        return instance;
    }

}
