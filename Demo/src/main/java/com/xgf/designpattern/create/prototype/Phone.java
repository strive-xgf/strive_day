package com.xgf.designpattern.create.prototype;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xgf
 * @create 2021-11-22 00:24
 * @description 实现Cloneable的抽象类
 **/

@Getter
@Setter
public abstract class Phone implements Cloneable {

    private Integer phoneId;
    private PhoneBrand brand;

    @Override
    protected Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    /**
     * 商品描述
     */
    abstract String description();

}
