package com.xgf.designpattern.create.builder.constant;

import com.xgf.constant.EnumBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xgf
 * @create 2021-11-21 21:00
 * @description 单位枚举
 **/

@Getter
@AllArgsConstructor
public enum UnitEnum implements EnumBase {
    UNIT_ONE_NUMBER("unitOneNumber", " / 1个"),
    UNIT_ONE_BOTTLE("unitOneBottle", " / 1瓶")
    ;

    private String code;
    private String value;

    public static UnitEnum getByCode(String code) {

        if(StringUtils.isBlank(code)){
            return null;
        }

        for(UnitEnum var : values()){
            if (var.getCode().equals(code)) {
                return var;
            }
        }
        return null;
    }


    public static UnitEnum getByValue(String value) {

        if(StringUtils.isBlank(value)){
            return null;
        }

        for(UnitEnum var : values()){
            if (var.getValue().equals(value)) {
                return var;
            }
        }
        return null;
    }
}
