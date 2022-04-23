package com.xgf.constant.reqrep;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author xgf
 * @create 2022-04-23 01:44
 * @description 通用响应对象（页面通过响应处理不同的样式）
 **/

@Data
@ApiModel("响应通用返回对象")
public class CommonResponse implements Serializable {

    private static final long serialVersionUID = -5686265032159081738L;

    @ApiModelProperty(value = "响应码code，默认1成功", example = "1")
    private String responseCode;

    @ApiModelProperty("响应消息内容")
    private String responseMessage;

    @ApiModelProperty("响应类型，默认 EXCEPTION")
    private ResponseTypeEnum responseType = ResponseTypeEnum.EXCEPTION;

    /**
     * 默认成功 SUCCESS
     */
    public CommonResponse() {
        this.responseCode = CommonResponseCodeEnum.SUCCESS.getCode();
        this.responseMessage = CommonResponseCodeEnum.SUCCESS.getValue();
        this.responseType = CommonResponseCodeEnum.SUCCESS.getResponseTypeEnum();
    }


    public static CommonResponse success() {
        return custom(CommonResponseCodeEnum.SUCCESS);
    }

    public static CommonResponse exception() {
        return custom(CommonResponseCodeEnum.SERVICE_EXCEPTION);
    }

    /**
     * 自定义
     */
    public static CommonResponse custom(CommonResponseCodeEnum respEnum) {
        return custom(respEnum.getCode(), respEnum.getValue(), respEnum.getResponseTypeEnum());
    }

    public static CommonResponse custom(String responseCode, String responseMessage) {
        return custom(responseCode, responseMessage, ResponseTypeEnum.EXCEPTION);
    }

    public static CommonResponse custom(String responseCode, String responseMessage, ResponseTypeEnum responseType) {
        CommonResponse resp = new CommonDataResponse<>();
        resp.responseCode = responseCode;
        resp.responseMessage = responseMessage;
        resp.responseType = responseType;
        return resp;
    }

    /**
     * 响应类型（页面根据类型处理通用样式）
     */
    enum ResponseTypeEnum {
        INFO,
        WARN,
        EXCEPTION,
        ERROR
    }

    @Getter
    @AllArgsConstructor
    enum CommonResponseCodeEnum {
        SUCCESS("1", "success", ResponseTypeEnum.INFO),
        SERVICE_EXCEPTION("-1", "service_exception", ResponseTypeEnum.EXCEPTION),
        CUSTOM_EXCEPTION("-100", "custom_exception", ResponseTypeEnum.EXCEPTION),
        CUSTOM_MESSAGE_EXCEPTION("-101", "custom_message_exception", ResponseTypeEnum.EXCEPTION),
        ;

        private String code;
        private String value;
        private ResponseTypeEnum responseTypeEnum;

    }

}
