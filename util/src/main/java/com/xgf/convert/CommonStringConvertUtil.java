package com.xgf.convert;

import com.xgf.constant.StringConstantUtil;
import com.xgf.constant.enumclass.LetterTypeEnum;

import java.util.Arrays;
import java.util.List;

/**
 * @author strive_day
 * @create 2023-04-03 3:18
 * @description 通用字符串转换
 */
public class CommonStringConvertUtil {


    /**
     * 驼峰表达式，转换为下划线格式 eg：aaBbCcDEFLetter > aa_bb_cc_d_e_f_letter
     *
     * @param str 参数字符串str
     * @return 转换后的下划线格式
     */
    public static String humpToUnderLine(String str) {
        return humpToFormat(str, StringConstantUtil.UNDERLINE, LetterTypeEnum.ALL_LETTER_LOWERCASE);
    }

    /**
     * 驼峰表达式转指定格式数据（如：下划线），eg: aaBbCcDEFLetter > Aa_Bb_Cc_D_E_F_Letter
     *
     * @param str            源字符串
     * @param formatStr      转换为指定格式字符串（大写前缀添加）
     * @param letterTypeEnum 数据格式枚举（eg：全大写、全小写、首字母大写）
     * @return 转换后的字符串
     */
    public static String humpToFormat(String str, String formatStr, LetterTypeEnum letterTypeEnum) {
        if (StringConstantUtil.isBlank(str)) {
            return str;
        }

        // 结果合集
        StringBuilder sb = new StringBuilder();
        // 加入首字母，不追加 formatStr（首字母大写的情况。需要处理）
        sb.append(LetterTypeEnum.isFirstLetterUppercase(letterTypeEnum) ? Character.toUpperCase(str.charAt(0)) : str.charAt(0));

        // 找到大写字符，且不是第一个字符，前置加formatStr
        for (int i = 1; i < str.length(); i++) {
            char curr = str.charAt(i);
            // 大写字母追加前缀
            if (Character.isUpperCase(curr)) {
                sb.append(formatStr);
            }
            sb.append(curr);
        }

        // 大小写转换
        String result = LetterTypeEnum.isAllLetterLowercase(letterTypeEnum)
                ? sb.toString().toLowerCase()
                : LetterTypeEnum.isAllLetterUppercase(letterTypeEnum)
                ? sb.toString().toUpperCase()
                : sb.toString();


        return result;
    }


    /**
     * 指定格式数据转驼峰表达式（如：下划线），eg: aa_bb_cc_d_e_f_letter > aaBbCcDEFLetter
     *
     * @param str       源字符串
     * @param formatStr 需要被替换的格式字符串
     * @return 转换后的字符串
     */
    public static String formatToHump(String str, String formatStr) {
        if (StringConstantUtil.isBlank(str)) {
            return str;
        }

        // 按字符串格式分割数组
        List<String> strList = Arrays.asList(str.split(formatStr));

        // 结果合集
        StringBuilder sb = new StringBuilder();
        // 首字母小写
        sb.append(StringConstantUtil.firstLetterLowerCase(strList.get(0)));

        // 转换为首字母大写的字符串
        for (int i = 1; i < strList.size(); i++) {
            sb.append(StringConstantUtil.firstLetterUpperCase(strList.get(i)));
        }

        return sb.toString();
    }


}
