package com.xgf.util;

import com.alibaba.fastjson.JSON;
import com.xgf.bean.User;
import com.xgf.create.CreateRandomObjectUtil;
import org.junit.Test;

/**
 * @author xgf
 * @create 2021-11-10 00:08
 * @description
 **/

public class CreateRandomObjectUtilTest {

    @Test
    public void test(){
        User user1 = CreateRandomObjectUtil.createData(User.class);
        System.out.println("user1 = " + JSON.toJSONString(user1));
        User user2 = CreateRandomObjectUtil.createData(User.class, Boolean.FALSE);
        System.out.println("\nuser2 = " + JSON.toJSONString(user2));
        User user3 = CreateRandomObjectUtil.createData(User.class, "我是一个随机的字符串中文内容", 3);
        System.out.println("\nuser3 = " + JSON.toJSONString(user3));
        User user4 = CreateRandomObjectUtil.createData(User.class, Boolean.TRUE, "我是随机的字符串中文内容，我不创建嵌套对象。嘿嘿嘿", 5);
        System.out.println("\nuser4 = " + JSON.toJSONString(user4));

    }

}
