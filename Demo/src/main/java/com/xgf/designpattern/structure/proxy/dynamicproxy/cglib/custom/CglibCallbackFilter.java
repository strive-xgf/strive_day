package com.xgf.designpattern.structure.proxy.dynamicproxy.cglib.custom;

import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-04-17 11:30
 * @description cglib 回调过滤器（实现不同的方法使用不同的回调方法）
 **/

@Component("cglibCallbackFilter")
public class CglibCallbackFilter implements CallbackFilter {

    @Resource(name = "customAspectSelector")
    private CustomAspectSelector customAspectSelector;

    @Override
    public int accept(Method method) {
        if (Objects.nonNull(customAspectSelector.selectorAspect(method.getName()))) {
            // Callback callbacks[1]
            return 1;
        }

        // 默认返回0，Callback callbacks[0]（回调数组中的第一个）
        return 0;
    }
}
