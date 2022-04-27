package com.xgf.constant.page;

import com.xgf.constant.reqrep.CommonDataRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * @author xgf
 * @create 2022-04-26 01:07
 * @description 通用分页请求
 **/


@Data
@NoArgsConstructor
@ApiModel(value = "通用分页请求", description = "分页请求对象")
public class CommonPageRequest<T> extends CommonDataRequest<T> {

    private static final long serialVersionUID = 2588557806571996258L;

    @Valid
    @ApiModelProperty(value = "分页信息 ")
    private CommonPage page;

    public CommonPageRequest(T param, CommonPage page) {
        super(param);
        this.page = page;
    }

    public static <T> CommonPageRequest<T> valueOf(T param, CommonPage page) {
        return new CommonPageRequest<T>(param, page);
    }



}
