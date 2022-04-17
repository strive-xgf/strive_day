package com.xgf.designpattern.structure.proxy.dynamicproxy.cglib.custom;

import com.xgf.annotation.aspcet.common.BeforeAfterAbstractAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2022-04-17 10:50
 * @description save开头方法拦截增强
 **/

@Slf4j
@Component("savePrefixAspect")
public class SavePrefixAspect extends BeforeAfterAbstractAspect {

    @Override
    public void before() {
        log.info(">> ====== SavePrefixAspect before, save pre check param");
    }

    @Override
    public void after() {
        log.info(">> ====== SavePrefixAspect after, save suf, currentTime = {}", System.currentTimeMillis());

    }
}
