package com.xgf.util.check.validate;

import com.xgf.check.CompareOperatorEnum;
import com.xgf.check.validate.CommonCheckCompareData;
import com.xgf.constant.CommonNullConstantUtil;
import com.xgf.constant.DataEntry;
import com.xgf.reflect.CommonReflectUtil;
import lombok.Data;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-05-09 02:18
 * @description 比较数据测试类
 **/

public class CommonCheckCompareDataTest {

    @Data
    static class CompareDataObject<T> {
        private T a;
        private T b;

        public static <T> CompareDataObject<T> valueOf(T a, T b) {
            CompareDataObject<T> result = new CompareDataObject<T>();
            result.setA(a);
            result.setB(b);
            return result;
        }
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_null_value() {
        // 空值不校验
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.setOperatorEnum(CompareOperatorEnum.EQ);
        data.setValueEntry(DataEntry.valueOf(null,  null));
        data.checkData();

        data.setValueEntry(DataEntry.valueOf(22, null));
        data.checkData();

        data.setValueEntry(DataEntry.valueOf(null, 66.66d));
        data.checkData();

    }

    @Test
    public void test_nullField_value() {
        // 空值不校验
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.setOperatorEnum(CompareOperatorEnum.EQ);
        data.setColumnEntry(DataEntry.valueOf("a",  "b"));
        data.setCheckObject(CompareDataObject.valueOf(null, null));
        data.checkData();

    }

    @Test
    public void test_special_Value() {
        // 特殊值 不校验
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.setOperatorEnum(CompareOperatorEnum.EQ);
        data.setDealSpecialValueFlag(Boolean.TRUE);

        data.setValueEntry(DataEntry.valueOf(CommonNullConstantUtil.INT_SPECIAL_NULL_VALUE, 22));
        data.checkData();

        data.setValueEntry(DataEntry.valueOf(CommonNullConstantUtil.DATE_SPECIAL_NULL_VALUE, new Date()));
        data.checkData();

        data.setValueEntry(DataEntry.valueOf(CommonNullConstantUtil.DOUBLE_SPECIAL_NULL_VALUE, 66d));
        data.checkData();

        data.setValueEntry(DataEntry.valueOf("hello", CommonNullConstantUtil.LONG_SPECIAL_NULL_VALUE));
        data.checkData();

        data.setValueEntry(DataEntry.valueOf(CommonNullConstantUtil.SHORT_SPECIAL_NULL_VALUE, 88d));
        data.checkData();

    }

    @Test
    public void test_nullEntry_ThenThrow() {
        thrown.expectMessage("参与比较字段不存在");

        // columnEntry == null 且 valueEntry == null
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.checkData();
    }


    @Test
    public void test_nullOperatorEnum_ThenThrow() {
        thrown.expectMessage("比较运算符不能为空");

        // 比较运算符为空
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.setColumnEntry(DataEntry.valueOf(null, null));
        data.checkData();
    }

    @Test
    public void test_nullCheckObject_ThenThrow() {
        thrown.expectMessage("column值存在，校验对象不能为空");

        // columnEntry 存在，反射获取对象值，目标对象不能为空
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.setColumnEntry(DataEntry.valueOf(null, null));
        data.setOperatorEnum(CompareOperatorEnum.GTE);
        data.checkData();
    }

    @Test
    public void test_checkObjectNoExist_ThenThrow() {
        thrown.expectMessage("参与比较字段不存在");

        // columnEntry 存在，反射获取对象值，校验字段不存在
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.setColumnEntry(DataEntry.valueOf("a", "c"));
        data.setOperatorEnum(CompareOperatorEnum.GTE);
        data.setCheckObject(CompareDataObject.valueOf("a", 2));
        data.checkData();
    }

    @Test
    public void test_checkDataGTE_ThenThrow() {

        // 大于等于
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.setColumnEntry(DataEntry.valueOf("a", "b"));
        data.setOperatorEnum(CompareOperatorEnum.GTE);
        data.setCheckObject(CompareDataObject.valueOf(22, 30));

        thrown.expectMessage("不满足条件: 22 >= 30");
        data.checkData();
    }


    @Test
    public void test_checkDataEQ_ThenThrow() {

        // 等于
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.setColumnEntry(DataEntry.valueOf("a", "b"));
        data.setOperatorEnum(CompareOperatorEnum.EQ);
        data.setCheckObject(CompareDataObject.valueOf(30, 66));

        thrown.expectMessage("不满足条件: 30 = 66");
        data.checkData();
    }

    @Test
    public void test_checkDataLTE() {

        // 小于等于
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.setColumnEntry(DataEntry.valueOf("a", "b"));
        data.setOperatorEnum(CompareOperatorEnum.LTE);
        data.setCheckObject(CompareDataObject.valueOf(30, 66));

        data.checkData();
    }

//    @Test
//    public void test_checkDataTypeDiff_ThenThrow() {
//
//        // 校验的两个字段类型不一样（eg:校验值的类型不一致: java.lang.Integer  <>  java.math.BigDecimal）
//        CommonCheckCompareData data = new CommonCheckCompareData();
//        data.setColumnEntry(DataEntry.valueOf("a", "b"));
//        data.setOperatorEnum(CompareOperatorEnum.NEQ);
//        CompareDataObject<? extends Number> compareDataObject = CompareDataObject.valueOf(30, new BigDecimal("30"));
//        data.setCheckObject(compareDataObject);
//
//        thrown.expectMessage("校验值的类型不一致: " + Objects.requireNonNull(CommonReflectUtil.getFieldValue(compareDataObject, "a")).getClass().getName()
//                + "  <>  " + Objects.requireNonNull(CommonReflectUtil.getFieldValue(compareDataObject, "b")).getClass().getName());
//        data.checkData();
//    }


    @Test
    public void test_checkDataTypeDiffEQ() {

        // 校验的两个字段类型不一样但是值一样的情况
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.setColumnEntry(DataEntry.valueOf("a", "b"));
        data.setOperatorEnum(CompareOperatorEnum.EQ);
        CompareDataObject<? extends Number> compareDataObject = CompareDataObject.valueOf(30.678, new BigDecimal("30.678"));
        data.setCheckObject(compareDataObject);

        data.checkData();
    }

    @Test
    public void test_checkDataTypeDiffNEQ_ThenThrow() {

        // 校验的两个字段类型不一样但是值一样的情况
        CommonCheckCompareData data = new CommonCheckCompareData();
        data.setColumnEntry(DataEntry.valueOf("a", "b"));
        data.setOperatorEnum(CompareOperatorEnum.NEQ);
        CompareDataObject<? extends Serializable> compareDataObject = CompareDataObject.valueOf("6666666666.9898989898", new BigDecimal("6666666666.9898989898"));
        data.setCheckObject(compareDataObject);

        thrown.expectMessage("不满足条件: 6666666666.9898989898 != 6666666666.9898989898");
        data.checkData();
    }


}
