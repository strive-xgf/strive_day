package com.xgf.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author xgf
 * @create 2021-11-04 14:40
 * @description
 **/

@Data
public class Person {
    private String personUuid;
    private String passWord;
    private String phoneNum;
    private Date createdTime;
    private Date updatedTime;
    private Integer id;
    private Integer age;
    private Hobby hobby;
}
