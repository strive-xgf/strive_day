package com.xgf.util.annotation.validate;

import com.xgf.annotation.validate.CheckEnum;
import com.xgf.annotation.validate.CheckEnumList;
import com.xgf.annotation.validate.CheckNumberValueAnnotation;
import com.xgf.annotation.validate.CommonCompareAnnotation;
import com.xgf.check.CompareOperatorEnum;
import com.xgf.exception.CustomExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
@CommonCompareAnnotation(checkColumn = "compare1", conditionColumn = "compare2", operator = CompareOperatorEnum.EQ,
//        checkColumnFieldDesc = "校验列描述compare1", conditionColumnFieldDesc = "条件列描述compare2",
        dealSpecialValueFlag = true)
public class ValidateCustomTestObject {


    @ApiModelProperty(value = "compare 比较值1", example = "9999.999")
    private BigDecimal compare1;

    @ApiModelProperty(value = "compare 比较值2", example = "9999.999")
    private String compare2;


    @CheckEnum(enumClass = CustomExceptionEnum.class)
    @ApiModelProperty(value = "异常code值", example = "paramValueCanNotNullException")
    private String exceptionCode;

    @CheckEnumList(enumClass = CustomExceptionEnum.class)
    @ApiModelProperty(value = "异常code值集合", allowableValues = "paramValueCanNotNullException")
    private List<String> exceptionCodeList;

    @CheckNumberValueAnnotation(min = "0", max = "99", scale = 3, dealSpecialValueFlag = true, fieldDescription = "int类型")
    @ApiModelProperty(value = "[0 - 99, 3位小数]", example = "1")
    private Integer ainteger;

    @CheckNumberValueAnnotation(min = "0", max = "99.998", scale = 3, dealSpecialValueFlag = true, fieldDescription = "float类型")
    @ApiModelProperty(value = "[0 - 99.998], 3位小数", example = "1")
    private Float aFloat;

    @CheckNumberValueAnnotation(min = "0", max = "99.998", scale = 3, dealSpecialValueFlag = true, fieldDescription = "我是double类型")
    @ApiModelProperty(value = "[0 - 99.998], 3位小数", example = "1")
    private Double aDouble;

    @CheckNumberValueAnnotation(min = "0", max = "99.998", scale = 3, dealSpecialValueFlag = true)
    @ApiModelProperty(value = "[0 - 99.998], 3位小数", example = "1")
    private Long aLong;

    @CheckNumberValueAnnotation(min = "0", max = "99.998", scale = 5, dealSpecialValueFlag = true)
    @ApiModelProperty(value = "[0 - 99.998], 3位小数", example = "1")
    private BigDecimal bigDecimal;

    @CheckNumberValueAnnotation(message = "自定义消息校验不通过不符合条件嘿嘿", min = "0", max = "99", scale = 3, dealSpecialValueFlag = true)
    @ApiModelProperty(value = "[0 - 99], 3位小数内，自定义消息报错模板", example = "1")
    private Integer customMessage;



}
