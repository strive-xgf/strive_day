package com.xgf.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xgf
 * @create 2021-10-26 11:55
 * @description
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String userUuid;
    private String passWord;
    private String phoneNum;
    private Hobby hobby;
    private Integer age;
    private Double balance;
    private Double balance2;
    private BigDecimal bigDecimal;
    private Date createdTime;
    private Date updatedTime;
    private WorkInfo workInfo;
    private List<String> stringList;
    private List<WorkInfo> workInfoList;
}
