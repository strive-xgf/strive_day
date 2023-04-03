package com.xgf.constant.enumclass;

import com.xgf.constant.EnumBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author strive_day
 * @create 2023-04-03 15:06
 * @description 单词类型枚举
 */

@Getter
@AllArgsConstructor
public enum LetterTypeEnum implements EnumBase {

    DEFAULT("default", "默认自定义格式"),
    FIRST_LETTER_UPPERCASE("firstLetterUppercase", "首字母大写"),
    ALL_LETTER_UPPERCASE("allLetterUppercase", "所有字符大写"),
    ALL_LETTER_LOWERCASE("allLetterLowercase", "所有字符小写"),
    ;


    private String code;
    private String value;

    public static LetterTypeEnum getByCode(String code) {
        return Stream.of(LetterTypeEnum.values()).filter(p -> p.getCode().equals(code)).findFirst().orElse(null);
    }

    public static LetterTypeEnum getByValue(String value) {
        return Stream.of(LetterTypeEnum.values()).filter(p -> p.getValue().equals(value)).findFirst().orElse(null);
    }

    public static boolean isFirstLetterUppercase(LetterTypeEnum letterTypeEnum) {
        return letterTypeEnum == LetterTypeEnum.FIRST_LETTER_UPPERCASE;
    }

    public static boolean isAllLetterUppercase(LetterTypeEnum letterTypeEnum) {
        return letterTypeEnum == LetterTypeEnum.ALL_LETTER_UPPERCASE;
    }

    public static boolean isAllLetterLowercase(LetterTypeEnum letterTypeEnum) {
        return letterTypeEnum == LetterTypeEnum.ALL_LETTER_LOWERCASE;
    }

}
