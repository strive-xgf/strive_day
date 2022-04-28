package com.xgf.mvc.service;

import com.xgf.bean.User;
import com.xgf.constant.page.CommonPage;
import com.xgf.mvc.bean.req.SearchUserReqDTO;

import java.util.List;

/**
 * @author xgf
 * @create 2022-04-25 14:09
 * @description service
 **/

public interface UserService {

    /**
     * 条件分页搜索用户信息
     * @param userCondition 搜索条件
     * @param pageCommon 分页信息
     * @return userList
     */
    List<User> searchUserByPage(SearchUserReqDTO.UserCondition userCondition, CommonPage pageCommon);

}
