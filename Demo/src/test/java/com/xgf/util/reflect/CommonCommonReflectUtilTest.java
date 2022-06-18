package com.xgf.util.reflect;

import com.alibaba.fastjson.JSON;
import com.xgf.bean.User;
import com.xgf.create.CreateRandomObjectUtil;
import com.xgf.reflect.CommonReflectUtil;
import lombok.Data;
import org.apache.commons.collections.MapUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author xgf
 * @create 2022-05-08 21:10
 * @description 反射测试
 **/

public class CommonCommonReflectUtilTest {


    @Test
    public void testGetField() {
        RC ra = new RA();
        System.out.println("ra = " + JSON.toJSONString(ra));

        System.out.println("ra aa = " + CommonReflectUtil.getField(ra, "aa"));;
        System.out.println("ra bb = " + CommonReflectUtil.getField(ra, "bb"));;
        System.out.println("ra cc = " + CommonReflectUtil.getField(ra, "cc"));;
        System.out.println("ra dd = " + CommonReflectUtil.getField(ra, "dd"));;

        assertNotNull(CommonReflectUtil.getFieldValue(ra, "aa"));
        assertNotNull(CommonReflectUtil.getFieldValue(ra, "bb"));
        assertNotNull(CommonReflectUtil.getFieldValue(ra, "cc"));
        assertNull(CommonReflectUtil.getFieldValue(ra, "dd"));

    }

    @Test
    public void testGetAllField() {
        RC ra = new RA();
        List<Field> fieldList = CommonReflectUtil.getAllField(ra);
        System.out.println(JSON.toJSONString(fieldList.stream().map(Field::getName).collect(Collectors.toList())));
        System.out.println(JSON.toJSONString(fieldList.stream().map(Field::getName).distinct().collect(Collectors.toList())));

    }

    @Test
    public void testGetAllFieldByNameDistinct() {

        List<Field> allField = CommonReflectUtil.getAllField(RA.class);
        System.out.println(allField.stream().map(Field::getName).collect(Collectors.toList()) + " >>> " + allField);
        System.out.println("\n>>>>>>>>>");
        List<Field> distinctFieldList = CommonReflectUtil.getAllFieldByNameDistinct(RA.class);
        System.out.println(distinctFieldList.stream().map(Field::getName).collect(Collectors.toList()) + " >>> " + distinctFieldList);

    }


    @Test
    public void testGetFieldValue() {
        RC ra = new RA();
        System.out.println("ra = " + JSON.toJSONString(ra));

        System.out.println("ra aa = " + CommonReflectUtil.getFieldValue(ra, "aa"));
        System.out.println("ra bb = " + CommonReflectUtil.getFieldValue(ra, "bb"));
        System.out.println("ra cc = " + CommonReflectUtil.getFieldValue(ra, "cc"));
        System.out.println("ra dd = " + CommonReflectUtil.getFieldValue(ra, "dd"));

        assertEquals("RA_aa", CommonReflectUtil.getFieldValue(ra, "aa"));
        assertEquals("RB_bb", CommonReflectUtil.getFieldValue(ra, "bb"));
        assertEquals("RC_cc", CommonReflectUtil.getFieldValue(ra, "cc"));
        assertNull(CommonReflectUtil.getFieldValue(ra, "dd"));

    }

    @Test
    public void testGetFieldValueReturnMap() {
        RC ra = new RA();

        Map<String, Object> fieldName2ValueMap = CommonReflectUtil.getFieldName2ValueMap(ra, Arrays.asList("aa", "bb", "cc", "dd", "ee", null));
        System.out.println("fieldName2ValueMap = " + JSON.toJSONString(fieldName2ValueMap));

        Map<String, Object> fieldName2ValueMap2 = CommonReflectUtil.getFieldName2ValueMap(ra, "aa", "bb", "cc", "dd", "ee", null);
        System.out.println("fieldName2ValueMap2 = " + JSON.toJSONString(fieldName2ValueMap2));

        Map<String, Object> fieldName2ValueMap3 = CommonReflectUtil.getFieldName2ValueMap(ra, Arrays.asList("dd", "ee", null));
        System.out.println("fieldName2ValueMap3 = " + JSON.toJSONString(fieldName2ValueMap3));
        System.out.println("fieldName2ValueMap3 is empty = " + MapUtils.isEmpty(fieldName2ValueMap3));

        DDD ddd = new DDD();
        Map<String, Object> fieldName2ValueMapDDD = CommonReflectUtil.getFieldName2ValueMap(ddd, Arrays.asList("aa", "bb", "cc", "dd", "ee", null));
        System.out.println("fieldName2ValueMapDDD = " + JSON.toJSONString(fieldName2ValueMapDDD));

    }

    @Test
    public void testSetFieldValue() {
        RC ra = new RA();
        System.out.println("ra = " + JSON.toJSONString(ra));

        CommonReflectUtil.setFieldValue(ra, "aa", "AA");
        CommonReflectUtil.setFieldValue(ra, "bb", "BB");
        CommonReflectUtil.setFieldValue(ra, "cc", "CC");
        CommonReflectUtil.setFieldValue(ra, "dd", "DD");
        System.out.println("set ra = " + JSON.toJSONString(ra));

        assertEquals("AA", ra.getAa());
        assertEquals("BB", ra.getBb());
        assertEquals("CC", ra.getCc());

    }

    @Test
    public void testGetAllFieldName2ValueMap() {
        User user = new User();
        Map<String, Object> map = CommonReflectUtil.getAllFieldName2ValueMap(user);
        System.out.println(map);

        User user2 = CreateRandomObjectUtil.createData(User.class);
        Map<String, Object> map2 = CommonReflectUtil.getAllFieldName2ValueMap(user2);
        System.out.println(map2);
    }




    @Data
    class DDD{}


    @Data
    static class RA extends RB{
        private String aa = "RA_aa";
    }

    @Data
    static class RB extends RC{
        private String aa = "RB_aa";
        private String bb = "RB_bb";
    }

    @Data
    static class RC{
        private String aa = "RC_aa";
        private String bb = "RC_bb";
        private String cc = "RC_cc";
    }


}
