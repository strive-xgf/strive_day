package com.xgf.mvc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xgf
 * @create 2022-04-22 23:46
 * @description
 **/


@RestController
@RequestMapping(value = "/demo/demoController")
@Api(description = "mvc_demo", tags = "demo_demoController")
public class DemoController {

    @GetMapping(value = "/printHello")
    @ApiOperation(value = "打印hello", notes = "打印hello")
    @ApiImplicitParam(name = "name", value = "名称", paramType = "query", dataType = "String", required = true)
    String printHello(@RequestParam(value = "name") String name) {
        return "hello " + name;
    }

}
