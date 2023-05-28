package com.xgf.page.service.impl;

import com.google.common.collect.Lists;
import com.xgf.constant.page.CommonPage;
import com.xgf.page.bean.req.ColorCompareReqDTO;
import com.xgf.page.bean.resp.ColorCompareRespDTO;
import com.xgf.page.data.ColorCompareTableDataStorage;
import com.xgf.page.service.IColorCompareTableService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author strive_day
 * @create 2023-05-28 20:34
 * @description
 */

@Service
public class ColorCompareTableServiceImpl implements IColorCompareTableService {


    @Override
    public List<ColorCompareRespDTO> search(ColorCompareReqDTO reqDTO, CommonPage page) {
        // 获取数据
        List<ColorCompareRespDTO> colorCompareRespDTOList =
                Optional.ofNullable(ColorCompareTableDataStorage.colorCompareRespDTOList)
                .orElseGet(Lists::newArrayList);

        // 过滤模拟搜索
        return colorCompareRespDTOList
                .stream()
                .filter(Objects::nonNull)
                .filter(p -> StringUtils.isBlank(reqDTO.getColorCnName()) || p.getColorCnName().contains(reqDTO.getColorCnName()))
                .filter(p -> StringUtils.isBlank(reqDTO.getColorEnName()) || p.getColorEnName().contains(reqDTO.getColorEnName()))
                .filter(p -> StringUtils.isBlank(reqDTO.getColorValue()) || p.getColorValue().contains(reqDTO.getColorValue()))
                .filter(p -> Objects.isNull(reqDTO.getColorR()) || reqDTO.getColorR().equals(p.getColorR()))
                .filter(p -> Objects.isNull(reqDTO.getColorG()) || reqDTO.getColorG().equals(p.getColorG()))
                .filter(p -> Objects.isNull(reqDTO.getColorB()) || reqDTO.getColorB().equals(p.getColorB()))
                .collect(Collectors.toList());

    }







}
