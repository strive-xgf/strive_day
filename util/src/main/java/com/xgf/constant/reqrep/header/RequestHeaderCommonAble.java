package com.xgf.constant.reqrep.header;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xgf
 * @create 2022-08-15 13:13
 * @description 通用请求头处理
 **/

public class RequestHeaderCommonAble {

    public static HttpServletRequest getRequest() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return null;
        }
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

}
