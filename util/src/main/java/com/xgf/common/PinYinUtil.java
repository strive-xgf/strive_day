package com.xgf.common;

import com.xgf.constant.StringConstantUtil;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author xgf
 * @create 2022-05-10 12:13
 * @description 拼音转换工具类
 **/

@Slf4j
public class PinYinUtil {

    /**
     * 返回自定义默认格式【小写, 无声调, ü显示】
     * @return 默认格式
     */
    public static HanyuPinyinOutputFormat defaultCustomFormat() {
        // 使用 HanyuPinyinOutputFormat 设置拼音格式
        HanyuPinyinOutputFormat pinyinFormat = new HanyuPinyinOutputFormat();

        // 拼音大小写: LOWERCASE: 拼音小写格式输出, UPPERCASE: 拼音大写格式输出
        pinyinFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 拼音声调格式转化, WITH_TONE_NUMBER: 用数字表示声调, WITHOUT_TONE: 无声调表示, WITH_TONE_MARK: 用声调符号表示
        pinyinFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // 拼音特殊字符 ü 显示格式, WITH_U_AND_COLON: 用 u: 来表示, WITH_V: 用 v 来表示, WITH_U_UNICODE: 用 ü 来表示
        pinyinFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

        return pinyinFormat;
    }

    /**
     * 返回中文的首拼（首字母组合）【小写】
     * @param str 中文字符串
     * @return 首字母小写组合
     */
    public static String toFirstSpell(String str) {

        if (StringUtils.isBlank(str)) {
            return StringConstantUtil.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char word = str.charAt(i);
            if (word >= 'A' && word <= 'Z') {
                // 大写转小写
                sb.append(String.valueOf(word).toLowerCase());
            } else if (StringConstantUtil.checkStrIsChinese(String.valueOf(word))) {
                // 转换拼音（默认小写，数字标识声调）【存在多音字，所以返回数组】
                String[] pinYinList = PinyinHelper.toHanyuPinyinStringArray(word);
                if (CollectionUtils.isNotEmpty(Arrays.asList(pinYinList))) {
                    sb.append(pinYinList[0].charAt(0));
                } else {
                    log.info("====== PinyinUtil convert pinYing value is empty");
                    sb.append(word);
                }
            } else {
                sb.append(word);
            }

        }
        return sb.toString();
    }


    /**
     * 汉语转拼音（小写，不处理多音字）
     *
     * @param str 汉语字符串
     * @return 小写拼音
     */
    public static String toPinYing(String str) {
        return toPinYing(str, false, false, defaultCustomFormat());
    }

    /**
     * 汉语转拼音带声调（小写，不处理多音字）
     *
     * @param str 汉语字符串
     * @return 带声调的小写拼音
     */
    public static String toPinYingWithTone(String str) {
        HanyuPinyinOutputFormat format = defaultCustomFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        return toPinYing(str, false, false, defaultCustomFormat());
    }

    /**
     * 汉字转换为全拼
     * @param str 汉字字符串
     * @param firstUpperCaseFlag 首拼大写
     * @param multiYinPrintFlag 多音字输出标识（支持，从第二个开始放到[]里面）
     * @param format 拼音格式
     * @return 全拼
     */
    public static String toPinYing(String str, boolean firstUpperCaseFlag, boolean multiYinPrintFlag, HanyuPinyinOutputFormat format) {

        if (StringUtils.isBlank(str)) {
            return StringConstantUtil.EMPTY;
        }

        if (Objects.isNull(format)) {
            format = defaultCustomFormat();
        }

        StringBuilder sb = new StringBuilder();

        String[] pinYinList = null;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((int) c <= 128) {
                sb.append(c);
                continue;
            }

            try {
                pinYinList = PinyinHelper.toHanyuPinyinStringArray(c, format);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                log.info("====== PinYinUtil toPinYing【BadHanyuPinyinOutputFormatCombination】 message = {}", e.getLocalizedMessage(), e);
            }
            
            if (pinYinList == null) {
                sb.append(c);
                continue;
            }

            // 首字母大小写，多音字输出格式
            sb.append(multiYinDeal(pinYinList, null, firstUpperCaseFlag, multiYinPrintFlag));
        }

        return sb.toString();

    }

    /**
     * 多音字输出格式
     *
     * @param pinYinList 拼音定长数组
     * @param separator 多音字字符分割符号
     * @param firstUpperCaseFlag 首字母大写标识
     * @param multiYinPrintFlag 多音字输出（false不输出多音字）
     * @return 拼音字符串
     */
    public static String multiYinDeal(String[] pinYinList, String separator, boolean firstUpperCaseFlag, boolean multiYinPrintFlag) {
        if (pinYinList == null || pinYinList.length == 0) {
            return StringConstantUtil.EMPTY;
        }

        if (StringUtils.isBlank(separator)) {
            separator = StringConstantUtil.COMMA + StringConstantUtil.BLANK;
        }

        StringBuilder sb = new StringBuilder();

        // 多音字会有多个结果，默认 multiYinPrintFlag 不开启时，取第一个，开启时，取第一个，后面的多音字节用[] 中括号包裹起来
        sb.append(firstUpperCaseByFlag(pinYinList[0], firstUpperCaseFlag));

        if (multiYinPrintFlag && pinYinList.length > 1) {
            // 多音字处理（存在多音字，组装到 [] 括号内）
            sb.append(StringConstantUtil.LEFT_MIDDLE_BRACKET);
            for (int i = 1; i < pinYinList.length; i++) {
                sb.append(firstUpperCaseByFlag(pinYinList[i], firstUpperCaseFlag));
                // 添加分隔符
                if (i != pinYinList.length -1) {
                    sb.append(separator);
                }
            }
            sb.append(StringConstantUtil.RIGHT_MIDDLE_BRACKET);
        }


        return sb.toString();
    }

    /**
     * 英文首字母是否大写
     * @param str 字符串
     * @param flag 是否需要首拼大写
     * @return 结果
     */
    public static String firstUpperCaseByFlag(String str, boolean flag) {
        if (StringUtils.isBlank(str)) {
            return StringConstantUtil.EMPTY;
        }

        if (flag) {
            // 首字母大写
            return str.toUpperCase().charAt(0) + str.substring(1);
        }

        return str;
    }

    /**
     * 将字符串转换为ASCII码，并转换为16进制最低取低8位
     * @param str 字符串ASCII码值
     * @return 字符串的
     */
    public static String toAscii(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] byteList = str.getBytes();
        for (byte b : byteList) {
            // 转16进制，取低八位
            // 0xff表示的数二进制数 11111111 占一字节，和其进行 &与 操作的数，最低8位
            sb.append(Integer.toHexString(b & 0xff));
        }
        return sb.toString();
    }

    /**
     * 字符串中数字转中文
     *
     * @param str 字符串
     * @return 数字转为中文后的字符串
     */
    public static String toNumberChinese(String str) {

        // 中文数字集合
        final char[] chineseNumberList = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};

        if (StringUtils.isBlank(str)) {
            return StringConstantUtil.EMPTY;
        }

        for (int i = 0; i <= chineseNumberList.length; i++) {
            if (str.contains(String.valueOf(i))) {
                // 数字替换
                str = str.replaceAll(String.valueOf(i), String.valueOf(chineseNumberList[i]));
            }
        }

        return str;
    }


}
