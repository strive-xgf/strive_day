package com.xgf.designpattern.create.prototype;

import com.alibaba.fastjson.JSON;

/**
 * @author xgf
 * @create 2021-11-22 14:35
 * @description
 **/

public class Vivo extends Phone {

    @Override
    String description() {
        return "vivo phone, brand = " + JSON.toJSONString(getBrand()) + ", phoneId = " + getPhoneId();
    }

}
