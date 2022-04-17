package com.xgf.designpattern.structure.proxy.dynamicproxy.cglib.custom;

import com.xgf.annotation.aspcet.common.CustomAspect;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xgf
 * @create 2022-04-17 11:00
 * @description 自定义拦截选择器
 **/

@Component("customAspectSelector")
public class CustomAspectSelector {

    /**
     * save 前缀
     */
    public static final String SAVE_PRE = "save";

    /**
     * delete 前缀
     */
    public static final String DELETE_PRE = "delete";


    @Resource(name = "savePrefixAspect")
    private CustomAspect savePrefixAspect;

    @Resource(name = "deletePrefixAspect")
    private CustomAspect deletePrefixAspect;

    public CustomAspect selectorAspect(String methodName) {
        if (BooleanUtils.isTrue(methodName.startsWith(SAVE_PRE))){
            return savePrefixAspect;
        } else if (BooleanUtils.isTrue(methodName.startsWith(DELETE_PRE))) {
            return deletePrefixAspect;
        }
        return null;
    }

}
