package com.xgf.designpattern.structure.flyweight;

import com.alibaba.fastjson.JSON;
import com.xgf.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import java.net.*;
import java.util.*;

/**
 * @author xgf
 * @create 2022-01-08 16:39
 * @description 缓存工厂（用于申请和使用共有对象）【以 HashMap 形式缓存用户信息】
 **/


@Slf4j
public class UserCacheFactory {

    /**
     * 用户缓存共享
     * key : companyUuid + userUuid
     * value : UserCommon
     */
    private static final Map<String, UserCommon> USER_COMMON_MAP = new HashMap<>();

    /**
     * 获取用户（不存在就生成放入缓存中）
     * @param companyUuid 公司uuid
     * @param deptUuid 部门uuid 【todo 部门uuid应该从数据库获取】
     * @param userUuid 用户uuid
     */
    public static UserCommon getUserCache(String companyUuid, String deptUuid, String userUuid) {

        UserCommon user = USER_COMMON_MAP.get(companyUuid + userUuid);

        if (Objects.isNull(user)) {
            user = UserCommon.builder().companyUuid(companyUuid).deptUuid(deptUuid).userUuid(userUuid).build();
            USER_COMMON_MAP.put(user.getCompanyUuid() + user.getUserUuid(), user);
        }

        if (BooleanUtils.isNotTrue(user.getUseFlag())) {
            user.setUseTime(new Date());
            user.setUseFlag(Boolean.TRUE);
            user.setUseIpAddress(getHostIpAddress());
        } else {
            // 用户被占用，查看是否是本人使用
            if (! Objects.equals(getHostIpAddress(), user.getUseIpAddress())) {
                throw new CustomException("用户已经被登录，ip地址 : " + user.getUseIpAddress());
            }
        }

        user.generateUserToken();

        return user;
    }

    /**
     * 释放用户缓存
     * @param companyUuid 公司uuid
     * @param userUuid 用户uuid
     */
    public static void releaseUserCache(String companyUuid, String userUuid) {
        UserCommon user = USER_COMMON_MAP.get(companyUuid + userUuid);

        if (Objects.nonNull(user)) {
            user.setUseFlag(Boolean.FALSE);
        }

    }

    /**
     * 从缓存中清除
     * @param companyUuid 公司uuid
     * @param userUuid 用户uuid
     */
    public static void clearUserCache(String companyUuid, String userUuid) {
        UserCommon user = USER_COMMON_MAP.get(companyUuid + userUuid);

        if (Objects.nonNull(user)) {
            log.info("====== clearUserCache user = {}", JSON.toJSONString(user));
            USER_COMMON_MAP.remove(companyUuid + userUuid);
        }

    }

    /**
     * 清空缓存
     */
    public static void clearAllUserCache() {
        USER_COMMON_MAP.clear();
    }


    /**
     * 获取用户机器ip地址
     *
     * @return ipv4地址
     */
    private static String getHostIpAddress() {

        Enumeration<NetworkInterface> allNets;
        String ipAddress = null;
        try {
            // 获得本机的所有网络接口
            allNets = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            log.error("====== getHostIpAddress exception, message = {}", e.getMessage(), e);
            return null;
        }
        while (allNets.hasMoreElements()) {
            NetworkInterface nif = allNets.nextElement();
            // System.out.println("nif.getName() = " + nif.getName()) ;
            // 获得与该网络接口绑定的 IP 地址【一般是一个】
            Enumeration<InetAddress> addresses = nif.getInetAddresses();
            // && "ppp0".equals(nif.getName())
            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();
                // ipv4地址 System.out.println("ipv4 address ："+ ip.getHostAddress());
                if (Objects.nonNull(ip)
                        && Objects.isNull(ipAddress)
                        && ip instanceof Inet4Address) {
                    ipAddress = ip.getHostAddress();
                }
            }
        }

        return ipAddress;
    }


}
