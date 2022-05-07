package com.xgf.annotation.validate.validator;

import com.xgf.annotation.validate.CheckNumberValueAnnotation;
import com.xgf.check.validate.CommonCheckNumberData;
import com.xgf.convert.validate.CommonCheckDataConvert;
import com.xgf.exception.CustomException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

/**
 * @author xgf
 * @create 2022-05-04 17:47
 * @description
 **/

@Slf4j
public class CheckNumberValueValidator implements ConstraintValidator<CheckNumberValueAnnotation, Object> {

    /**
     * 通用 数值 类型校验类
     */
    private CommonCheckNumberData commonCheckNumberData;

    /**
     * message 模板
     */
    private String errorMessageTemplate;


    @Override
    public void initialize(CheckNumberValueAnnotation constraintAnnotation) {

        // 转换为 通用 数值 类型校验
        commonCheckNumberData = CommonCheckDataConvert.convert(constraintAnnotation);
        // 获取默认消息模板
        errorMessageTemplate = constraintAnnotation.message();

    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        // 设置字段值
        commonCheckNumberData.setFiledValue(value);

        try {
            // 通用数值校验 校验执行
            commonCheckNumberData.checkDate();
        } catch (CustomException e) {
            // 禁用默认提示信息
            context.disableDefaultConstraintViolation();
            // 使用自定义提示信息
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format(errorMessageTemplate, e.getMessage(), String.valueOf(value)))
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
