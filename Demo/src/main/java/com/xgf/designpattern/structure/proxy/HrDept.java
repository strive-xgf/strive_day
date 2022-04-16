package com.xgf.designpattern.structure.proxy;

import com.alibaba.fastjson.JSON;
import com.xgf.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author xgf
 * @create 2022-01-10 00:17
 * @description hr部门
 **/

@Slf4j
@Component("hrDept")
public class HrDept implements CompanyEmploy {

    @Resource(name = "xxxHeadHunter")
    private HeadHunterCompany xxxHeadHunter;

    @Override
    public List<Employee> employPeople(String condition) {
        // hr 找熟知的猎头公司招人 + 发招聘广告等等
        List<Employee> employeeList = Optional.ofNullable(xxxHeadHunter.queryPeopleByCondition(condition)).orElseGet(ArrayList::new);
        log.info("====== HrDept size = 【{}】", employeeList.size());
        return employeeList;
    }
}
