package com.xgf.util.constant;

import com.alibaba.fastjson.JSON;
import com.xgf.bean.Hobby;
import com.xgf.bean.User;
import com.xgf.bean.WorkInfo;
import com.xgf.constant.DataEntry;
import com.xgf.constant.DataRange;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * @author xgf
 * @create 2022-04-24 23:11
 * @description
 **/

public class ConstantTest {

    User user = User.builder()
            .userUuid("userUuid")
            .passWord("123")
            .age(18)
            .hobby(Hobby.builder().hobbyType("hobbyType").hobbyCount(12).createdTime(new Date()).hobbyUuid("hobbyUuid").build())
            .createdTime(new Date())
            .bigDecimal(new BigDecimal("9999"))
            .stringList(Arrays.asList("1", "2", "3", "4", "5", "6"))
            .workInfoList(Arrays.asList(WorkInfo.builder().workUuid("workUuid1").workContent("workContent1").build(),
                    WorkInfo.builder().workUuid("workUuid2").workContent("workContent2").build()))
            .build();

    @Test
    public void testDataEntry() {
        DataEntry<String, String> str2strEntry = DataEntry.valueOf("key1", "value2");
        DataEntry<String, User> userUuid2UserEntry = DataEntry.valueOf(user.getUserUuid(), user);
        System.out.println("str2strEntry = " + str2strEntry);
        System.out.println("userUuid2UserEntry = " + userUuid2UserEntry);
    }

    @Test
    public void testRangEntry() {

        DataRange<Integer> intRange = DataRange.valueOf(999999, 100);
        DataRange<BigDecimal> bigDecimalRange = DataRange.valueOf(new BigDecimal("1.00"), new BigDecimal("999999.99"));
        DataRange<Double> doubleRange = DataRange.valueOf(1.23D, 666.668D, false, true);
        System.out.println("intRange = " + intRange.printRange());
        System.out.println("intRange. = " + intRange.getLower());

        System.out.println("bigDecimalRange = " + bigDecimalRange.printRange());
        System.out.println("doubleRange = " + doubleRange.printRange());

        System.out.println("100 in range " + intRange.printRange() + " >> " + intRange.inRange(100));
        System.out.println("999999.99 in range " + bigDecimalRange.printRange() + " >> " + bigDecimalRange.inRange(new BigDecimal("999999.99")));
        System.out.println("1.23 in range " + doubleRange.printRange() + " >> " + doubleRange.inRange(1.23D));
        System.out.println("1.25 in range " + doubleRange.printRange() + " >> " + doubleRange.inRange(1.25D));

    }



}
