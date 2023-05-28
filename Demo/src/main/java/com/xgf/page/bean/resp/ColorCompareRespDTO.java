package com.xgf.page.bean.resp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author strive_day
 * @create 2023-05-28 14:45
 * @description 颜色对照
 */

@Data
@Api("颜色实体类响应 DTO")
public class ColorCompareRespDTO {

    @ApiModelProperty("颜色英文名称")
    private String colorEnName;

    @ApiModelProperty("颜色中文名称")
    private String colorCnName;

    @ApiModelProperty("颜色值")
    private String colorValue;

    @ApiModelProperty("颜色rgb：R")
    private Integer colorR;

    @ApiModelProperty("颜色rgb：G")
    private Integer colorG;

    @ApiModelProperty("颜色rgb：B")
    private Integer colorB;


    public static ColorCompareRespDTO valueOf(String colorEnName, String colorCnName, String colorValue,
                                              Integer colorR, Integer colorG, Integer colorB) {

        ColorCompareRespDTO dto = new ColorCompareRespDTO();
        dto.setColorEnName(colorEnName);
        dto.setColorCnName(colorCnName);
        dto.setColorValue(colorValue);
        dto.setColorR(colorR);
        dto.setColorG(colorG);
        dto.setColorB(colorB);

        return dto;
    }



}
