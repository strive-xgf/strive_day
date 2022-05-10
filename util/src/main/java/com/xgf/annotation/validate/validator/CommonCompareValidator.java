package com.xgf.annotation.validate.validator;

import com.xgf.annotation.validate.CommonCompareAnnotation;
import com.xgf.check.validate.CommonCheckCompareData;
import com.xgf.constant.DataEntry;
import com.xgf.exception.CustomException;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author xgf
 * @create 2022-05-08 00:56
 * @description 通用比较 接口验证器
 **/
public class CommonCompareValidator implements ConstraintValidator<CommonCompareAnnotation, Object> {

    /**
     * 校验比较数据类
     */
    CommonCheckCompareData commonCheckCompareData;

    /**
     * 校验列描述
     */
    String checkColumnFieldDesc;

    /**
     * 条件列描述
     */
    String conditionColumnFieldDesc;


    @Override
    public void initialize(CommonCompareAnnotation constraintAnnotation) {
        commonCheckCompareData = convertToCommonCheckCompareData(constraintAnnotation);
        // 获取字段描述，获取成功使用，否则使用字段属性名作为描述
        checkColumnFieldDesc = StringUtils.isNotBlank(constraintAnnotation.checkColumnFieldDesc()) ? constraintAnnotation.checkColumnFieldDesc() : constraintAnnotation.checkColumn();
        conditionColumnFieldDesc = StringUtils.isNotBlank(constraintAnnotation.conditionColumnFieldDesc()) ? constraintAnnotation.conditionColumnFieldDesc() : constraintAnnotation.conditionColumn();

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        commonCheckCompareData.setCheckObject(value);

        try {
            commonCheckCompareData.checkData();
        } catch (CustomException e) {
            // 禁用默认提示信息
            context.disableDefaultConstraintViolation();
            // 使用自定义提示信息
            context.buildConstraintViolationWithTemplate(
                    checkColumnFieldDesc + "必须" + commonCheckCompareData.getOperatorEnum().getDesc() + conditionColumnFieldDesc)
                    .addConstraintViolation();
            return false;
        }

        return true;


    }


    private CommonCheckCompareData convertToCommonCheckCompareData(CommonCompareAnnotation annotation) {
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.setColumnEntry(DataEntry.valueOf(annotation.checkColumn(), annotation.conditionColumn()));
        data.setDealSpecialValueFlag(annotation.dealSpecialValueFlag());
        data.setOperatorEnum(annotation.operator());
        return data;
    }

}