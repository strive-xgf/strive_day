package com.xgf.annotation.validate;

import com.xgf.annotation.validate.validator.CheckEnumValidator;
import com.xgf.constant.EnumBase;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author xgf
 * @create 2022-04-28 23:05
 * @description 校验枚举取值code【String类型】是否符合指定枚举类型，需要继承通用枚举 com.xgf.constant.EnumBase 才能校验
 * 注意：为空校验通过
 **/

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckEnumValidator.class)
@Documented
public @interface CheckEnum {

    String message() default "「校验枚举code错误, code = {0}, checkEnum = {1}」";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return 期望枚举类型
     */
    Class<? extends EnumBase> enumClass();
}
