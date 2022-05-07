package com.xgf.annotation.validate;

import com.xgf.annotation.validate.validator.CheckNumberValueValidator;
import com.xgf.constant.CommonNullConstantUtil;
import com.xgf.constant.StringConstantUtil;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author xgf
 * @create 2022-05-04 17:37
 * @description 校验数据值
 **/

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckNumberValueValidator.class)
@Documented
public @interface CheckNumberValueAnnotation {
    String message() default "「数据验证不通过, 原因: {0}, 校验对象值: {1}」";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 最小值，包含
     * 【注意 > 非数值类型会报错: NumberFormatException】
     * @return 最小值
     */
    String min() default StringConstantUtil.EMPTY;

    /**
     * 最大值，包含
     * 【注意 > 非数值类型会报错: NumberFormatException】
     * @return 最大值
     */
    String max() default StringConstantUtil.EMPTY;


    /**
     * 限制小数位
     * 【该值只对数值型数据有效】注意，如果不是数值类型数据，将报错
     * @return 默认 -1 不限制
     */
    int scale() default -1;

    /**
     * 描述字段信息
     * @return 描述字段名（用于报错提示） {0}
     */
    String fieldDescription() default "字段";

    /**
     * 是否处理特殊值 ${@link CommonNullConstantUtil}，这些值当成空处理，不校验
     * @return 默认false
     */
    boolean dealSpecialValueFlag() default false;

}
