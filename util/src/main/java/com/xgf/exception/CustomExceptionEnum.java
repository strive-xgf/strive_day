package com.xgf.exception;

import com.xgf.constant.EnumBase;
import com.xgf.constant.StringConstantUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xgf
 * @create 2021-11-21 17:23
 * @description 自定义异常枚举
 **/

@Getter
@AllArgsConstructor
public enum CustomExceptionEnum implements EnumBase {
    PARAM_VALUE_CAN_NOT_NULL_EXCEPTION("paramValueCanNotNullException", "参数不能为空异常"),
    PARAM_TYPE_ILLEGAL_EXCEPTION("paramTypeIllegalException", "参数类型不合法异常"),
    ENUM_TYPE_ILLEGAL_EXCEPTION("enumTypeIllegalException", "枚举类型不合法"),
    DATA_PARSE_EXCEPTION("dataParseException", "数据解析异常"),
    DATA_CONVERT_EXCEPTION("dataConvertException", "数据转换异常"),
    REFLECT_EXCEPTION("reflectException", "反射处理异常"),
    CUSTOM_PARAM_VALIDATOR_NULL_EXCEPTION("customParamValidatorNullException", "参数校验字段不存在"),
    FILE_CREATE_EXCEPTION("fileCreateException", "文件创建异常"),
    FILE_DOWNLOAD_EXCEPTION("fileDownloadException", "文件下载异常"),
    CUSTOM_CHECK_DATA_ILLEGAL_EXCEPTION("customCheckDataIllegalException", "自定义校验数据非法异常"),
    ;

    private String code;
    private String value;

    public static CustomExceptionEnum getByCode(String code) {

        if(StringUtils.isBlank(code)){
            return null;
        }

        for(CustomExceptionEnum var : values()){
            if (var.getCode().equals(code)) {
                return var;
            }
        }
        return null;
    }


    public static CustomExceptionEnum getByValue(String value) {

        if(StringUtils.isBlank(value)){
            return null;
        }

        for(CustomExceptionEnum var : values()){
            if (var.getValue().equals(value)) {
                return var;
            }
        }
        return null;
    }

    /**
     * 根据枚举生成自定义异常
     * @return 返回异常
     */
    public CustomException generateException() {
        return new CustomException(this.getCode(), this.getValue());
    }

    /**
     * 根据枚举code和入参生成异常
     * @param arg 入参
     * @return 枚举code和入参message的异常
     */
    public CustomException generateException(String arg) {
        return new CustomException(this.getCode(), StringUtils.join(this.getValue(), arg));
    }

    /**
     * 根据code，和多个入参（按逗号隔开）生成异常
     * @param args 参数
     * @return 异常信息
     */
    public CustomException generateException(String ...args) {
        return new CustomException(this.getCode(), StringUtils.join(args, StringConstantUtil.COMMA));
    }

    /**
     * 生成自定义消息内容异常，不用枚举的value
     * @param args 消息内容
     * @return 异常信息（message = args）
     */
    public CustomException generateCustomMessageException(String ...args) {
        return new CustomException(this.getCode(), StringUtils.join(args, StringConstantUtil.COMMA));
    }
}
