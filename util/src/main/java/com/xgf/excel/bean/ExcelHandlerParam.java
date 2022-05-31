package com.xgf.excel.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xgf
 * @create 2022-05-17 01:25
 * @description excel 处理器参数信息
 **/

@Data
@NoArgsConstructor
public class ExcelHandlerParam {

    /**
     * 数据集合
     */
    private List<?> dataList;

    /**
     * 数据类型
     */
    private Class<?> modelClz;

    /**
     * 数据量大小限制
     */
    private Integer dataSizeLimit;


    public static ExcelHandlerParam valueOf(List<?> dataList, Class<?> modelClz, int dataSizeLimit) {

        ExcelHandlerParam param = new ExcelHandlerParam();
        param.setDataList(dataList);
        param.setModelClz(modelClz);
        param.setDataSizeLimit(dataSizeLimit);

        return param;
    }

}
