package com.xgf.mvc.controller;

import com.xgf.common.LogUtil;
import com.xgf.constant.reqrep.CommonDataResponse;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.retry.guava.GuavaRetryAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xgf
 * @create 2022-07-08 15:04
 * @description
 **/

@Slf4j
@RestController
@RequestMapping(value = "/demo/guavaRetryController")
@Api(description = "demo_guavaRetryController", tags = "demo_guavaRetryController")
public class GuavaRetryDemoController {

    @PostMapping(value = "/testGuavaRetryBaseConfig")
    @ApiOperation(value = "测试GuavaRetry", notes = "测试GuavaRetry重试，基于配置")
    public CommonDataResponse<String> testGuavaRetryBaseConfig() {
        LogUtil.infoParam(">>>>>> execute testGuavaRetry", System.currentTimeMillis());

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        throw new NullPointerException();
        throw CustomExceptionEnum.DATA_PARSE_EXCEPTION.generateException();
//        return CommonDataResponse.ok("hello testGuavaRetry");
    }

    @GuavaRetryAnnotation(attemptNumber = 200, waitStrategySleepTime = 10)
//    @GuavaRetry(attemptNumber = 3, waitStrategySleepTime = 500, exceptionClass = IllegalArgumentException.class)
    @PostMapping(value = "/testGuavaRetryBaseAnnotation")
    @ApiOperation(value = "测试GuavaRetry重试基于注解", notes = "基本测试")
    public CommonDataResponse<Void> testGuavaRetryBaseAnnotation() {
        LogUtil.infoParam(">>>>>> execute testGuavaRetry2", System.currentTimeMillis());
        throw CustomExceptionEnum.DATA_CONVERT_EXCEPTION.generateException();
    }


}
