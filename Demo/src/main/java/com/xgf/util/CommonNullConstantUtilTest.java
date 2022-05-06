package com.xgf.util;

import com.xgf.bean.User;
import com.xgf.constant.CommonNullConstantUtil;
import com.xgf.date.DateUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xgf
 * @create 2022-05-05 22:56
 * @description 校验空值情况的处理
 **/

public class CommonNullConstantUtilTest {

    @Test
    public void testCheckSpecialValue() {

        Short nullShort = null;
        System.out.println("value = " + nullShort
                + "  >>> check is specialValue : " + CommonNullConstantUtil.checkSpecialValue(nullShort));

        Short aShort = -32768;
        System.out.println(aShort.getClass().getSimpleName() + ", value = " + aShort
                + "  >>> check is specialValue : " + CommonNullConstantUtil.checkSpecialValue(aShort));

        Integer aInt = -999999999;
        System.out.println(aInt.getClass().getSimpleName() + ", value = " + aInt
                + "  >>> check is specialValue : " + CommonNullConstantUtil.checkSpecialValue(aInt));

        Long aLong = -999999999L;
        System.out.println(aLong.getClass().getSimpleName() + ", value = " + aLong
                + "  >>> check is specialValue : " + CommonNullConstantUtil.checkSpecialValue(aLong));

        Float aFloat = -999999999.999999999F;
        System.out.println(aFloat.getClass().getSimpleName() + ", value = " + aFloat
                + "  >>> check is specialValue : " + CommonNullConstantUtil.checkSpecialValue(aFloat));

        Double aDouble = -999999999.999999999D;
        System.out.println(aDouble.getClass().getSimpleName() + ", value = " + aDouble
                + "  >>> check is specialValue : " + CommonNullConstantUtil.checkSpecialValue(aDouble));

        BigDecimal aBigDecimal = new BigDecimal("-999999999.999999999");
        System.out.println(aBigDecimal.getClass().getSimpleName() + ", value = "
                + aBigDecimal + "  >>> check is specialValue : " + CommonNullConstantUtil.checkSpecialValue(aBigDecimal));

        Date aDate = DateUtil.getDateNullInstance();
        assert aDate != null;
        System.out.println(aDate.getClass().getSimpleName() + ", value = " + DateUtil.dateFormatString(aDate, DateUtil.FORMAT_MILL)
                + "  >>> check is specialValue : " + CommonNullConstantUtil.checkSpecialValue(aDate));

    }


    @Test
    public void testConvertNullBySpecialValue() {
        Short nullShort = null;
        System.out.println("value = " + nullShort
                + "  >>> specialValue convert : " + CommonNullConstantUtil.convertNullBySpecialValue(nullShort));

        Short aShort = -32768;
        System.out.println(aShort.getClass().getSimpleName() + ", value = " + aShort
                + "  >>> specialValue convert : " + CommonNullConstantUtil.convertNullBySpecialValue(aShort));

        Integer aInt = -999999999;
        System.out.println(aInt.getClass().getSimpleName() + ", value = " + aInt
                + "  >>> specialValue convert : " + CommonNullConstantUtil.convertNullBySpecialValue(aInt));

        Long aLong = -999999999L;
        System.out.println(aLong.getClass().getSimpleName() + ", value = " + aLong
                + "  >>> specialValue convert : " + CommonNullConstantUtil.convertNullBySpecialValue(aLong));

        Float aFloat = -999999999.999999999F;
        System.out.println(aFloat.getClass().getSimpleName() + ", value = " + aFloat
                + "  >>> specialValue convert : " + CommonNullConstantUtil.convertNullBySpecialValue(aFloat));

        Double aDouble = -999999999.999999999D;
        System.out.println(aDouble.getClass().getSimpleName() + ", value = " + aDouble
                + "  >>> specialValue convert : " + CommonNullConstantUtil.convertNullBySpecialValue(aDouble));

        BigDecimal aBigDecimal = new BigDecimal("-999999999.999999999");
        System.out.println(aBigDecimal.getClass().getSimpleName() + ", value = "
                + aBigDecimal + "  >>> specialValue convert : " + CommonNullConstantUtil.convertNullBySpecialValue(aBigDecimal));

        Date aDate = DateUtil.getDateNullInstance();
        assert aDate != null;
        System.out.println(aDate.getClass().getSimpleName() + ", value = " + DateUtil.dateFormatString(aDate, DateUtil.FORMAT_MILL)
                + "  >>> specialValue convert : " + CommonNullConstantUtil.convertNullBySpecialValue(aDate));
    }


    @Test
    public void testGetSpecialValueByClassType() {
        
        System.out.println("null >>> get specialValue : " + CommonNullConstantUtil.getSpecialValueByClassType(null));
        System.out.println("other User >>> get specialValue : " + CommonNullConstantUtil.getSpecialValueByClassType(User.class));
        System.out.println(Short.class.getSimpleName() + "  >>> get specialValue : " + CommonNullConstantUtil.getSpecialValueByClassType(Short.class));
        System.out.println(Integer.class.getSimpleName() + "  >>> get specialValue : " + CommonNullConstantUtil.getSpecialValueByClassType(Integer.class));
        System.out.println(Long.class.getSimpleName() + "  >>> get specialValue : " + CommonNullConstantUtil.getSpecialValueByClassType(Long.class));
        System.out.println(Float.class.getSimpleName() + "  >>> get specialValue : " + CommonNullConstantUtil.getSpecialValueByClassType(Float.class));
        System.out.println(Double.class.getSimpleName() + "  >>> get specialValue : " + CommonNullConstantUtil.getSpecialValueByClassType(Double.class));
        System.out.println(BigDecimal.class.getSimpleName() + "  >>> get specialValue : " + CommonNullConstantUtil.getSpecialValueByClassType(BigDecimal.class));
        System.out.println(Date.class.getSimpleName() + "  >>> get specialValue : " + CommonNullConstantUtil.getSpecialValueByClassType(Date.class));

    }

}
