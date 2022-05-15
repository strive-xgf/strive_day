package com.xgf.http;

import com.xgf.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xgf
 * @create 2022-05-13 16:11
 * @description http cookie工具类
 **/

public class CookieUtil {
    protected final static transient Logger log = LoggerFactory.getLogger(CookieUtil.class);

    /**
     * 新增 cookie
     *
     * @param response http 响应
     * @param name     cookie名字
     * @param value    cookie值
     * @param maxAge   cookie生命周期（单位：秒）
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    /**
     * 清除 cookie
     *
     * @param request http 请求
     * @param name    cookie 的name
     */
    public static void cleanCookie(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = getAllCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            cookie.setMaxAge(0);
        }
    }

    /**
     * 读取 cookie 信息封装到 Map 中
     *
     * @param request http 请求
     * @return cookie信息的map集合
     */
    private static Map<String, Cookie> getAllCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }

        return cookieMap;
    }

    /**
     * 根据 name 获取 cookie 信息
     *
     * @param name    cookie name值
     * @param request http 请求
     * @return cookie
     */
    public static Cookie getCookieByName(String name, HttpServletRequest request) {
        Map<String, Cookie> cookieMap = getAllCookieMap(request);
        return cookieMap.getOrDefault(name, null);
    }

    /**
     * 根据 name 获取 cookie 的value信息
     *
     * @param name    cookie 名
     * @param request http 请求
     * @return cookie 的value
     */
    public static String getCookieValue(String name, HttpServletRequest request) {
        Cookie cookie = getCookieByName(name, request);
        String value = null;

        if (cookie != null) {
            try {
                value = URLDecoder.decode(cookie.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.warn("====== CookieUtil getCookieValue exception, cookie name = {}, message = {}", name, e.getLocalizedMessage(), e);
            }
        }
        return value;
    }

    /**
     * 设置 Cookie 做分布式 session 验证 过期时间为第二天的早上6点（失效需要重新登录）
     *
     * @param name cookie 名
     * @param value cookie 值
     */
    public static void setCookieForSession(String name, String value, HttpServletResponse response, boolean httpOnly, boolean secure) {
        long start = System.currentTimeMillis();

        // 获取第二天早上 6 点的时间
        Date targetDate = DateUtil.getNextDaySixClock();
        // expiry 过时时间单位是秒
        int expiry = (int) (targetDate.getTime() - start) / 1000;
        log.info("====== CookieUtil setCookieForSession name = {}, expiry time = {}", name, targetDate.getTime());
        setCookie(name, value, expiry, httpOnly, secure, response);

    }

    /**
     * 设置 cookie 信息
     *
     * @param name cookie 名
     * @param value cookie 值
     * @param expiry   过期时间
     * @param httpOnly 设置 httponly 为true，则js脚本无法读取cookie信息，有效防止XSS（跨站脚本攻击）攻击窃取用户的cookie信息，增加cookie安全性 [无法使用document.cookie 获取信息]
     * @param secure 设置 secure 为true，则该cookie只能用https协议发送给副武器，而http协议不发送（只能再https协议中使用该cookie）
     * @param response http响应
     */
    public static void setCookie(String name, String value, int expiry, boolean httpOnly, boolean secure, HttpServletResponse response) {
        if (StringUtils.isNotBlank(value)) {
            value = setEncode(value);
        }
        Cookie cookie = new Cookie(setEncode(name), value);
        cookie.setMaxAge(expiry);
        cookie.setPath("/");
        cookie.setSecure(secure);
        cookie.setHttpOnly(httpOnly);
        setCookie(cookie, response);
    }

    /**
     * 将 cookie 信息返回给客户端
     *
     * @param cookie cookie信息
     * @param response http响应
     */
    public static void setCookie(Cookie cookie, HttpServletResponse response) {
        response.addCookie(cookie);
    }

    /**
     * 设置编码格式，将字符串转换成<code> str </ code>
     * @param str 字符串
     * @return 捕获到异常则返回原字符串
     */
    public static String setEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.info("======= CookieUtil setEncode exception message = {}", e.getLocalizedMessage(), e);
        }
        return str;
    }

}
