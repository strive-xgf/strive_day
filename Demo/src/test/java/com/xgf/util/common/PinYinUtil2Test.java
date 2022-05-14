package com.xgf.util.common;


import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.xgf.common.PinYinUtil2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author xgf
 * @create 2022-05-11 18:32
 * @description 拼音工具类2测试
 **/

public class PinYinUtil2Test {

    @Test
    public void testCheckMultiPinYin() {
        System.out.println(PinYinUtil2.checkMultiPinYin('重'));
        System.out.println(PinYinUtil2.checkMultiPinYin('和'));
        System.out.println(PinYinUtil2.checkMultiPinYin('我'));

    }


    @Test
    public void testToSimplified() {
        String str = "<生活不止眼前的苟且，還有詩和遠方> strive day";
        System.out.println(PinYinUtil2.toSimplified(str));
        assertEquals("<生活不止眼前的苟且，还有诗和远方> strive day", PinYinUtil2.toSimplified(str));
    }

    @Test
    public void testToTraditional() {
        String str = "<生活不止眼前的苟且，还有诗和远方> strive day";
        System.out.println(PinYinUtil2.toTraditional(str));
        assertEquals("<生活不止眼前的苟且，還有詩和遠方> strive day", PinYinUtil2.toTraditional(str));

    }

    @Test
    public void testToPinYin() {
        String str = "<生活不止眼前的苟且，还有诗和远方> strive day";
        System.out.println(PinYinUtil2.toPinYin(str));
        System.out.println(PinYinUtil2.toPinYin(str, " "));
        System.out.println(PinYinUtil2.toPinYin(str, "-", PinyinFormat.WITH_TONE_MARK));
        System.out.println(PinYinUtil2.toPinYin("癖好", PinyinFormat.WITH_TONE_MARK));
    }

}
