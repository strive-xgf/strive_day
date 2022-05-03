package com.xgf.annotation.validate.validator;

import com.google.common.collect.Lists;
import com.xgf.annotation.validate.CheckEnum;
import com.xgf.constant.EnumBase;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xgf
 * @create 2022-04-28 23:08
 * @description 实现 javax.validation.ConstraintValidator 接口的验证器【自定义枚举类型校验】
 * ConstraintValidator 两个泛型参数? 分别为：
 * 1. 自定义注解类
 * 2. 需要验证字段的类型
 **/

public class CheckEnumValidator implements ConstraintValidator<CheckEnum, String> {

    /**
     * 校验枚举 enumClass 的所有code集合
     */
    private List<String> enumCodeList;

    /**
     * 校验枚举类简称
     */
    private String enumSimpleName;

    @Override
    public void initialize(CheckEnum constraintAnnotation) {
        // 初始化，将枚举常量值全部初始到数组中去，每次执行先取值
        Class<? extends EnumBase> classValue = constraintAnnotation.enumClass();
        enumSimpleName = classValue.getSimpleName();

        // 获取枚举常量数组（枚举元素）
        EnumBase[] enumConstants = classValue.getEnumConstants();
        enumCodeList = new ArrayList<>();
        for (EnumBase enumConstant : enumConstants) {
            enumCodeList.add(enumConstant.getCode());
        }
    }

    /**
     * 校验逻辑
     * @param value ConstraintValidator的第二个参数【校验对象值】
     * @return true：校验通过
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isNotEmpty(value) && !enumCodeList.contains(value)) {
            // 获取默认提示信息 即枚举的 message 内容（校验枚举code错误，code = {0}, checkEnum = {1}）
            String defaultConstraintMessageTemplate = context.getDefaultConstraintMessageTemplate();
            // 禁用默认提示信息
            context.disableDefaultConstraintViolation();
            // 使用自定义提示信息
            context.buildConstraintViolationWithTemplate(MessageFormat.format(defaultConstraintMessageTemplate, value, enumSimpleName)).addConstraintViolation();
            return false;
        }

        return true;
    }
}
