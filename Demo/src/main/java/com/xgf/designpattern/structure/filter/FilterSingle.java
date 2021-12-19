package com.xgf.designpattern.structure.filter;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2021-12-11 14:55
 * @description 过滤婚姻状态 - 未婚
 **/

@Component("filterSingle")
public class FilterSingle implements FilterCondition {
    @Override
    public List<Person> executeFilter(List<Person> personList) {
        return personList.stream().filter(Objects::nonNull).filter(p -> p.getMaritalStatus() == 0).collect(Collectors.toList());
    }
}
