package com.xgf.designpattern.create.singleton;

import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author xgf
 * @create 2021-11-17 15:53
 * @description 单例模式选择器
 **/

@Component
public class SingletonSelector {

    /**
     * 执行
     */
    public Singleton getSingletonInstance(SingletonTypeEnum typeEnum){
        if(Objects.isNull(typeEnum)){
            return null;
        }
        switch (typeEnum){
            case ENUM_SINGLETON:
                return EnumSingleton.getInstance();
            case INNER_CLASS_SINGLETON:
                return InnerClassSingleton.getInstance();
            case DCL_SINGLETON:
                return DCLSingleton.getInstance();
            case HUNGRY_SINGLETON:
                return HungrySingleton.getInstance();
            case LAZY_SINGLETON:
                return LazySingleton.getInstance();
            case LAZY_SINGLETON_NO_LOCK:
                return LazySingletonNoLock.getInstance();
            default:
                return null;
        }
    }

}
