package com.xgf.designpattern.create.singleton;

/**
 * @author xgf
 * @create 2021-11-17 15:41
 * @description 静态内部类/登记式，能达到双重校验锁的功效，实现简单，内部类被使用时才会初始化实例
 **/

public class InnerClassSingleton implements Singleton{

    private static class SingletonHolder {
        private static final InnerClassSingleton INSTANCE = new InnerClassSingleton();
    }

    private InnerClassSingleton (){}

    public static final InnerClassSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
