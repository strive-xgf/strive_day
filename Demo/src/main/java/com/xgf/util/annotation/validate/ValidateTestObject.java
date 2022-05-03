package com.xgf.util.annotation.validate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * @author xgf
 * @create 2022-04-28 23:11
 * @description 校验对象
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("测试 Valid 验证对象")
public class ValidateTestObject {

    @NotEmpty(message = "对象字段名不能为空，但可以为空串")
    @ApiModelProperty(value = "name值", example = "      ")
    private String name;


    @AssertTrue(message = "该值必须为true")
    @ApiModelProperty(value = "boolean值，必须为true", example = "true")
    private Boolean boolTrue;

    @AssertFalse(message = "该值必须为false")
    @ApiModelProperty(value = "boolean值，必须为false", example = "false")
    private Boolean boolFalse;

    @Digits(integer = 2, fraction = 3, message = "该值必须在2位整数和3位小数以内的数")
    @ApiModelProperty(value = "2位整数和3位小数以内的数, 注意小数点前置0会忽略，小数点后加0会报错，eg: 012.6780 实际就是12.6780", example = "12.678")
    private String digitsValue;
//    private Double digitsValue;
//    private BigDecimal digitsValue;

    @Min(value = 66, message = "该值最小为66")
    @Max(value = 88, message = "该值最大为88")
    @ApiModelProperty(value = "最小为66，最大为88的数", example = "66.66")
    private BigDecimal minMaxValue;


    @Min(value = 66, message = "该值最小为66")
    @Max(value = 88, message = "该值最大为88")
    @Digits(integer = 2, fraction = 3, message = "该值必须在2位整数和3位小数以内的数")
    @ApiModelProperty(value = "最小为66，最大为88的数，且最多2位整数和3位小数", example = "66.66")
    private BigDecimal minMaxDigitsValue;

    @DecimalMin(value = "66.66", message = "该值最小为66")
    @DecimalMax(value = "88.888", message = "该值最大为88")
    @Digits(integer = 2, fraction = 3, message = "该值必须在2位整数和3位小数以内的数")
    @ApiModelProperty(value = "最小为66.66，最大为88.888的数，且最多2位整数和3位小数", example = "66.66")
    private BigDecimal minMaxBigDecimalValue;

//    @Negative(message = "该值必须是负整数")
//    @ApiModelProperty(value = "该值必须是负整数", example = "-1")
//    private int negativeValue;
//
//    @NegativeOrZero(message = "该值必须是负整数或0")
//    @ApiModelProperty(value = "该值必须是负整数或0", example = "0")
//    private int negativeOrZeroValue;

    @Size(min = 6, max = 8, message = "该值最小长度6，最大长度8")
    @ApiModelProperty(value = "该值最小长度6，最大长度8", example = "123456")
    private String sizeValue;

    @Range(min = 2, max = 6, message = "该值范围在[2, 6] 之间")
    @ApiModelProperty(value = "该值最小长度6，最大长度8", example = "2")
    private String rangeValue;

    @Length(min = 3, max = 8, message = "该值长度在[3, 8] 之间")
    @ApiModelProperty(value = "该值长度在 [3, 8] 之间", example = "123")
    private String lengthValue;

    @Size(min = 1, max = 10, message = "该值数组最小长度1，最大长度10, [1, 10]")
    @ApiModelProperty(value = "该值数组最小长度1，最大长度10, [1, 10]")
    private List<String> sizeValueList;



    @Valid
    @NotNull(message = "内部类对象不能为空")
    @ApiModelProperty(value = "内部类对象")
    private ValidateTestInnerObject innerObject;

//    @Null
//    @NotNull(message = "该对象必须为空")
//    @ApiModelProperty(value = "@Null为空校验", hidden = true)
//    private Object objNull;

    @Data
    @ApiModel("ValidateTestObject内部类")
    public static class ValidateTestInnerObject {

        @NotBlank(message = "inner内部对象字段名不能为空及空串")
        @ApiModelProperty(value = "内部类name值", example = "innerName")
        private String innerName;

    }



}
