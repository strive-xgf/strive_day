package com.xgf.mvc.controller;

import com.xgf.constant.reqrep.CommonDataResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xgf
 * @create 2022-04-22 23:46
 * @description
 **/


@Slf4j
@RestController
@RequestMapping(value = "/demo/demoController")
@Api(description = "mvc_demo", tags = "demo_demoController")
public class DemoController {

    @GetMapping(value = "/printHello")
    @ApiOperation(value = "打印hello", notes = "打印hello")
    @ApiImplicitParam(name = "name", value = "名称", paramType = "query", dataType = "String", required = true)
    CommonDataResponse<String> printHello(@RequestParam(value = "name") String name) {
        log.info("====== DemoController printHello in param = {}", name);
        return CommonDataResponse.ok("hello " + name);
    }

}
