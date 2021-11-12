package com.xgf.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xgf
 * @create 2021-11-10 00:34
 * @description
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkInfo {

    private String workUuid;
    private Date createdTime;
    private Date updatedTime;
    private String workContent;

}
