package com.xgf.designpattern.structure.proxy.dynamicproxy.cglib.custom;

import com.xgf.annotation.aspcet.common.BeforeAfterAbstractAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2022-04-17 10:52
 * @description delete开头方法拦截增强
 **/

@Slf4j
@Component("deletePrefixAspect")
public class DeletePrefixAspect extends BeforeAfterAbstractAspect {

    @Override
    public void before() {
        log.info(">> ====== DeletePrefixAspect before, delete pre check param, query param");
    }

    @Override
    public void after() {
        log.info(">> ====== DeletePrefixAspect after, delete suf, currentTime = {}", System.currentTimeMillis());

    }
}
