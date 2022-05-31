package com.xgf.excel.handler;

import com.xgf.excel.constant.ExcelConstant;
import com.xgf.excel.bean.ExcelHandlerParam;
import com.xgf.exception.CustomExceptionEnum;
import org.apache.commons.collections.CollectionUtils;

import java.util.Objects;

/**
 * @author xgf
 * @create 2022-05-17 01:36
 * @description excel 的数据大小限制 dataSizeLimit 处理器
 **/

public class ExcelDataSizeLimitHandler implements ExcelHandler {

    @Override
    public void check(ExcelHandlerParam param) {
        Integer dataSizeLimit = param.getDataSizeLimit();

        if (Objects.isNull(dataSizeLimit) || dataSizeLimit <= 0) {
            // 设置默认限制数
            dataSizeLimit = ExcelConstant.EXPORT_MAX_ROW_SIZE;
        }

        if (CollectionUtils.isNotEmpty(param.getDataList())
                && param.getDataList().size() > dataSizeLimit) {
            throw CustomExceptionEnum.PARAM_TYPE_ILLEGAL_EXCEPTION.generateException("data size over limit " + dataSizeLimit);
        }
    }

}
