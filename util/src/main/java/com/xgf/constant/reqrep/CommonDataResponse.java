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

    public static <V> CommonDataResponse<V> fail() {
        return fail(CommonResponse.CommonResponseCodeEnum.SERVICE_EXCEPTION);
    }

    public static <V> CommonDataResponse<V> fail(CommonResponseCodeEnum respEnum) {
        return custom(CommonResponse.CommonResponseCodeEnum.SERVICE_EXCEPTION, null);
    }

    public static <V> CommonDataResponse<V> fail(String responseMessage) {
        return custom(CommonResponse.CommonResponseCodeEnum.CUSTOM_MESSAGE_EXCEPTION.getCode(), responseMessage, CommonResponse.CommonResponseCodeEnum.SERVICE_EXCEPTION.getResponseTypeEnum(), null);
    }

    public static <V> CommonDataResponse<V> ok() {
        return ok(null);
    }

    public static <V> CommonDataResponse<V> ok(V data) {
        return custom(CommonResponse.CommonResponseCodeEnum.SUCCESS, data);
    }

    public static <V> CommonDataResponse<V> ok(String responseMessage, V data) {
        return custom(CommonResponse.CommonResponseCodeEnum.CUSTOM_MESSAGE_EXCEPTION.getCode(), responseMessage, CommonResponse.CommonResponseCodeEnum.SUCCESS.getResponseTypeEnum(), data);
    }

    /**
     * 自定义
     */
    public static <V> CommonDataResponse<V> custom(CommonResponseCodeEnum respEnum, V data) {
        return custom(respEnum.getCode(), respEnum.getValue(), respEnum.getResponseTypeEnum(), data);
    }

    public static <V> CommonDataResponse<V> custom(String responseCode, String responseMessage, ResponseTypeEnum responseType, V data) {
        CommonDataResponse<V> result = new CommonDataResponse<V>();
        result.setResponseCode(responseCode);
        result.setResponseMessage(responseMessage);
        result.setResponseType(responseType);
        result.setData(data);
        return result;
    }
    
}
