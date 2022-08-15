package com.xgf.mvc.controller;

import com.xgf.bean.User;
import com.xgf.bean.WorkInfo;
import com.xgf.common.LogUtil;
import com.xgf.constant.page.CommonPageDataResponse;
import com.xgf.constant.page.CommonPageRequest;
import com.xgf.constant.reqrep.CommonDataRequest;
import com.xgf.constant.reqrep.CommonDataResponse;
import com.xgf.constant.reqrep.header.RequestDeviceUtil;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.java8.BooleanFunctionUtil;
import com.xgf.java8.BranchHandleUtil;
import com.xgf.mvc.bean.req.SearchUserReqDTO;
import com.xgf.mvc.service.UserService;
import com.xgf.randomstr.RandomStrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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



    @PostMapping(value = "/testSwaggerRequestHeader")
    @ApiOperation(value = "测试 swagger 请求头", notes = "测试 swagger 请求头, 入参传 header 的 key")
    CommonDataResponse<Map<String, String>> testSwaggerRequestHeader(@RequestBody @Valid CommonDataRequest<List<String>> req) {

        if (Objects.isNull(RequestContextHolder.getRequestAttributes())
                || Objects.isNull(req)
                || CollectionUtils.isEmpty(req.getParam())) {
            return CommonDataResponse.ok();
        }

        Map<String, String> resultMap = new LinkedHashMap<>();

//        LogUtil.info("====== token = {}", JsonUtil.toJsonString(httpServletRequest.getHeader(StringConstantUtil.TOKEN_HERDER_KEY)));
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();


        BranchHandleUtil.isTrueOrFalse(RequestDeviceUtil.isMobile())
                .handle(() -> System.out.println("手机端访问 : " + RequestDeviceUtil.getRequestDevice()),
                        () -> System.out.println("pc端访问：" + RequestDeviceUtil.getRequestDevice()));

        for (String headerKey : req.getParam()) {
            resultMap.put(headerKey, httpServletRequest.getHeader(headerKey));
        }

        return CommonDataResponse.ok(resultMap);
    }

}
