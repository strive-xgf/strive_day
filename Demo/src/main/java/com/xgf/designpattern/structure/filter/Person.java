package com.xgf.designpattern.structure.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xgf
 * @create 2021-12-11 14:34
 * @description 待过滤 Person 对象
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private String name;
    private Integer age;
    /**
     * 性别 ： 0 女性， 1 男性， -1 隐藏， 其它 : 未知
     */
    private Integer gender;
    /**
     * 婚姻情况： 0 未婚， 1 已婚， -1 离异， 其它 ： 未知
     */
    private Integer maritalStatus;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder().append("name = ").append(name)
                .append(", age = ").append(age)
                .append(", gender = ").append(gender == 0 ? "女性" : gender == 1 ? "男性" : gender == -1 ? "隐藏" : "未知")
                .append(", maritalStatus = ").append(maritalStatus == 0 ? "未婚" : maritalStatus == 1 ? "已婚" : maritalStatus == -1 ? "离异" : "未知")
                ;
        return sb.toString();
    }

}
