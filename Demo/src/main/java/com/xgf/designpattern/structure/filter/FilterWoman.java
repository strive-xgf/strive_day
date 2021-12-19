package com.xgf.designpattern.structure.filter;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2021-12-11 14:51
 * @description 性别过滤器 - 女性
 **/

@Component("filterWoman")
public class FilterWoman implements FilterCondition {
    @Override
    public List<Person> executeFilter(List<Person> personList) {
        return personList.stream().filter(Objects::nonNull).filter(p -> p.getGender() == 0).collect(Collectors.toList());
    }
}
