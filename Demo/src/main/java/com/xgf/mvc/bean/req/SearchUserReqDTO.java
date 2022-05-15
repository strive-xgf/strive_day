package com.xgf.mvc.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xgf
 * @create 2022-04-25 14:05
 * @description 搜索用户 请求DTO
 **/

@Data
@ApiModel(value = "搜索User信息")
public class SearchUserReqDTO {

    @ApiModelProperty("条件")
    private UserCondition userCondition;

    @Data
    @ApiModel(value = "搜索User条件")
    public static class UserCondition {
        @ApiModelProperty("用户uuid搜索")
        private String userUuid;

        @ApiModelProperty("用户age搜索")
        private Integer age;
    }

}
