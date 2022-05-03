package com.xgf.mvc.service.impl;

import com.alibaba.fastjson.JSON;
import com.xgf.mvc.service.ValidateService;
import com.xgf.util.annotation.validate.ValidateCustomTestObject;
import com.xgf.util.annotation.validate.ValidateTestObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xgf
 * @create 2022-04-30 17:46
 * @description
 **/

@Slf4j
@Service("validateService")
public class ValidateServiceImpl implements ValidateService {

    @Override
    public void testValidate(ValidateTestObject param) {
        log.info("====== ValidateService testValidate = 【{}】", JSON.toJSONString(param));
    }

    @Override
    public void testCustomValidate(ValidateCustomTestObject param) {
        log.info("====== ValidateService testCustomValidate = 【{}】", JSON.toJSONString(param));
    }
}
