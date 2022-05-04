package com.xgf.annotation.validate.validator;

import com.xgf.annotation.validate.CheckEnumList;
import com.xgf.constant.EnumBase;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xgf
 * @create 2022-05-04 10:30
 * @description 校验自定义枚举List的code
 **/

public class CheckEnumListValidator implements ConstraintValidator<CheckEnumList, List<String>> {

    /**
     * 校验枚举 enumClass 的所有code集合
     */
    private List<String> enumCodeList;

    /**
     * 错误提示信息
     */
    private String errorMessage;

    /**
     * 校验枚举类简称
     */
    private String enumSimpleName;

    @Override
    public void initialize(CheckEnumList constraintAnnotation) {
        Class<? extends EnumBase> classValue = constraintAnnotation.enumClass();
        enumSimpleName = classValue.getSimpleName();

        EnumBase[] enumConstants = classValue.getEnumConstants();
        enumCodeList = new ArrayList<>();
        for (EnumBase enumConstant : enumConstants) {
            enumCodeList.add(enumConstant.getCode());
        }
        errorMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if(CollectionUtils.isNotEmpty(value)){
            for (int i = 0; i < value.size(); i++) {
                String item = value.get(i);
                if (StringUtils.isNotEmpty(item) && !enumCodeList.contains(item)) {
                    // 禁用默认提示信息
                    context.disableDefaultConstraintViolation();
                    // 使用自定义提示信息
                    context.buildConstraintViolationWithTemplate(
                            MessageFormat.format(errorMessage, i + 1, item, enumSimpleName))
                            .addConstraintViolation();
                    return false;
                }
            }
        }
        return true;
    }

}
