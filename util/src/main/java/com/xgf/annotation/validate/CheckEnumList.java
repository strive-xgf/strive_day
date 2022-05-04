package com.xgf.annotation.validate;

import com.xgf.annotation.validate.validator.CheckEnumListValidator;
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
 * @create 2022-05-04 10:31
 * @description EnumBase 的 code 集合校验
 * 注意：为空校验通过
 **/

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckEnumListValidator.class)
@Documented
public @interface CheckEnumList {

    String message() default "「校验枚举code第 {0} 条出现错误, code = {1}, checkEnum = {2}」";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends EnumBase> enumClass();
}
