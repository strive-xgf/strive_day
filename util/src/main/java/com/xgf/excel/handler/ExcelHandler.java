package com.xgf.excel.handler;

import com.xgf.excel.bean.ExcelHandlerParam;

/**
 * @author xgf
 * @create 2022-05-17 01:22
 * @description 自定义 Excel Handler 处理器接口
 **/

public interface ExcelHandler {

    /**
     * 校验数据合法性
     *
     * @param param ExcelHandlerParam 参数
     */
     void check(ExcelHandlerParam param);

}
