package com.xgf.util;

import com.alibaba.fastjson.JSON;
import com.xgf.bean.User;
import com.xgf.check.CheckCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xgf
 * @create 2022-04-22 11:19
 * @description
 **/

public class CheckUtilTest {

    @Test
    public void testCheckAllFieldsIsNull() {
        User user = new User();
//        user.setUserUuid("");

        if (CheckCommonUtil.checkAllFieldsIsNull(user)) {
            System.out.println("user is null");
        }

        List<User> userList = new ArrayList<>(Collections.nCopies(10, user));
        System.out.println("userList size = " + userList.size() + ", value = " + JSON.toJSONString(user));

        if (CollectionUtils.isEmpty(userList)) {
            System.out.println("CollectionUtils userList is null");

        }

        if (CheckCommonUtil.checkAllFieldsIsNull(userList)) {
            System.out.println("CheckCommonUtil userList is null");
        }

    }


}
