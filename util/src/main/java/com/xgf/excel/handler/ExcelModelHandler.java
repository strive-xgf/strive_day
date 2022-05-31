package com.xgf.excel.handler;

import com.xgf.excel.bean.ExcelHandlerParam;
import com.xgf.exception.CustomExceptionEnum;

import java.util.Objects;

/**
 * @author xgf
 * @create 2022-05-17 01:26
 * @description excel 的类型 Class<?> modelClz 处理器
 **/

public class ExcelModelHandler implements ExcelHandler {

    @Override
    public void check(ExcelHandlerParam param) {
        if (Objects.isNull(param)) {
            throw CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION.generateException();
        }

        if (Objects.isNull(param.getModelClz())) {
            throw CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION.generateException("modelClz is null");
        }
    }

}
