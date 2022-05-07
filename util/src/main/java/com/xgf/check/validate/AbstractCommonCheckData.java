package com.xgf.check.validate;

import com.xgf.constant.CommonNullConstantUtil;
import lombok.Data;

/**
 * @author xgf
 * @create 2022-05-05 12:09
 * @description 通用校验数据抽象
 **/

@Data
public abstract class AbstractCommonCheckData {

    /**
     * 字段信息描述，抛出异常会前置加该字段作为描述
     */
    private String fieldDescription;

    /**
     * 校验字段的实际值
     */
    private Object filedValue;

    /**
     * 特殊值（Null值）是否处理不校验, true 则不校验
     * {@link CommonNullConstantUtil#checkSpecialValue(java.lang.Object)}
     */
    private Boolean dealSpecialValueFlag;

    /**
     * 执行校验逻辑
     */
    protected abstract void checkDate();


}
