package com.xgf.constant.reqrep;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xgf
 * @create 2022-04-23 02:10
 * @description 通用结果响应（不抛出异常，将执行结果包起来返回）
 **/

@Data
@ApiModel("通用结果响应")
public class CommonDataResponse<T> extends CommonResponse {

    private static final long serialVersionUID = 5533357882925323386L;

    @ApiModelProperty("方法结果数据")
    private T data;

    public static <T> CommonDataResponse<T> fail() {
        return fail(CommonResponse.CommonResponseCodeEnum.SERVICE_EXCEPTION);
    }

    public static <T> CommonDataResponse<T> fail(CommonResponseCodeEnum respEnum) {
        return custom(respEnum, null);
    }

    public static <T> CommonDataResponse<T> fail(String responseMessage) {
        return custom(CommonResponse.CommonResponseCodeEnum.CUSTOM_MESSAGE_EXCEPTION.getCode(), responseMessage, CommonResponse.CommonResponseCodeEnum.SERVICE_EXCEPTION.getResponseTypeEnum(), null);
    }

    public static <T> CommonDataResponse<T> ok() {
        return ok(null);
    }

    public static <T> CommonDataResponse<T> ok(T data) {
        return custom(CommonResponse.CommonResponseCodeEnum.SUCCESS, data);
    }

    public static <T> CommonDataResponse<T> ok(String responseMessage, T data) {
        return custom(CommonResponse.CommonResponseCodeEnum.CUSTOM_MESSAGE_EXCEPTION.getCode(), responseMessage, CommonResponse.CommonResponseCodeEnum.SUCCESS.getResponseTypeEnum(), data);
    }

    /**
     * 自定义
     */
    public static <T> CommonDataResponse<T> custom(CommonResponseCodeEnum respEnum, T data) {
        return custom(respEnum.getCode(), respEnum.getValue(), respEnum.getResponseTypeEnum(), data);
    }

    public static <T> CommonDataResponse<T> custom(String responseCode, String responseMessage, ResponseTypeEnum responseType, T data) {
        CommonDataResponse<T> result = new CommonDataResponse<T>();
        result.setResponseCode(responseCode);
        result.setResponseMessage(responseMessage);
        result.setResponseType(responseType);
        result.setData(data);
        return result;
    }
    
}
