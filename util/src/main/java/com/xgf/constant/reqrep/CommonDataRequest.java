package com.xgf.constant.reqrep;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author xgf
 * @create 2022-04-23 14:11
 * @description 请求参数通用
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonDataRequest<T> extends CommonRequest {

    private static final long serialVersionUID = 898105900898255956L;

    @Valid
    @NotNull(message = "CommonDataRequest 通用请求参数不能为空")
    @ApiModelProperty(value = "请求参数", required = true)
    private T param;

}
