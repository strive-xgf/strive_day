package com.xgf.constant.reqrep.header;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-08-15 10:43
 * @description 请求设备工具类
 **/

public class RequestDeviceUtil extends RequestHeaderCommonAble {

    /**
     * 请求设备 请求头
     */
    public static final String EXTEND_DEVICE_TYPE_HEADER_KEY = "extend-device-type";

    /**
     * android 端口
     */
    public static final String ANDROID = "android";

    /**
     * ios 端口
     */
    public static final String IOS = "ios";

    /**
     * pc 端口
     */
    public static final String PC = "pc";


    public static boolean isMobile() {
        return isMobile(getRequestDevice());
    }

    public static boolean isMobile(HttpServletRequest req) {
        return isMobile(req.getHeader(EXTEND_DEVICE_TYPE_HEADER_KEY));
    }

    public static boolean isMobile(String deviceType) {
        return ANDROID.equalsIgnoreCase(deviceType) || IOS.equalsIgnoreCase(deviceType);
    }

    public static boolean isPc() {
        return isPc(getRequestDevice());
    }

    public static boolean isPc(HttpServletRequest req) {
        return isPc(req.getHeader(EXTEND_DEVICE_TYPE_HEADER_KEY));
    }

    public static boolean isPc(String deviceType) {
        return PC.equalsIgnoreCase(deviceType);
    }

    public static String getRequestDevice() {
        return Objects.requireNonNull(getRequest())
                .getHeader(EXTEND_DEVICE_TYPE_HEADER_KEY);
    }

}
