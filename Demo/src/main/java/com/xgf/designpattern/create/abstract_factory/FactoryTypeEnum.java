package com.xgf.designpattern.create.abstract_factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xgf
 * @create 2021-11-15 11:10
 * @description 工厂类型枚举
 **/

@Getter
@AllArgsConstructor
public enum FactoryTypeEnum {

    FACTORY_SHAPE("factory_shape", "形状工厂"),
    FACTORY_COLOR("factory_color", "颜色工厂"),
    ;

    private String code;
    private String value;

    /**
     * 通过code获取枚举
     * @param code
     * @return
     */
    public static FactoryTypeEnum getByCode(String code) {

        if(StringUtils.isBlank(code)){
            return null;
        }

        for(FactoryTypeEnum var : values()){
            if (var.getCode().equals(code)) {
                return var;
            }
        }
        return null;
    }

    /**
     * 通过value获取枚举
     * @param value
     * @return
     */
    public static FactoryTypeEnum getByValue(String value) {

        if(StringUtils.isBlank(value)){
            return null;
        }

        for(FactoryTypeEnum var : values()){
            if (var.getValue().equals(value)) {
                return var;
            }
        }
        return null;
    }
}
