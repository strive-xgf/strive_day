package com.xgf.util;

import com.alibaba.fastjson.JSON;
import com.xgf.bean.Hobby;
import com.xgf.bean.User;
import com.xgf.bean.WorkInfo;
import com.xgf.constant.page.CommonPage;
import com.xgf.springbean.BeanCopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author xgf
 * @create 2022-04-27 21:55
 * @description 分页测试
 **/

@Slf4j
public class CommonPageTest {

    public static List<User> userList;
    public static User user;
    // 模拟数据
    static {
        user = User.builder()
                .userUuid("user1")
                .passWord("123")
                .age(18)
                .balance(999999D)
                .hobby(Hobby.builder().hobbyType("hobbyType").hobbyCount(12).createdTime(new Date()).hobbyUuid("hobbyUuid").build())
                .createdTime(new Date())
                .bigDecimal(new BigDecimal("9999"))
                .stringList(Arrays.asList("1", "2", "3", "4", "5", "6"))
                .workInfoList(Arrays.asList(WorkInfo.builder().workUuid("workUuid1").workContent("workContent1").build(),
                        WorkInfo.builder().workUuid("workUuid2").workContent("workContent2").build()))
                .build();
        userList = Arrays.asList(
                user,
                User.builder().userUuid("user2").age(18).balance(1D).build(),
                User.builder().userUuid("user2").age(20).balance(1D).build(),
                User.builder().userUuid("user2").age(18).balance(2D).build(),
                User.builder().userUuid("user2").age(19).balance(10D).build(),
                User.builder().userUuid("user2").age(18).balance(1D).build(),
                User.builder().userUuid("user6").age(18).balance(11D).build(),
                User.builder().userUuid("user7").age(18).balance(1D).build(),
                User.builder().userUuid("user8").age(16).balance(1D).build(),
                User.builder().userUuid("user9").age(18).balance(10D).build()
        );
    }


    /**
     * mock 对象，复制 userList
     * @return userList 的复制对象
     */
    private List<User> mockUserList() {
        return BeanCopyUtil.convertList2List(userList, User.class);
    }

    @Test
    public void test() {
        CommonPage num2Size8 = CommonPage.valueOf(2, 8);
        List<User> num2Size8Result = num2Size8.subDataListByPage(mockUserList());
        log.info("pageNumber = 2, pageSize = 8 >>>>>> result size = 【{}】, value= 【{}】\n", num2Size8Result.size(), JSON.toJSONString(num2Size8Result));

        num2Size8.setPageSkip(1);
        List<User> num2Size8Skip1Result = num2Size8.subDataListByPage(mockUserList());
        log.info("pageNumber = 2, pageSize = 8 >>>>>> result size = 【{}】, value= 【{}】\n", num2Size8Skip1Result.size(), JSON.toJSONString(num2Size8Skip1Result));

        CommonPage offset5Size8 = new CommonPage();
        offset5Size8.setPageSize(8);
        offset5Size8.setOffset(5);
        List<User> offset5Size8Result = num2Size8.subDataListByPage(mockUserList());
        log.info("offset = 5, pageSize = 8 >>>>>> result size = 【{}】, value= 【{}】\n", offset5Size8Result.size(), JSON.toJSONString(offset5Size8Result));
    }



}
