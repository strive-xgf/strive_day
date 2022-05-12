package com.xgf.util.common;

import com.alibaba.fastjson.JSON;
import com.xgf.common.PinYinUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author xgf
 * @create 2022-05-11 18:32
 * @description 拼音工具类测试
 **/

public class PinYinUtilTest {

    @Test
    public void test() throws BadHanyuPinyinOutputFormatCombination {

        String str = "我是汉语ü, 今天星期几啦";
        char c = '国';

        // 使用 HanyuPinyinOutputFormat 设置拼音格式
        HanyuPinyinOutputFormat pinyinFormat = new HanyuPinyinOutputFormat();

        System.out.println("default = " + JSON.toJSONString(PinyinHelper.toHanyuPinyinStringArray('国', pinyinFormat)));

        // 拼音大小写: LOWERCASE: 拼音小写格式输出, UPPERCASE: 拼音大写格式输出
        pinyinFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 拼音声调格式转化, WITH_TONE_NUMBER: 用数字表示声调, WITHOUT_TONE: 无声调表示, WITH_TONE_MARK: 用声调符号表示
        pinyinFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        // 拼音特殊字符 ü 显示格式, WITH_U_AND_COLON: 用 u: 来表示, WITH_V: 用 v 来表示, WITH_U_UNICODE: 用 ü 来表示
        pinyinFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

        String[] result1 = PinyinHelper.toHanyuPinyinStringArray(c, pinyinFormat);
        System.out.println("result1 = " + JSON.toJSONString(result1));

        String[] result2 = PinyinHelper.toHanyuPinyinStringArray(c);
        System.out.println("result2 = " + JSON.toJSONString(result2));

        String result3 = PinyinHelper.toHanyuPinyinString(str, pinyinFormat, " ");
        System.out.println("result3 = " + JSON.toJSONString(result3));


    }

    @Test
    public void testToFirstSpell() {
        assertEquals("   zq   a 123 strive qwe xx wszwywszzh >> ", PinYinUtil.toFirstSpell("   重庆   A 123 strive QWE xx 我是中文英文数字组合 >> "));
        String str = "<生活不止眼前的苟且，还有诗和远方> strive day";
        System.out.println(PinYinUtil.toFirstSpell(str));
    }

    @Test
    public void testToPinYing() {

        String str = "<生活不止眼前的苟且，还有诗和远方> strive day";

        System.out.println(PinYinUtil.toPinYing(str));

        System.out.println("default = " + PinYinUtil.toPinYing(str, false, false, null));
        System.out.println("大写 = " + PinYinUtil.toPinYing(str, true, false, null));
        System.out.println("大写 + 多音字输出 = " + PinYinUtil.toPinYing(str, true, true, null));

        HanyuPinyinOutputFormat pinyinFormat = new HanyuPinyinOutputFormat();
        // 拼音大小写: LOWERCASE: 拼音小写格式输出
        pinyinFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 拼音声调格式转化, WITH_TONE_MARK: 用声调符号表示
        pinyinFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        // 拼音特殊字符 ü 显示格式, WITH_U_UNICODE: 用 ü 来表示
        pinyinFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

        System.out.println("大写 + 多音字输出 + 自定义格式 = " + PinYinUtil.toPinYing(str, true, true, pinyinFormat));
    }

    @Test
    public void testToAscii() {
        String str = "abcdeft<生活不止眼前的苟且，还有诗和远方> strive day";
        System.out.println(PinYinUtil.toAscii(str));
    }

    @Test
    public void testToNumberChinese() {
        String str = "1234567890hhh_str>0987654321";
        System.out.println(PinYinUtil.toNumberChinese(str));
    }


}
