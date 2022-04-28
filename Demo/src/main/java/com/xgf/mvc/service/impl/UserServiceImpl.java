package com.xgf.mvc.service.impl;

import com.alibaba.fastjson.JSON;
import com.xgf.bean.Hobby;
import com.xgf.bean.User;
import com.xgf.bean.WorkInfo;
import com.xgf.constant.page.CommonPage;
import com.xgf.mvc.bean.req.SearchUserReqDTO;
import com.xgf.mvc.service.UserService;
import com.xgf.springbean.BeanCopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2022-04-25 14:09
 * @description userService 实现
 **/

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

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


    @Override
    public List<User> searchUserByPage(SearchUserReqDTO.UserCondition userCondition, CommonPage page) {
        log.info("====== searchUserByPage in param userCondition = 【{}】, pageComon = 【{}】",
                JSON.toJSONString(userCondition), JSON.toJSONString(page));
        if (Objects.isNull(page)) {
            page = new CommonPage();
        }

        // 模拟对象（复制）
        List<User> userList = mockUserList();

        // 按条件过滤
        if (Objects.nonNull(userCondition)) {
            userList = Optional.of(userList).orElseGet(ArrayList::new).stream().filter(u -> {
                if (StringUtils.isNotBlank(userCondition.getUserUuid()) && !userCondition.getUserUuid().equals(u.getUserUuid())) {
                    return false;
                }
                if (Objects.nonNull(userCondition.getAge()) && !(userCondition.getAge().equals(u.getAge()))) {
                    return false;
                }
                return true;

            }).collect(Collectors.toList());
        }

        // 给数据预排序， 先按userUuid升序，再按age升序，最后balance降序
        userList.sort(
                Comparator.comparing(User::getUserUuid, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(User::getAge, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(User::getBalance, Comparator.nullsLast(Comparator.reverseOrder()))
        );


        userList = page.subDataListByPage(userList);

//        userList = page.subDataListByOffset(userList);


        log.info("====== searchUserByPage sort result, size = 【{}】, value = 【{}】", userList.size(), JSON.toJSONString(userList));

        return userList;
    }


}
