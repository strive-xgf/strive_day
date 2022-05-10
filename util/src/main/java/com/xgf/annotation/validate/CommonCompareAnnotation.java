package com.xgf.annotation.validate;

import com.xgf.annotation.validate.validator.CommonCompareValidator;
import com.xgf.check.CompareOperatorEnum;
import com.xgf.constant.CommonNullConstantUtil;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author xgf
 * @create 2022-05-07 14:47
 * @description 比较类对象的字段值 【checkColumn operator conditionColumn】
 * 对象为空不校验，存在字段为空不校验（checkColumn / conditionColumn 为 null)
 **/
@Target({FIELD ,PARAMETER,TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {CommonCompareValidator.class})
public @interface CommonCompareAnnotation {

    /**
     * 存在desc，使用desc描述，不存在 使用 checkColumn/conditionColumn 字段名作为描述
     */
    String message() default "{checkColumnFieldDesc}/{checkColumn} 必须 {operator.getDesc()} {conditionColumnFieldDesc}/{conditionColumn}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * @return 校验列（属性字段名）
     */
    String checkColumn();

    /**
     * @return 校验列的字段描述
     */
    String checkColumnFieldDesc() default "";

    /**
     * @return 条件列（属性字段名）[比较值]
     */
    String conditionColumn();

    /**
     * @return 比较列的字段描述
     */
    String conditionColumnFieldDesc() default "";

    /**
     * 是否处理特殊空值 ${@link CommonNullConstantUtil}，这些值存在当成空处理，不校验
     * @return 默认 false
     */
    boolean dealSpecialValueFlag() default false;

    /**
     * @return 比较运算符
     */
    CompareOperatorEnum operator();

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        CommonCompareAnnotation[] value();
    }


}
