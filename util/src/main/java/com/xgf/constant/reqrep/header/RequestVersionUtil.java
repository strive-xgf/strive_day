package com.xgf.constant.reqrep.header;

import com.xgf.common.JsonUtil;
import com.xgf.common.LogUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.java8.BooleanFunctionUtil;
import com.xgf.java8.ThrowExceptionFunctionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;


/**
 * @author xgf
 * @create 2022-08-15 13:02
 * @description 版本校验工具 - 用于 app 旧版校验
 **/

@Component
public class RequestVersionUtil extends RequestHeaderCommonAble {

    /**
     * 扩展版本号 请求头
     */
    public static final String EXTEND_VERSION_HEADER_KEY = "extend-version";

    /**
     * 是否打印日志
     */
    private static Boolean REQUEST_VERSION_LOG_FLAG;

    /**
     * 给静态变量 REQUEST_VERSION_LOG_FLAG 读取赋值，默认为 false
     */
    @Value("${custom.config.log.requestVersionLogFlag:false}")
    private void setStaticRequestVersionLogFlag(Boolean requestVersionLogFlag) {
        RequestVersionUtil.REQUEST_VERSION_LOG_FLAG = requestVersionLogFlag;
    }


    /**
     * 版本配置
     */
    private static Map<String, String> CHECK_VERSION_MAP;

    /**
     * 给静态变量 LOCK_LOG_FLAG 读取赋值，默认为
     */
    @Value("#{${custom.config.request.versionMap:null}}")
    private void setStaticVersionMap(Map<String, String> versionMap) {
        System.out.println(">>>>>> versionMap = " + versionMap);
        RequestVersionUtil.CHECK_VERSION_MAP = versionMap;
    }


    public static String printVersion() {
        System.out.println("====== RequestVersionUtil.CHECK_VERSION_MAP >> " + RequestVersionUtil.CHECK_VERSION_MAP);
        return JsonUtil.toJsonString(RequestVersionUtil.CHECK_VERSION_MAP);
    }


    public static String getRequestVersion() {
        return Objects.requireNonNull(getRequest())
                .getHeader(EXTEND_VERSION_HEADER_KEY);
    }



    public static void checkVersionWithMobileOrElseThrow(String versionKey) {
        ThrowExceptionFunctionUtil
                .isFalseThrow(checkVersionWithMobile(versionKey))
                .throwMessage("当前版本  " + getRequestVersion() + "  过低，请升级至 " + CHECK_VERSION_MAP.get(versionKey) + " 以上版本");
    }

    /**
     * 手机端口才校验版本号
     *
     * @param versionKey 配置的版本类型key
     * @return 非手机端访问: false，true：访问参数版本 >= 配置版本，false: 访问参数版本 < 配置版本
     */
    public static boolean checkVersionWithMobile(String versionKey) {
        if (!RequestDeviceUtil.isMobile()) {
            return true;
        }

        return checkVersion(versionKey);
    }



    public static void checkVersionOrElseThrow(String versionKey) {
        ThrowExceptionFunctionUtil
                .isFalseThrow(checkVersion(versionKey))
                .throwMessage("当前版本  " + getRequestVersion() + "  过低，请升级至 " + CHECK_VERSION_MAP.get(versionKey) + " 以上版本");
    }

    /**
     * 根据配置versionKey，读取配置版本，比较参数版本值【 (每次使用，旧的版本校验不动，替换 key 就行)】
     *
     * @param versionKey 配置的版本类型key
     * @return true：访问参数版本 >= 配置版本
     *  配置为null || 配置中没有对应key(versionKey) || 请求没有传版本，返回false
     */
    public static boolean checkVersion(String versionKey) {
        // 配置为空
        if (CHECK_VERSION_MAP == null) {
            BooleanFunctionUtil.trueRunnable(REQUEST_VERSION_LOG_FLAG).run(() ->
                    LogUtil.info("CHECK_VERSION_MAP config is null"));
            return false;
        }

        // 请求版本
        String requestVersion = getRequestVersion();
        // 配置版本
        String configVersion = CHECK_VERSION_MAP.get(versionKey);

        // 请求没有传版本
        if (StringUtils.isBlank(requestVersion)) {
            BooleanFunctionUtil.trueRunnable(REQUEST_VERSION_LOG_FLAG).run(() ->
                    LogUtil.info("request version config is null"));
            return false;
        }

        // 配置中不包含校验的版本key（versionKey）
        if (StringUtils.isBlank(configVersion)) {
            BooleanFunctionUtil.trueRunnable(REQUEST_VERSION_LOG_FLAG).run(() ->
                    LogUtil.info("CHECK_VERSION_MAP config {} is blank", versionKey));
            return false;
        }

        return doCompareVersion(requestVersion, configVersion);
    }





    /**
     * 具体比较版本号（按点 . 分割，从左到右依次比较，按最小的位数比较，eg:  10.22.33 和 10.22.23.33.55，则只比较前三个）
     *
     * @param paramVersion 参数的版本号
     * @param configVersion 配置的版本号
     * @return true: paramVersion >= configVersion
     *  >>> 版本号version eg: 22.33.55
     */
    private static boolean doCompareVersion(String paramVersion, String configVersion) {

        ThrowExceptionFunctionUtil.isTureThrow(StringUtils.isEmpty(paramVersion) || StringUtils.isEmpty(configVersion))
                .throwMessage("不能存在版本号值为空, paramVersion = " + paramVersion + ", configVersion " + configVersion);

        String[] paramVersions = paramVersion.split(StringConstantUtil.DOWN_DIAGONAL + StringConstantUtil.DOT);
        String[] configVersions = configVersion.split(StringConstantUtil.DOWN_DIAGONAL + StringConstantUtil.DOT);

        for (int i = 0; i < paramVersions.length && i < configVersions.length; i++) {
            // 相等继续比较下一位
            if (parseVersionInt(paramVersions[i]) == parseVersionInt(configVersions[i])) {
                continue;
            }
            // 入参版本大于配置，返回true
            return parseVersionInt(paramVersions[i]) > parseVersionInt(configVersions[i]);
        }

        // 执行到这里，那就是配置版本一致（相同的参数个数【取少的】）
        return true;
    }


    /**
     * 版本数字字符串转int
     *
     * @param str String
     * @return str转int，如果转换出现异常，返回0
     */
    private static int parseVersionInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            LogUtil.warn("parse version parseInt exception, message = {}", e.getLocalizedMessage(), e);
        }
        return 0;
    }

}
