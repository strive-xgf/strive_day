package com.xgf.designpattern.structure.filter;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2021-12-11 15:55
 * @description 过滤条件 or 组装
 **/

@Component("filterOrAssembly")
public class FilterOrAssembly {

    public List<Person> assemblyConditionExecute(List<Person> personList, FilterCondition... filterConditionList) {
        List<Person> resultList = new ArrayList<>();
        Arrays.stream(filterConditionList).forEach(e -> resultList.addAll(e.executeFilter(personList)));
        return resultList.stream().distinct().collect(Collectors.toList());
    }

}
