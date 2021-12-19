package com.xgf.designpattern.structure.filter;

import com.xgf.springbean.BeanCopyUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author xgf
 * @create 2021-12-11 15:14
 * @description 过滤条件 and 组装
 **/

@Component("filterAndAssembly")
public class FilterAndAssembly {

    public List<Person> assemblyConditionExecute(List<Person> personList, FilterCondition... filterConditionList) {

        List<Person> resultList = BeanCopyUtil.convertList2List(personList, Person.class);
        Arrays.stream(filterConditionList).forEach(e -> {
            resultList.retainAll(e.executeFilter(personList));
        });
        return resultList;
    }

}
