package com.xgf.designpattern.create.singleton;

import java.util.Objects;

/**
 * @author xgf
 * @create 2021-11-17 15:37
 * @description 双检锁/双重校验锁（DCL，即 double-checked locking）
 **/

public class DCLSingleton implements Singleton{

    private volatile static DCLSingleton instance;

    private DCLSingleton(){}
    public static DCLSingleton getInstance(){
        if(Objects.isNull(instance)){
            synchronized (DCLSingleton.class){
                if(Objects.isNull(instance)){
                    instance = new DCLSingleton();
                }
            }
        }
        return instance;
    }


}
