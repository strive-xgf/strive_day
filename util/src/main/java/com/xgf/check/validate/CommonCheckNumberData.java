package com.xgf.check.validate;

import com.xgf.constant.CommonNullConstantUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.exception.CustomExceptionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-05-05 12:07
 * @description 通用校验 数值 类型数据
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonCheckNumberData extends AbstractCommonCheckData {

    /**
     * 最小值，默认为空不校验
     */
    private String min;

    /**
     * 最大值，默认为空不校验
     */
    private String max;

    /**
     * 保留小数位
     */
    private Integer scale;

    /**
     * 校验当前对象数据合法性
     */
    @Override
    public void checkDate() {
        checkDate(this);
    }

    /**
     * 校验入参数据合法性
     * @param dto 参数
     */
    public static void checkDate(CommonCheckNumberData dto) {

        String errorInfo = doCheckData(dto);

        if (StringUtils.isBlank(errorInfo)) {
            return;
        }

        // 错误原因默认前置加: 字段名
        if (StringUtils.isNotBlank(dto.getFieldDescription())) {
            errorInfo = dto.getFieldDescription() + errorInfo;
        }

        // 抛出异常
        throw CustomExceptionEnum.CUSTOM_CHECK_DATA_ILLEGAL_EXCEPTION.generateCustomMessageException(errorInfo);

    }

    /**
     * 执行校验数据，不满足条件返回不满足原因
     * @param dto 入参
     * @return 合法返回true, 不合法返回不满足原因
     */
    public static String doCheckData(CommonCheckNumberData dto) {
        Object value = dto.getFiledValue();

        // 空值校验通过
        if (value == null) {
            return null;
        }

        // 字段不为空，转为 BigDecimal 继续校验，转换失败抛出异常
        BigDecimal valueBig = convertBigDecimal(value, dto.getFieldDescription());

        // 特殊值不校验
        if (BooleanUtils.isTrue(dto.getDealSpecialValueFlag()) && CommonNullConstantUtil.checkSpecialValue(value)) {
            return null;
        }

        // 数值类型转换为 BigDecimal 比较小数位
        if (Objects.nonNull(dto.getScale())) {
            // 限制小数位 >=0 则限制位数
            if (dto.getScale() >= 0 && valueBig.scale() > dto.getScale()) {
                return "小数位不能大于 " + dto.getScale() + " 位, 当前 " + valueBig.scale() + " 位";
            }
        }

        // 校验大于等于最小值
        if (StringUtils.isNotBlank(dto.getMin())) {
            if (valueBig.compareTo(convertBigDecimal(dto.getMin(), "最小值")) < 0) {
                return "不能小于 " + dto.getMin();
            }
        }

        // 校验小于等于最大值
        if (StringUtils.isNotBlank(dto.getMax())) {
            // 转换异常自动报错 java.lang.NumberFormatException
            if (valueBig.compareTo(convertBigDecimal(dto.getMax(), "最大值")) > 0) {
                return "不能大于 " + dto.getMax();
            }
        }

        return null;
    }

    /**
     * 将数据转换为 BigDecimal ，如果非数值类型，转换报错，抛出异常
     * @param value 数据只
     * @param fieldDesc 转换的字段描述，用于转换失败抛出异常
     * @return 转换失败抛出异常信息
     */
    private static BigDecimal convertBigDecimal(Object value, String fieldDesc) {
        // 数值类型转换为 BigDecimal 比较小数位
        try {
            // 转换异常自动报错 java.lang.NumberFormatException
            return new BigDecimal(String.valueOf(value));
        } catch (Exception e) {
            // 兼容描述字段为 null
            fieldDesc = StringUtils.isBlank(fieldDesc) ? StringConstantUtil.EMPTY : fieldDesc;

            throw CustomExceptionEnum.DATA_PARSE_EXCEPTION.generateCustomMessageException(
                    fieldDesc + "赋值非数值类型: " + value);
        }
    }



}