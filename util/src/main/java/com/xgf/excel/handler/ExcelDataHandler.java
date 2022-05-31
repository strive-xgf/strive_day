package com.xgf.excel.handler;

import com.xgf.excel.bean.ExcelHandlerParam;
import com.xgf.exception.CustomExceptionEnum;
import org.apache.commons.collections.CollectionUtils;

import java.util.Objects;

/**
 * @author xgf
 * @create 2022-05-17 01:29
 * @description excel 的数据 List<?> dataList 处理器
 **/

public class ExcelDataHandler implements ExcelHandler {

    @Override
    public void check(ExcelHandlerParam param) {
        if (Objects.isNull(param)) {
            throw CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION.generateException();
        }

        if (CollectionUtils.isEmpty(param.getDataList())) {
            return;
        }

        // 数据类型
        Class<?> modelClz = param.getModelClz();

        if (Objects.isNull(modelClz)) {
            return;
        }

        // 不为空需要校验，dataList 的数据类型是否和 modelClz 一致，不一致抛出异常
        long count = param.getDataList()
                .stream()
                .filter(Objects::nonNull)
                .filter(p -> !modelClz.getName().equals(p.getClass().getName()))
                .count();

        if (count > 0) {
            throw CustomExceptionEnum.PARAM_TYPE_ILLEGAL_EXCEPTION.generateException("dataList type with modelClz not same");
        }
    }

}
