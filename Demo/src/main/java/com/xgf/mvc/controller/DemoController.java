package com.xgf.mvc.controller;

import com.xgf.bean.User;
import com.xgf.bean.WorkInfo;
import com.xgf.constant.page.CommonPageDataResponse;
import com.xgf.constant.page.CommonPageRequest;
import com.xgf.constant.reqrep.CommonDataRequest;
import com.xgf.constant.reqrep.CommonDataResponse;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.mvc.bean.req.SearchUserReqDTO;
import com.xgf.mvc.service.UserService;
import com.xgf.randomstr.RandomStrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @Autowired
    private UserService userService;

    @GetMapping(value = "/printHello")
    @ApiOperation(value = "打印hello", notes = "打印hello")
    @ApiImplicitParam(name = "name", value = "名称", paramType = "query", dataType = "String", required = true)
    CommonDataResponse<String> printHello(@RequestParam(value = "name") String name) {
        log.info("====== DemoController printHello in param = {}", name);
        return CommonDataResponse.ok("hello " + name);
    }

    @PostMapping(value = "/printUser")
    @ApiOperation(value = "输出 user", notes = "输出 user")
    CommonDataResponse<User> printUser(@RequestBody @Valid CommonDataRequest<User> req) {
        log.info("====== DemoController printUser in param = {}", req);
        User user = req.getParam();
        user.setWorkInfo(WorkInfo.builder().workContent("generate workContent").workUuid(RandomStrUtil.createRandomStr(32)).build());
        return CommonDataResponse.ok(user);
    }

    @PostMapping(value = "/testException")
    @ApiOperation(value = "测试异常", notes = "测试异常")
    CommonDataResponse<Void> testException() {
//        throw new NullPointerException();
        throw CustomExceptionEnum.DATA_PARSE_EXCEPTION.generateException();
    }


    @PostMapping(value = "/testPageCommon")
    @ApiOperation(value = "测试分页", notes = "分页搜索User信息")
    CommonPageDataResponse<User> testPageCommon(@RequestBody @Valid CommonPageRequest<SearchUserReqDTO> req) {
//        return CommonPageDataResponse.fail();
//        return CommonPageDataResponse.fail(CommonResponse.CommonResponseCodeEnum.PARAM_VALID_EXCEPTION);
//        return CommonPageDataResponse.fail("testPageCommon 执行失败了，喔喔喔喔喔");

        return CommonPageDataResponse.ok(userService.searchUserByPage(req.getParam().getUserCondition(), req.getPage()));
    }

}
