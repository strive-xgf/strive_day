package com.xgf.common;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.xgf.constant.StringConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author xgf
 * @create 2022-05-12 17:59
 * @description 拼音工具类JPinYinU，可用于处理多音字的问题，返回多音字正确的唯一拼音
 **/

@Slf4j
public class PinYinUtil2 {


    /**
     * 检查汉字是否为多音字
     *
     * @param pinYinStr 需检查的汉字
     * @return true 多音字，false 不是多音字
     */
    public static boolean checkMultiPinYin(char pinYinStr) {

        return PinyinHelper.hasMultiPinyin(pinYinStr);

    }

    /**
     * 简体转繁体
     *
     * @param str 简体字符串
     * @return 繁体字符串
     */
    public static String toTraditional(String str) {

        try {
            return ChineseHelper.convertToTraditionalChinese(str);
        } catch (Exception e) {
            log.info("====== PinYinUtil2 tooTraditional exception, message = {}", e.getMessage(), e);
            return str;
        }

    }

    /**
     * 繁体转简体
     *
     * @param str 繁体字符串
     * @return 简体字符串
     */
    public static String toSimplified(String str) {

        try {
            return ChineseHelper.convertToSimplifiedChinese(str);
        } catch (Exception e) {
            log.info("====== PinYinUtil2 tooTraditional exception, message = {}", e.getMessage(), e);
            return str;
        }

    }


    /**
     * 转换为拼音字符串（自动处理多音字 - Dict 字典中处理）
     *
     * @param str       汉字
     * @param separator 每个词语之间的分隔符
     * @param format    格式（音调）
     * @return 有声调的拼音字符串
     */
    public static String toPinYin(String str, String separator, PinyinFormat format) {

        if (StringUtils.isBlank(str)) {
            return StringConstantUtil.EMPTY;
        }

        if (StringUtils.isEmpty(separator)) {
            separator = StringConstantUtil.EMPTY;
        }

        if (Objects.isNull(format)) {
            format = PinyinFormat.WITHOUT_TONE;
        }

        try {
            return PinyinHelper.convertToPinyinString(str, separator, format);
        } catch (Exception e) {
            log.info("====== PinYinUtil2 tooTraditional exception, message = {}", e.getMessage(), e);
            return str;
        }

    }

    public static String toPinYin(String str) {
        return toPinYin(str, null, null);
    }

    public static String toPinYin(String str, String separator) {
        return toPinYin(str, separator, null);
    }

    public static String toPinYin(String str, PinyinFormat format) {
        return toPinYin(str, null, format);
    }

}
