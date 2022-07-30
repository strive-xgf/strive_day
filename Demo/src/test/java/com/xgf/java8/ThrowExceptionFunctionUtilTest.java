package com.xgf.java8;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author xgf
 * @create 2022-07-26 13:20
 * @description
 **/

public class ThrowExceptionFunctionUtilTest {


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testThrowExceptionFunctionIsTrueThrow() {
        String message = "为 true 抛出异常";
        thrown.expectMessage(message);
        ThrowExceptionFunctionUtil.isTureThrow(true).throwMessage(message);
    }

    @Test
    public void testThrowExceptionFunctionIsFalseThrow() {
        String message = "为 false 抛出异常";
        thrown.expectMessage(message);
        ThrowExceptionFunctionUtil.isFalseThrow(false).throwMessage(message);
    }

    @Test
    public void testThrowExceptionFunctionIsNullThrow() {
        String message = "为 null 抛出异常";
        thrown.expectMessage(message);
        ThrowExceptionFunctionUtil.isNullThrow(null).throwMessage(message);
    }

    @Test
    public void testThrowExceptionFunctionIsBlankThrow() {
        String message = "为 空/空字符串 抛出异常";
        thrown.expectMessage(message);
        ThrowExceptionFunctionUtil.isBlankThrow(null).throwMessage(message);
        ThrowExceptionFunctionUtil.isBlankThrow("      ").throwMessage(message);
    }

    @Test
    public void testThrowExceptionFunctionNoThrow() {
        String message = "异常不满足";
        ThrowExceptionFunctionUtil.isTureThrow(false).throwMessage(message);
        ThrowExceptionFunctionUtil.isTureThrow(null).throwMessage(message);
        ThrowExceptionFunctionUtil.isFalseThrow(true).throwMessage(message);
        ThrowExceptionFunctionUtil.isFalseThrow(null).throwMessage(message);
        ThrowExceptionFunctionUtil.isNullThrow(true).throwMessage(message);
        ThrowExceptionFunctionUtil.isNullThrow(false).throwMessage(message);
        ThrowExceptionFunctionUtil.isBlankThrow("str").throwMessage(message);
    }


}
