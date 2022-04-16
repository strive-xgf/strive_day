package com.xgf.designpattern.structure.proxy;


import java.util.List;

/**
 * @author xgf
 * @create 2022-01-10 00:24
 * @description 猎头公司对外提供接口 【抽象委托】
 **/


public interface HeadHunterCompany {

    /**
     * 根据条件搜索满足的人
     * @param condition 条件
     * @return 满足条件的用户
     */
    List<Employee> queryPeopleByCondition(String condition);

    /**
     * 获取来源名（重写）
     *
     * @return 来源类名
     */
    default String getSourceName() {
        return this.getClass().getName();
    }

}
