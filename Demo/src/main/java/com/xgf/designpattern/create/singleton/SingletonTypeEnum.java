package com.xgf.designpattern.create.singleton;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xgf
 * @create 2021-11-17 16:45
 * @description
 **/
@Getter
@AllArgsConstructor
public enum SingletonTypeEnum {
    ENUM_SINGLETON,
    INNER_CLASS_SINGLETON,
    DCL_SINGLETON,
    HUNGRY_SINGLETON,
    LAZY_SINGLETON,
    LAZY_SINGLETON_NO_LOCK,
    ;
}
