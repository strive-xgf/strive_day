package com.xgf.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xgf
 * @create 2021-11-10 00:11
 * @description
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hobby {

    private String hobbyUuid;
    private String hobbyType;
    private String userUuid;
    private String hobbyContent;
    private Integer hobbyCount;
    private Date createdTime;
    private Date updatedTime;

}
