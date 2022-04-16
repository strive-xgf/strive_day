package com.xgf.designpattern.structure.proxy.staticproxy;

import com.xgf.collection.AssemblyUtil;
import com.xgf.designpattern.structure.proxy.CompanyEmploy;
import com.xgf.designpattern.structure.proxy.Employee;
import com.xgf.designpattern.structure.proxy.HeadHunterCompany;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2022-01-17 00:27
 * @description 雇佣员工
 *  静态代理 : 被代理对象与代理对象一起实现相同的接口或者是继承相同父类
 *  需要显式的实现与目标对象类（RealSubject）相同的接口（定义接口或者父类），在程序运行前，代理类就已经创建好
 **/

@Component("staticProxyEmploy")
public class StaticProxyEmploy implements CompanyEmploy {

    @Resource(name = "hrDept")
    private CompanyEmploy hrDept;
    
    @Resource(name = "yyyHeadHunter")
    private HeadHunterCompany yyyHeadHunter;

    @Override
    public List<Employee> employPeople(String condition) {
        
        List<Employee> employeeList= hrDept.employPeople(condition);
        
        // 可在 前 / 后 附加条件加以控制
        // 从 yyy 猎头公司也找人
        Optional.ofNullable(employeeList).orElseGet(ArrayList::new)
                .addAll(yyyHeadHunter.queryPeopleByCondition(condition));

        // 去重（例如：按 姓名 和 电话号码 去重
        return Optional.ofNullable(employeeList).orElseGet(ArrayList::new)
                .stream()
                .filter(Objects::nonNull)
                .filter(AssemblyUtil.distinctByCondition(Employee::getEmployeeName, Employee::getPhone))
                .collect(Collectors.toList());
    }

}
