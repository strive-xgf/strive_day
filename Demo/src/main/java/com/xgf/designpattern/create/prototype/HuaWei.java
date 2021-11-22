package com.xgf.designpattern.create.prototype;

import com.alibaba.fastjson.JSON;

/**
 * @author xgf
 * @create 2021-11-22 14:53
 * @description
 **/

public class HuaWei extends Phone {
    @Override
    String description() {
        return "huawei phone, brand = " + JSON.toJSONString(getBrand()) + ", phoneId = " + getPhoneId();
    }
}
