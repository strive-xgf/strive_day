package com.xgf.util.constant;

import com.xgf.constant.StringConstantUtil;
import com.xgf.randomstr.RandomStrUtil;
import org.junit.Test;

import javax.validation.constraints.AssertTrue;
import java.util.List;

/**
 * @author xgf
 * @create 2022-04-28 00:40
 * @description
 **/

public class StringConstantUtilTest {

    @Test
    public void testDbcAndSbc() {
        System.out.println(", >>> " + StringConstantUtil.dbcToSbc(","));
        System.out.println("， >>> " + StringConstantUtil.sbcToDbc("，"));
    }

    @Test
    public void testEndTrimSweep() {
        String s1 = "strive -";
        String s2 = "strive =";
        System.out.println(StringConstantUtil.endTrimSweep(s1, "-"));
        System.out.println(StringConstantUtil.endTrimSweep(s2, "-"));
    }

    @Test
    public void testStringAppend() {
        List<String> stringList = RandomStrUtil.batchCreateRandomList(10, 32);
        stringList.add("");
        stringList.add("  ");
        stringList.add(null);
        System.out.println("stringList = " + stringList);
        String stringListAppendResult = StringConstantUtil.stringListAppend(stringList, "'", "',");
        // 模拟字符串拼接
        System.out.println("stringListAppendResult = " + stringListAppendResult.substring(0, stringListAppendResult.length() - 1));

        String randomStr = RandomStrUtil.createRandomStr(32);
        String randomStrPrefix = StringConstantUtil.stringAddPrefix(randomStr, "prefix + ");
        String randomStrSuffix = StringConstantUtil.stringAddSuffix(randomStr, " + suffix");
        System.out.println("randomStrPrefix : " + randomStrPrefix);
        System.out.println("randomStrSuffix : " + randomStrSuffix);
    }


    @Test
    public void testCheckStrIsNumber() {
//        String str = "123678";
        String str = "-123678";
//        String str = "";
//        String str = null;
//        String str = "3.1415926";
        System.out.println(str + " is number = " + StringConstantUtil.checkStrIsNumber(str));

        System.out.println(str + " is number contain negative = " + StringConstantUtil.checkStrIsNumberContainNegative(str));
    }


    @Test
    public void testCheckStrIsLetter() {
        String str = "abilKjs";
//        String str = "abilKjs.";
        System.out.println(str + " is letter = " + StringConstantUtil.checkStrIsLetter(str));
    }

    @Test
    public void testC() {
        String str = "123456789.987654321";
        System.out.println(str + "\t" + StringConstantUtil.checkStrIsFloatNumber(str));
        str = "123456789123456789123456789.987654321987654321987654321";
        System.out.println(str + "\t" + StringConstantUtil.checkStrIsFloatNumber(str));
        str = "123456789123456789123456789123456789123456789123456789123456789123456789123456789.987654321987654321987654321987654321987654321987654321987654321987654321987654321";
        System.out.println(str + "\t" + StringConstantUtil.checkStrIsFloatNumber(str));
        str = "-123456789123456789123456789.987654321987654321987654321";
        System.out.println(str + "\t" + StringConstantUtil.checkStrIsFloatNumber(str));
        str = "--123456789123456789123456789.987654321987654321987654321";
        System.out.println(str + "\t" + StringConstantUtil.checkStrIsFloatNumber(str));
        str = "123456789123456789123456789.98765432198765.4321987654321";
        System.out.println(str + "\t" + StringConstantUtil.checkStrIsFloatNumber(str));

    }



}
