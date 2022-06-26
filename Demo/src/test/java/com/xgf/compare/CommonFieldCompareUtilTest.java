package com.xgf.compare;

import com.xgf.bean.User2;
import com.xgf.common.JsonUtil;
import com.xgf.create.CreateRandomObjectUtil;
import org.junit.Test;

import java.util.*;

/**
 * @author xgf
 * @create 2022-06-21 23:31
 * @description 数据字段比较工具类测试
 **/

public class CommonFieldCompareUtilTest {

    @Test
    public void test() {
        User2 user21 = CreateRandomObjectUtil.createData(User2.class);
        User2 user22 = CreateRandomObjectUtil.createData(User2.class);
        user21.setAge(null);
        user21.setUserUuid(null);
        user21.setStringList(Arrays.asList("1", "2", "8", "第一", "第二", "第三"));
        user22.setStringList(Arrays.asList("1", "2", "3", "第一", "第二", "第三"));

        System.out.println("user21 = " + JsonUtil.toJsonString(user21));
        System.out.println("user22 = " + JsonUtil.toJsonString(user22));


        Map<String, String> fieldName2DescMap = new HashMap<>();
        fieldName2DescMap.put("passWord", "密码");
        fieldName2DescMap.put("stringList", "字符串数组");
        fieldName2DescMap.put("age", "年龄");

        Set<String> excludeFieldSet = new HashSet<>(Arrays.asList("passWord", "age"));

        System.out.println("不过滤，不包含 = " + JsonUtil.toJsonString(CommonFieldCompareUtil.getFieldChangeInfo(user21, user22)));
        System.out.println("过滤，不包含 = " + JsonUtil.toJsonString(CommonFieldCompareUtil.getFieldChangeInfo(user21, user22, excludeFieldSet)));
        System.out.println("不过滤，包含 = " + JsonUtil.toJsonString(CommonFieldCompareUtil.getFieldChangeInfo(user21, user22, fieldName2DescMap)));
        System.out.println("过滤，包含 = " + JsonUtil.toJsonString(CommonFieldCompareUtil.getFieldChangeInfo(user21, user22, fieldName2DescMap, excludeFieldSet)));
    }

}
