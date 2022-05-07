package com.xgf.util.check.validate;

import com.xgf.check.validate.CommonCheckNumberData;
import com.xgf.constant.CommonNullConstantUtil;
import com.xgf.exception.CustomException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;


/**
 * @author xgf
 * @create 2022-05-07 00:32
 * @description
 **/

public class CommonCheckNumberDataTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void testAssertThrows() {
        Assertions.assertThrows(CustomException.class, () -> mockCheck("我是校验值").checkDate(), "抛出异常不是CustomException");
    }

    @Test
    public void no_rule() {
        // 无规则且数值类型自动校验通过
        mockCheck(CommonNullConstantUtil.BIG_DECIMAL_SPECIAL_NULL_VALUE).checkDate();
        mockCheck(88.88D).checkDate();;
        mockCheck(null).checkDate();;
    }

    @Test
    public void no_rule_illegalNumber_ThenThrow() {
        thrown.expectMessage("赋值非数值类型: 我是校验值");
        // 无规则非数值类型抛出异常
        mockCheck("我是校验值").checkDate();
    }

    @Test
    public void check_SpecialValue() {
        // 特殊值校验通过
        mockCheck(CommonNullConstantUtil.BIG_DECIMAL_SPECIAL_NULL_VALUE, 3, true).checkDate();;
        mockCheck(CommonNullConstantUtil.INT_SPECIAL_NULL_VALUE, 3, true).checkDate();;
        mockCheck(CommonNullConstantUtil.DOUBLE_SPECIAL_NULL_VALUE, 3, true).checkDate();;
        mockCheck(CommonNullConstantUtil.LONG_SPECIAL_NULL_VALUE, 3, true).checkDate();;
    }

    @Test
    public void check_min_noNumber_ThenThrow() {
        String min = "min123";
        // 最小值赋值非数字类型
        thrown.expectMessage("最小值赋值非数值类型: " + min);
        mockCheck("99.99", min, "222").checkDate();;
    }

    @Test
    public void check_max_noNumber_ThenThrow() {
        String max = "max678";
        // 最大值赋值非数字类型
        thrown.expectMessage("最大值赋值非数值类型: " + max);
        mockCheck("99.99", "1", max).checkDate();;
    }


    @Test
    public void check_lessThanMin_ThenThrow() {
        double min = 1.68;
        // 小于最小值
        thrown.expectMessage("不能小于 " + min);
        mockCheck(min - 1, String.valueOf(min), "888.88").checkDate();;
    }

    @Test
    public void check_greaterThanMax_ThenThrow() {
        double max = 99.99;
        // 大于最大值
        thrown.expectMessage("不能大于 " + max);
        mockCheck(max + 1, "0", String.valueOf(max)).checkDate();;
    }

    @Test
    public void check_greaterThanMax_fieldDesc_ThenThrow() {
        double max = 999.99;
        // 大于最大值（加字段）
        thrown.expectMessage("房子层高不能大于 " + max);
        mockCheck(max + 1, "0", String.valueOf(max), "房子层高").checkDate();;
    }

    @Test
    public void check_greaterScale_ThenThrow() {
        BigDecimal bigDecimal = new BigDecimal("999999.99999999");
        int scale = 6;
        // 小数位大于指定值
        thrown.expectMessage("小数位不能大于 " + 6 + " 位, 当前 " + bigDecimal.scale() + " 位");
        mockCheck(999999.99999999, scale, Boolean.TRUE).checkDate();;
    }

    @Test
    public void check_scale() {
        BigDecimal bigDecimal = new BigDecimal("999999.99999999");
        mockCheck(999999.99999999, bigDecimal.scale() + 1, Boolean.TRUE).checkDate();;
    }




    private CommonCheckNumberData mockCheck(Object o) {
        return mockCheck(o, null, null, null, null, null);
    }

    private CommonCheckNumberData mockCheck(Object o, Integer scale, Boolean dealSpecialValueFlag) {
        return mockCheck(o, null, null, scale, dealSpecialValueFlag, null);
    }

    private CommonCheckNumberData mockCheck(Object o, String min, String max) {
        return mockCheck(o, min, max, null, null, null);
    }

    private CommonCheckNumberData mockCheck(Object o, String min, String max, String fieldDescription) {
        return mockCheck(o, min, max, null, null, fieldDescription);
    }

    private CommonCheckNumberData mockCheck(Object o, String min, String max, Integer scale, Boolean dealSpecialValueFlag) {
        return mockCheck(o, min, max, scale, dealSpecialValueFlag, null);
    }

        private CommonCheckNumberData mockCheck(Object o, String min, String max, Integer scale, Boolean dealSpecialValueFlag, String fieldDescription) {
        CommonCheckNumberData check = new CommonCheckNumberData();
        check.setFiledValue(o);
        check.setMin(min);
        check.setMax(max);
        check.setScale(scale);
        check.setDealSpecialValueFlag(dealSpecialValueFlag);
        check.setFieldDescription(fieldDescription);
        return check;
    }

}
