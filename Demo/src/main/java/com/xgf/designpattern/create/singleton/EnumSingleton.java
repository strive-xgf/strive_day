package com.xgf.designpattern.create.singleton;

/**
 * @author xgf
 * @create 2021-11-17 15:45
 * @description 枚举实现，是实现单例模式的最佳方法。更简洁，【自动支持序列化机制】，绝对防止多次实例化
 *          不仅能避免多线程同步问题，而且还自动支持序列化机制，防止反序列化重新创建新的对象，绝对防止多次实例化（jdk1.5之后引入枚举enum）
 **/

public enum EnumSingleton implements Singleton{
    INSTANCE;

    public static EnumSingleton getInstance(){
        return INSTANCE;
    }
    public void executeMethod() {
        System.out.println("EnumSingleton executeMethod");
    }
}
