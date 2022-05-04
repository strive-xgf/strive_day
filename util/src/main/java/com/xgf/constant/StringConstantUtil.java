package com.xgf.constant;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-04-17 21:45
 * @description String 常量工具类
 **/

public class StringConstantUtil {

    public static final String EMPTY = "";
    public static final String BLANK = " ";
    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String POUND = "#";
    public static final String DOWN_DIAGONAL = "\\";
    public static final String UP_DIAGONAL = "/";
    public static final String LEFT_SMALL_BRACKET = "(";
    public static final String RIGHT_SMALL_BRACKET = ")";
    public static final String LEFT_MIDDLE_BRACKET = "[";
    public static final String RIGHT_MIDDLE_BRACKET = "]";
    public static final String CHINESE_LEFT_MIDDLE_BRACKET = "【";
    public static final String CHINESE_RIGHT_MIDDLE_BRACKET = "】";
    public static final String LEFT_BIG_BRACKET = "{";
    public static final String RIGHT_BIG_BRACKET = "}";
    public static final String LINE_FEED = "\r\n";
    public static final String LOG = "log";
    public static final String SYS_TIME_LOG = "sysTimeLog";

    /**
     * 正则：数字
     */
    public static final String MATCH_ALL_NUMBER = "^[0-9]+$";

    /**
     * 正则：数字，包含正负
     */
    public static final String MATCH_ALL_NUMBER_CONTAIN_NEGATIVE="^(-)?[0-9]+$";

    /**
     * 正则：字母
     */
    public static final String MATCH_ALL_LETTER = "^[a-zA-Z]+$";




    /**
     * 半角转全角
     *
     * @param str 半角字符串
     * @return 全角字符串
     */
    public static String dbcToSbc(String str) {
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    /**
     * 全角转半角
     *
     * @param str 全角字符串
     * @return 半角字符串
     */
    public static String sbcToDbc(String str) {
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 去除 字符串 最后一个 指定字符，如果最后一个不满足，正常返回
     * @param str 处理字符串
     * @param sweepField 需要去除的最后一个字符字段
     * @return 结果
     */
    public static String endTrimSweep(String str, String sweepField) {
        return str.endsWith(sweepField) ? str.substring(0, str.length() - 1) : str;
    }

    /**
     * 拼接中文中括号
     * @param str 拼接内容
     * @return 添加中括号之后
     */
    public static String stringAppendChineseMidBracket(String str) {
        return stringBuilderAppend(null, str, CHINESE_LEFT_MIDDLE_BRACKET, CHINESE_RIGHT_MIDDLE_BRACKET).toString();
    }

    /**
     * 添加后缀
     * @param str 拼接内容
     * @param suffix 后缀
     * @return 拼接后字符串
     */
    public static String stringAddSuffix(String str, String suffix){
        return stringBuilderAppend(null, str, null, suffix).toString();
    }

    /**
     * 添加前缀
     * @param str 拼接内容
     * @param prefix 前缀
     * @return 拼接后字符串
     */
    public static String stringAddPrefix(String str, String prefix){
        return stringBuilderAppend(null, str, prefix, null).toString();
    }

    /**
     * 字符串列表 添加前后缀
     * @param sb StringBuilder
     * @param strList 字符串数组
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 拼接后字符串
     */
    public static String stringListAppend(StringBuilder sb, List<String> strList, String prefix, String suffix) {
        if (CollectionUtils.isEmpty(strList)) {
            return EMPTY;
        }

        for (String str : strList) {
            sb = stringBuilderAppend(sb, str, prefix, suffix);
        }

        return sb.toString();
    }

    public static String stringListAppend(List<String> strList, String prefix, String suffix) {
        return stringListAppend(null, strList, prefix, suffix);
    }

    /**
     * 拼接字符串（前后缀）
     * @param sb StringBuilder
     * @param str 拼接内容
     * @param prefix 前缀
     * @param suffix 后缀
     * @return StringBuilder
     */
    public static StringBuilder stringBuilderAppend(StringBuilder sb, String str, String prefix, String suffix){
        if (Objects.isNull(sb)) {
            sb = new StringBuilder();
        }

        if (Objects.nonNull(str) && str.length() > 0) {
            if (Objects.nonNull(prefix)) {
                sb.append(prefix);
            }
            sb.append(str);

            if (Objects.nonNull(suffix)) {
                sb.append(suffix);
            }
        }
        return sb;
    }




        /**
         * 校验字符串全为数字
         * @param str 字符串
         * @return true: 全数字
         */
    public static boolean checkStrIsNumber(String str){
        if (str == null) {
            return false;
        }
        return str.matches(MATCH_ALL_NUMBER);
    }

    /**
     * 校验字符串全为数字，包含负数
     * @param str 字符串
     * @return true: 全数字
     */
    public static boolean checkStrIsNumberContainNegative(String str){
        if (str == null) {
            return false;
        }
        return str.matches(MATCH_ALL_NUMBER_CONTAIN_NEGATIVE);
    }

    /**
     * 校验字符串全为字母
     * @param str 字符串
     * @return true: 全字母
     */
    public static boolean checkStrIsLetter(String str){
        if (str == null) {
            return false;
        }
        return str.matches(MATCH_ALL_LETTER);
    }


}
