package com.xgf.util.constant;

import com.xgf.constant.StringConstantUtil;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.java8.BranchHandleUtil;
import com.xgf.java8.ThrowExceptionFunctionUtil;
import com.xgf.randomstr.RandomStrUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.validation.constraints.AssertTrue;
import java.util.List;

/**
 * @author xgf
 * @create 2022-04-28 00:40
 * @description
 **/

public class StringConstantUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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

    @Test
    public void testPreFillZero() {
        String str000668 = StringConstantUtil.preFillZero(668, 6);
        BranchHandleUtil.isTrueOrFalse(str000668.equals("000668"))
                .handle(() -> System.out.println(str000668), () -> {throw CustomExceptionEnum.UNIT_TEST_EXCEPTION.generateException();});

        String str668 = StringConstantUtil.preFillZero(668, 2);
        BranchHandleUtil.isTrueOrFalse(str668.equals("668"))
                .handle(() -> System.out.println(str668), () -> {throw CustomExceptionEnum.UNIT_TEST_EXCEPTION.generateException();});

        String str000000668 = StringConstantUtil.preFillZero(668, 9);
        BranchHandleUtil.isTrueOrFalse(str000000668.equals("000000668"))
                .handle(() -> System.out.println(str000000668), () -> {throw CustomExceptionEnum.UNIT_TEST_EXCEPTION.generateException();});
    }


    @Test
    public void testSubstringByPreSuf() {
        String str1 = StringConstantUtil.substringByPreSuf("https://www.baidu.com/img/222[1-100]aa", StringConstantUtil.LEFT_MIDDLE_BRACKET, StringConstantUtil.RIGHT_MIDDLE_BRACKET);
        ThrowExceptionFunctionUtil.isFalseThrow("[1-100]".equals(str1)).throwMessage("[1-100]");

        String str2 = StringConstantUtil.substringByPreSuf("https://www.baidu.com/img/2[22[1-100]a]a", StringConstantUtil.LEFT_MIDDLE_BRACKET, StringConstantUtil.RIGHT_MIDDLE_BRACKET);
        ThrowExceptionFunctionUtil.isFalseThrow("[22[1-100]a]".equals(str2)).throwMessage("[22[1-100]a]");

        String str3 = StringConstantUtil.substringByPreSuf("https://www.baidu.com/img/2221-100]aa", StringConstantUtil.LEFT_MIDDLE_BRACKET, StringConstantUtil.RIGHT_MIDDLE_BRACKET);
        ThrowExceptionFunctionUtil.isFalseThrow(str3 == null).throwMessage("null");

        String str4 = StringConstantUtil.substringByPreSuf("https://www.baidu.com/img/222[1-100aa", StringConstantUtil.LEFT_MIDDLE_BRACKET, StringConstantUtil.RIGHT_MIDDLE_BRACKET);
        ThrowExceptionFunctionUtil.isFalseThrow(str4 == null).throwMessage("null");


        String str5 = StringConstantUtil.substringByPreSuf("https://www.baidu.com/img/222[1-100aa", StringConstantUtil.LEFT_MIDDLE_BRACKET, StringConstantUtil.RIGHT_MIDDLE_BRACKET);
        ThrowExceptionFunctionUtil.isFalseThrow(str5 == null).throwMessage("null");

        String str6 = StringConstantUtil.substringByPreSuf("https://www.baidu.com/img/222[1-100]]aa", StringConstantUtil.LEFT_MIDDLE_BRACKET, StringConstantUtil.RIGHT_MIDDLE_BRACKET);
        ThrowExceptionFunctionUtil.isFalseThrow("[1-100]]".equals(str6)).throwMessage("[1-100]]");

        String str7 = StringConstantUtil.substringByPreSuf("https://www.baidu.com/img/222[1-100]", StringConstantUtil.LEFT_MIDDLE_BRACKET, StringConstantUtil.RIGHT_MIDDLE_BRACKET);
        ThrowExceptionFunctionUtil.isFalseThrow("[1-100]".equals(str6)).throwMessage("[1-100]");

        System.out.println("str1 = " + str1);
        System.out.println("str2 = " + str2);
        System.out.println("str3 = " + str3);
        System.out.println("str4 = " + str4);
        System.out.println("str5 = " + str5);
        System.out.println("str6 = " + str6);
        System.out.println("str7 = " + str7);

    }



}
