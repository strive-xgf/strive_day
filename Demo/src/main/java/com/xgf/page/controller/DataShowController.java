package com.xgf.page.controller;

import com.xgf.constant.page.CommonPageRequest;
import com.xgf.constant.reqrep.CommonDataResponse;
import com.xgf.page.bean.req.ColorCompareReqDTO;
import com.xgf.page.bean.resp.ColorCompareRespDTO;
import com.xgf.page.service.IColorCompareTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author strive_day
 * @create 2023-05-28 23:15
 * @description 数据展示 controller
 */
@RestController
@RequestMapping("/data")
@Slf4j
@Api("数据展示 controller")
public class DataShowController {

    @Autowired
    private IColorCompareTableService colorCompareTableService;

    @PostMapping(value = "/searchColorCompare")
    @ApiOperation(value = "搜索颜色对照表")
    public CommonDataResponse<List<ColorCompareRespDTO>> searchColorCompareTable(@RequestBody @Valid CommonPageRequest<ColorCompareReqDTO> reqDTO) {
        try {
            return CommonDataResponse.ok(colorCompareTableService.search(reqDTO.getParam(), reqDTO.getPage()));
        } catch (Exception e) {
            log.warn("====== searchColorCompareTable exception ", e);
            return CommonDataResponse.fail(e.getLocalizedMessage());
        }
    }


}
