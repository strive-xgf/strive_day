package com.xgf.mvc.controller;

import com.xgf.constant.reqrep.CommonDataRequest;
import com.xgf.constant.reqrep.CommonResponse;
import com.xgf.mvc.service.ValidateService;
import com.xgf.util.annotation.validate.ValidateTestObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author xgf
 * @create 2022-05-04 00:15
 * @description
 **/

@Slf4j
@RestController
@RequestMapping(value = "/demo/validateController")
@Api(description = "mvc_validate", tags = "demo_validateController")
public class ValidateController {

    @Autowired
    private ValidateService validateService;

    @PostMapping(value = "/testValidate")
    @ApiOperation(value = "测试预制验证器", notes = "测试预制验证器testValidate")
    CommonResponse testValidate(@RequestBody @Valid CommonDataRequest<ValidateTestObject> req){
        validateService.testValidate(req.getParam());
        return CommonResponse.success();
    }


}
