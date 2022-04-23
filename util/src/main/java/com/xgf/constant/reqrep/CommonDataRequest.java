package com.xgf.constant.reqrep;

import com.sun.istack.internal.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

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
    @NotNull
    @ApiModelProperty(value = "请求参数", required = true)
    private T param;

}
