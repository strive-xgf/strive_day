package com.xgf.designpattern.structure.filter;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2021-12-11 14:49
 * @description 性别过滤器 - 男性
 **/

@Component("filterMale")
public class FilterMale implements FilterCondition {
    @Override
    public List<Person> executeFilter(List<Person> personList) {
        return personList.stream().filter(Objects::nonNull).filter(p -> p.getGender() == 1).collect(Collectors.toList());
    }
}
