package com.xgf.designpattern.create.builder.constant;

import com.xgf.constant.EnumBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xgf
 * @create 2021-11-21 11:10
 * @description 打包方式枚举
 **/

@Getter
@AllArgsConstructor
public enum PackingEnum implements EnumBase {
    WRAPPER("wrapper", "打包 - 装盒"),
    BOTTLE("bottle", "打包 - 装瓶"),

    ;

    private String code;
    private String value;

    public static PackingEnum getByCode(String code) {

        if(StringUtils.isBlank(code)){
            return null;
        }

        for(PackingEnum var : values()){
            if (var.getCode().equals(code)) {
                return var;
            }
        }
        return null;
    }


    public static PackingEnum getByValue(String value) {

        if(StringUtils.isBlank(value)){
            return null;
        }

        for(PackingEnum var : values()){
            if (var.getValue().equals(value)) {
                return var;
            }
        }
        return null;
    }

}
