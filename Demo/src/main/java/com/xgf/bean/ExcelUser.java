package com.xgf.bean;

import com.xgf.excel.aspect.ExcelFieldAnnotation;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xgf
 * @create 2022-05-18 00:21
 * @description excel 导出类测试
 **/

@Data
public class ExcelUser {

    @ExcelFieldAnnotation(name = "用户uuid", order = 1)
    private String userUuid;

    @ExcelFieldAnnotation(name = "用户密码", hidden = true)
    private String passWord;

    @ExcelFieldAnnotation(name = "手机号", order = 2)
    private String phoneNum;

    @ExcelFieldAnnotation(name = "年龄", order = 10)
    private Integer age;

    @ExcelFieldAnnotation(name = "余额", order = 2)
    private Double balance;

    @ExcelFieldAnnotation(name = "bigDecimal数", order = 3)
    private BigDecimal bigDecimal;

    @ExcelFieldAnnotation(name = "创建时间", order = 1)
    private Date createdTime;

    @ExcelFieldAnnotation(name = "更新时间", order = 1)
    private Date updatedTime;

    @ExcelFieldAnnotation(name = "爱好信息")
    private Hobby hobby;

    @ExcelFieldAnnotation(name = "工作信息")
    private WorkInfo workInfo;

    private List<String> stringList;

    private List<WorkInfo> workInfoList;

}
