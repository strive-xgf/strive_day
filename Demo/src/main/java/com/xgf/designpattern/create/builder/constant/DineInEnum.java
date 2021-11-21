package com.xgf.designpattern.create.builder.constant;

import com.xgf.constant.EnumBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xgf
 * @create 2021-11-21 11:05
 * @description
 **/

@Getter
@AllArgsConstructor
public enum DineInEnum implements EnumBase {
    DINEINROOM("dineInRoom", "堂食 - 餐厅现场就餐")
    ;

    private String code;
    private String value;

    public static DineInEnum getByCode(String code) {

        if(StringUtils.isBlank(code)){
            return null;
        }

        for(DineInEnum var : values()){
            if (var.getCode().equals(code)) {
                return var;
            }
        }
        return null;
    }


    public static DineInEnum getByValue(String value) {

        if(StringUtils.isBlank(value)){
            return null;
        }

        for(DineInEnum var : values()){
            if (var.getValue().equals(value)) {
                return var;
            }
        }
        return null;
    }

}
