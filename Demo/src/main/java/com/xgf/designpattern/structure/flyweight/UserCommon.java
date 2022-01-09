package com.xgf.designpattern.structure.flyweight;

import com.xgf.bean.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-01-08 16:34
 * @description User 通用字段 - 存缓存里面 {@linkplain User}
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCommon {

    /**
     * token 默认前缀
     */
    private static final String DEFAULT_TOKEN_PREFIX = "token";

    /**
     * token 默认后缀
     */
    private static final String DEFAULT_TOKEN_SUFFIX = "userCommon";

    /**
     * token 间隔符
     */
    private static final String DEFAULT_SPACE_MARK = "-";


    /**
     * 用户uuid
     */
    private String userUuid;

    /**
     * 部门uuid
     */
    private String deptUuid;

    /**
     * 用户所属分区/公司
     */
    private String companyUuid;

    /**
     * 用户token
     */
    private String userToken;

    /**
     * 是否被使用
     */
    private Boolean useFlag = Boolean.FALSE;

    /**
     * 使用 Ip 地址
     */
    private String useIpAddress;

    /**
     * 被使用时间
     */
    private Date useTime;

    public void generateUserToken() {

        this.userToken = addSpaceMark(DEFAULT_TOKEN_PREFIX)
                + addSpaceMark(companyUuid)
                + addSpaceMark(deptUuid)
                + addSpaceMark(userUuid)
                + addSpaceMark(userUuid)
                + addSpaceMark(useFlag)
                + addSpaceMark(Objects.isNull(useTime) ? System.currentTimeMillis() : useTime.getTime())
                + DEFAULT_TOKEN_SUFFIX
                ;
    }

    private String addSpaceMark(Object param) {
        return param + DEFAULT_SPACE_MARK;
    }

}
