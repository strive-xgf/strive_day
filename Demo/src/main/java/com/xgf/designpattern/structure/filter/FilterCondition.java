package com.xgf.designpattern.structure.filter;

import java.util.List;

/**
 * @author xgf
 * @create 2021-12-11 14:37
 * @description 过滤标准接口（过滤条件都实现该接口）
 **/
public interface FilterCondition {

    /**
     * 过滤列表
     * @param personList 入参person列表
     * @return 过滤后的结果
     */
    public List<Person> executeFilter(List<Person> personList);
}
