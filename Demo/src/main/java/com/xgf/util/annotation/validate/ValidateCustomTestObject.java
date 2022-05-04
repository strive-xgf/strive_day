package com.xgf.util.annotation.validate;

import com.xgf.annotation.validate.CheckEnum;
import com.xgf.annotation.validate.CheckEnumList;
import com.xgf.exception.CustomExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xgf
 * @create 2022-05-04 00:27
 * @description 自定义验证器测试类
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("自定义验证器测试类")
public class ValidateCustomTestObject {

    @CheckEnum(enumClass = CustomExceptionEnum.class)
    @ApiModelProperty(value = "异常code值", example = "paramValueCanNotNullException")
    private String exceptionCode;

    @CheckEnumList(enumClass = CustomExceptionEnum.class)
    @ApiModelProperty(value = "异常code值集合", allowableValues = "paramValueCanNotNullException")
    private List<String> exceptionCodeList;

}
