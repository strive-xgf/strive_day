package com.xgf.util;

import com.alibaba.fastjson.JSON;
import com.xgf.date.DateUtil;
import org.junit.Test;

import java.util.Date;

/**
 * @author strive_day
 * @create 2021-07-18 15:55
 * @description
 */
public class DateUtilTest {

    @Test
    public void testIntervalTime(){
//        System.out.println(DateUtil.stringParseDate("2021d年7月18日 16we时38分22d秒222毫2秒2a"));
//        System.out.println(DateUtil.stringParseDate("2021-12-21 2:2:2.2"));

//        System.out.println(DateUtil.computeIntervalTimeByDate(new Date(), new Date()));
//        System.out.println(DateUtil.computeIntervalTimeByString("2021年7月18日 16时38分22秒222毫秒", "2021-12-25"));
//        System.out.println(DateUtil.computeIntervalTimeByObject(new Date(), "2021-8-1"));
//        System.out.println(JSON.toJSONString(DateUtil.computeIntervalTimeByObject(new Date(), "2021-8-1")));
        // 通过反射获取变量类型
//        System.out.println("2021".getClass().getCanonicalName());
//        System.out.println(new Date().getClass().getCanonicalName());

        System.out.println(DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_MILL_CN));

        Date date = new Date();
        System.out.println(DateUtil.dateFormatString(date, DateUtil.FORMAT_MILL));

        date = DateUtil.addYear(date, 2);
        date = DateUtil.addMonth(date, 5);
        date = DateUtil.addDay(date, 36);
        date = DateUtil.addMinute(date, 72);
        date = DateUtil.addSecond(date, 108);
        System.out.println(DateUtil.dateFormatString(date, DateUtil.FORMAT_MILL));

        System.out.println(DateUtil.getWeekOfYear(new Date()));
        System.out.println(DateUtil.getWeekOfMonth(new Date()));
        System.out.println(DateUtil.getDayOfYear(new Date()));

        System.out.println(DateUtil.getChineseDate(new Date(), DateUtil.FORMAT_SECOND_CN));
    }
}
