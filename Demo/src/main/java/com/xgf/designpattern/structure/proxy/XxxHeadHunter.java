package com.xgf.designpattern.structure.proxy;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2022-01-16 23:22
 * @description Xxx猎头公司【具体委托】
 **/

@Data
@Component("xxxHeadHunter")
public class XxxHeadHunter implements HeadHunterCompany {

    /**
     * 人才库
     */
    public static final List<Employee> EMPLOYEE_POOL = new ArrayList<>();

    static {
        EMPLOYEE_POOL.addAll(Arrays.asList(
                Employee.builder().employeeName("夏1").phone("111").desc("Java高级工程师").source("xxxHeadHunter").build(),
                Employee.builder().employeeName("赵2").phone("222").desc("Python工程师").source("xxxHeadHunter").build(),
                Employee.builder().employeeName("张3").phone("333").desc("Java架构师").source("xxxHeadHunter").build(),
                Employee.builder().employeeName("李4").phone("444").desc("Java实习生").source("xxxHeadHunter").build(),
                Employee.builder().employeeName("王5").phone("555").desc("Java讲师").source("xxxHeadHunter").build()
        ));
    }


    @Override
    public List<Employee> queryPeopleByCondition(String condition) {

        // 猎头公司（例如从自己的人才库中）匹配算法找符合条件的求职者
        return Optional.of(EMPLOYEE_POOL)
                .orElseGet(ArrayList::new)
                .stream()
                .filter(Objects::nonNull)
                .filter(p -> p.getDesc().contains(condition))
                .collect(Collectors.toList());
    }
}
