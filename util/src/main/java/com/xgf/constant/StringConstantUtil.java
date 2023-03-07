package com.xgf.constant;

import com.xgf.exception.CustomExceptionEnum;
import com.xgf.java8.BooleanFunctionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    public static final String ADD = "+";
    public static final String SUBTRACT = "-";
    public static final String UNDERLINE = "_";
    public static final String EQUALS_SIGN = "=";
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
    public static final String COLON = ":";
    public static final String CHINESE_COLON = "：";
    public static final String SEMICOLON = ";";
    public static final String CHINESE_SEMICOLON = "；";
    public static final String CHANGE_SEPARATOR = " => ";
    public static final String PARAM = "param";
    public static final String EXCEPTION = "exception";
    public static final String EXIST = "exist";

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
     * 正则：浮点数
     */
    public static final String MATCH_FLOAT_NUMBER = "^(-?\\d+)(\\.\\d+)?$";

    /**
     * 正则：百分数
     */
    public static final String MATCH_PERCENT_NUMBER = "^([0-9.]+)[ ]*%$";

    /**
     * 正则: 匹配汉字（中文字符）
     */
    public static final String MATCH_CHINESE = "[\\u4E00-\\u9FA5]+";

    /**
     * 正则: 匹配URL链接
     */
    public static final String MATCH_URL = "^(http|https)://.+";

    // ---------- request header key start -----------

    /**
     * token 令牌
     */
    public static final String TOKEN_HERDER_KEY = "strive-token";

    // ---------- request header key end -----------



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
     * 判断字符串是否以 startStr开头，是的话直接返回，否则添加这个开头
     *
     * @param str 字符串
     * @param startStr 开头前缀
     * @return 以 startStr 开头的字符串
     */
    public static String defaultStartWith(String str, String startStr) {
        if (str.startsWith(startStr)) {
            return str;
        }
        return startStr + str;
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

    public static String listToString(List<?> dataList) {
        return StringUtils.join(dataList, StringConstantUtil.COMMA);
    }

    /**
     * List 转 String
     *
     * @param dataList List 数据
     * @param separator 分隔符
     * @return String字符串
     */
    public static String listToString(List<?> dataList, Object separator) {
        return StringUtils.join(dataList, separator);
    }


    /**
     * List 转 String （不保留数组形式）
     *
     * @param dataList List 数据
     * @param separator 分隔符
     * @return String字符串
     */
    public static String listToString2(List<?> dataList, Object separator) {
        StringBuilder sb = new StringBuilder();
        Optional.ofNullable(dataList).orElseGet(ArrayList::new).forEach(p -> sb.append(p).append(separator));
        return sb.toString();
    }

    /**
     * 校验字符串全为数字
     * @param str 字符串
     * @return true: 全数字
     */
    public static boolean checkStrIsNumber(String str){
        return checkStrIsMatches(str, MATCH_ALL_NUMBER);

    }

    /**
     * 校验字符串全为数字，包含负数
     * @param str 字符串
     * @return true: 全数字
     */
    public static boolean checkStrIsNumberContainNegative(String str){
        return checkStrIsMatches(str, MATCH_ALL_NUMBER_CONTAIN_NEGATIVE);

    }

    /**
     * 校验字符串全为字母
     * @param str 字符串
     * @return true: 全字母
     */
    public static boolean checkStrIsLetter(String str){
        return checkStrIsMatches(str, MATCH_ALL_LETTER);

    }


    /**
     * 校验字符串是否为浮点数
     * @param str 字符串
     * @return true: 浮点数
     */
    public static boolean checkStrIsFloatNumber(String str){
        return checkStrIsMatches(str, MATCH_FLOAT_NUMBER);

    }

    /**
     * @param str 校验字符串
     * @return true：百分数（eg: 22.2%）
     */
    public static boolean checkStrIsPercentNumber(String str){
        return checkStrIsMatches(str, MATCH_PERCENT_NUMBER);
    }

    /**
     * 校验字符串是否全为中文汉字
     * @param str 字符串
     * @return true: 汉字全匹配
     */
    public static boolean checkStrIsChinese(String str){
        return checkStrIsMatches(str, MATCH_CHINESE);

    }

    /**
     * 校验字符串是否为url链接
     * @param str 字符串
     * @return true: url地址合法
     */
    public static boolean checkStrIsURL(String str){
        return checkStrIsMatches(str, MATCH_URL);
    }

    /**
     * 校验字符串是否符合正则表达式
     * @param str 字符串
     * @param regex 正则表达式
     * @return true: 符合
     */
    public static boolean checkStrIsMatches(String str, String regex) {
        if (str == null) {
            return false;
        }

        return str.matches(regex);
    }


    /**
     * 数字前置填充0，补齐位数（若当前数字位数 > 补齐后位数，则直接返回原数值）
     *
     * @param num 需要补齐的数值
     * @param length 补齐后希望长度
     * @return 补齐后的字符串
     */
    public static String preFillZero(int num, int length) {
        BooleanFunctionUtil.trueRunnable(length <= 0).run(() -> {throw CustomExceptionEnum.PARAM_TYPE_ILLEGAL_EXCEPTION.generateException(" length = " + length);});
        // %0[x]d
        return String.format("%0".concat(String.valueOf(length)).concat("d"), num);
    }


    public static String substringByPreSuf(String str, String prefix, String suffix) {
        return StringConstantUtil.substringByPreSuf(str, prefix, suffix, true);
    }

    /**
     * 截取 str 指定内容包含的值, eg: 123[456]78]9，截取 []， 则值为[456]78]
     *
     * @param str 字符串
     * @param prefix 指定最先出现的值
     * @param suffix 指定最后出现的值
     * @param containPreSufFlag 是否包含前后缀内容， eg: 123[456]78]9，截取 []，包含则值为[456]78]，不包含则为: 456]78
     * @return
     */
    public static String substringByPreSuf(String str, String prefix, String suffix, boolean containPreSufFlag) {
        // 找到 [ 最先开始的下标
        int prefixIndex = str.indexOf(prefix);
        if (prefixIndex == -1) {
            return null;
        }
        // 找到 ] 最后结束的下标
        int suffixIndex = str.lastIndexOf(suffix);
        if (suffixIndex == -1) {
            return null;
        }
        // 包含前后缀
        if (containPreSufFlag) {
            return str.substring(prefixIndex, suffixIndex + 1);
        }
        return str.substring(prefixIndex + 1, suffixIndex);
    }


    public static String replaceStrByMap(String str, Map<String, String> replaceMap) {
        List<String> resultList = replaceStrByMap(Collections.singletonList(str), replaceMap);

        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }

        return resultList.get(0);
    }


    /**
     * 根据 map 替换字符串信息指定内容
     *
     * @param strList 被处理的字符串集合
     * @param replaceMap 内容替换map集合, key: 替换前的字符串，支持正则表达式， value: 替换后的值
     */
    public static List<String> replaceStrByMap(List<String> strList, Map<String, String> replaceMap) {

        if (CollectionUtils.isEmpty(strList) || MapUtils.isEmpty(replaceMap)) {
            return strList;
        }

        List<String> resultList = strList;
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            // 替换文本内容
            resultList = resultList.stream().filter(StringUtils::isNotBlank)
                    .map(p -> p.replaceAll(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        }

        return resultList;
    }










    //  >>>>>>>>>> 封装 <<<<<<<<<<
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    public static <T extends CharSequence> T defaultIfBlank(T str, T defaultValue) {
        return StringUtils.defaultIfBlank(str, defaultValue);
    }



}
