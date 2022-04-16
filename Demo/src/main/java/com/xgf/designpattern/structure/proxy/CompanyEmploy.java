package com.xgf.designpattern.structure.proxy;


import java.util.List;

/**
 * @author xgf
 * @create 2022-01-10 00:10
 * @description 公司雇佣接口
 **/
public interface CompanyEmploy {

    /**
     * 雇佣员工
     *
     * @param condition 条件
     * @return 满足条件人
     */
    public List<Employee> employPeople(String condition);

}
