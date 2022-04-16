package com.xgf.designpattern.structure.proxy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xgf
 * @create 2022-01-10 00:13
 * @description 员工接口
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    /**
     * 名称
     */
    private String employeeName;

    /**
     * 描述
     */
    private String desc;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 来源
     */
    private String source;

}
