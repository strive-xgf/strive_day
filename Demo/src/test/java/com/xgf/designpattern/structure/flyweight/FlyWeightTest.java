package com.xgf.designpattern.structure.flyweight;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

/**
 * @author xgf
 * @create 2022-01-09 16:56
 * @description 享元模式测试
 **/

public class FlyWeightTest {

    @Test
    public void test() {
        // 获取相同用户，都是获取到同一个 UserCommon 对象 (字段值都一样)
        UserCommon userCommon = UserCacheFactory.getUserCache("阿里巴巴", "管理一部", "strive_123");
        System.out.println("userCommon = " + JSON.toJSONString(userCommon));
        UserCommon userCommon2 = UserCacheFactory.getUserCache("阿里巴巴", "管理一部", "strive_123");
        System.out.println("userCommon2 = " + JSON.toJSONString(userCommon2));
        System.out.println("【userCommon == userCommon2】 = " + (userCommon == userCommon2));

        // 释放用户使用，再获取，还是同一个 UserCommon 对象，但是会重新生成使用时间和对应token
        System.out.println("\n====== 释放使用用户 ======");
        UserCacheFactory.releaseUserCache("阿里巴巴", "strive_123");
        System.out.println("userCommon = " + JSON.toJSONString(userCommon));
        UserCommon userCommon3 = UserCacheFactory.getUserCache("阿里巴巴", "管理一部", "strive_123");
        System.out.println("userCommon3 = " + JSON.toJSONString(userCommon3));
        System.out.println("【userCommon == userCommon3】 = " + (userCommon == userCommon3));

        System.out.println("\n====== 更换默认ip ======");
        // 更换 ip 地址，不释放就获取会报错，已被使用，释放后则获取成功 【注释掉这一行报错】 还是同一个 UserCommon 对象
        UserCacheFactory.releaseUserCache("阿里巴巴", "strive_123");
        userCommon.setUseIpAddress("1-1-1-1");
        UserCommon userCommon4 = UserCacheFactory.getUserCache("阿里巴巴", "管理一部", "strive_123");
        System.out.println("userCommon4 = " + JSON.toJSONString(userCommon3));

        System.out.println("\n====== 更换一个对象 ======");
        UserCommon userCommon5 = UserCacheFactory.getUserCache("腾讯", "企鹅小组", "strive_123");
        System.out.println("userCommon5 = " + JSON.toJSONString(userCommon5));
        System.out.println("【userCommon == userCommon5】 = " + (userCommon == userCommon5));

        // 清除用户缓存，UserCommon 对象就不是同一个了

        System.out.println("\n====== 清除对象缓存 =====");
        UserCacheFactory.clearUserCache("阿里巴巴", "strive_123");
        UserCommon userCommon6 = UserCacheFactory.getUserCache("阿里巴巴", "管理一部", "strive_123");
        System.out.println("userCommon6 = " + JSON.toJSONString(userCommon6));
        System.out.println("【userCommon == userCommon6】 = " + (userCommon == userCommon6));


    }

}
