package com.xgf.designpattern.create.prototype;

import java.util.Hashtable;
import java.util.UUID;

/**
 * @author xgf
 * @create 2021-11-22 20:43
 * @description 原型模式（将难以初始化/耗费资源大的对象，先初始化到内存中，后面使用clone方法克隆原型对象使用）
 **/

public class PhoneCache {
    private static Hashtable<Integer, Phone> phoneMap
            = new Hashtable<Integer, Phone>();

    /**
     * 从原型数据中获取并返回克隆对象
     * @param
     * @return
     */
    public static Phone getPhonePrototype(Integer phoneId) {
        Phone cachedShape = phoneMap.get(phoneId);
        return (Phone) cachedShape.clone();
    }

    /**
     * 模拟数据库查询出来数据放到缓存中作为原型复制
     */
    public static void loadCache() {
        // 数据原型初始化，（从数据库，或自定义原型）
        Phone vivo = new Vivo();
        vivo.setBrand(PhoneBrand.builder()
                .brandUuid(UUID.randomUUID().toString().replaceAll("=", ""))
                .brandName("vivo").build());
        vivo.setPhoneId(1);
        phoneMap.put(vivo.getPhoneId(), vivo);

        Phone huaWei = new HuaWei();
        huaWei.setBrand(PhoneBrand.builder()
                .brandUuid(UUID.randomUUID().toString().replaceAll("=", ""))
                .brandName("huaWei").build());
        huaWei.setPhoneId(2);
        phoneMap.put(huaWei.getPhoneId(), huaWei);
    }
}

