package com.xgf.excel.bean;

import com.xgf.excel.constant.ExcelConstant;
import com.xgf.excel.handler.ExcelDataHandler;
import com.xgf.excel.handler.ExcelDataSizeLimitHandler;
import com.xgf.excel.handler.ExcelHandler;
import com.xgf.excel.handler.ExcelModelHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.*;

/**
 * @author xgf
 * @create 2022-05-17 19:05
 * @description excel 数据参数
 **/

@Data
@NoArgsConstructor
public class ExcelDataParam {

    /**
     * excel 对象 Workbook 相关配置初始化对象
     * SXSSFWorkbook 对象，避免内存溢出，不使用：HSSFWorkbook / XSSFWorkbook
     */
    private SXSSFWorkbook workbook;

    /**
     * 自定义 Handler 处理器集合（ExcelHandler）
     * 可以继承ExcelHandler添加
     */
    private List<ExcelHandler> excelHandlerList;

    /**
     * 数据对象类型Class
     *  (List<?>的数据类型，防止数据为空，泛型擦拭，获取不到类信息【数据为空，会返回表头信息（第一行）】）
     */
    private Class<?> dataClz;

    /**
     * 数据集合，数据类型若和 dataClz 类型不一致，抛出异常
     */
    private List<?> dataList;

    /**
     * 数据量大小限制
     */
    private Integer dataSizeLimit;

    /**
     * excel 开始行数
     */
    private Integer startRow;

    /**
     * excel 开始列数
     */
    private Integer startCell;


    /**
     * 校验和填充参数
     */
    public void checkFillParam() {
        checkFillParam(this);
    }

    /**
     * 校验和填充参数，默认兼容处理
     * 逻辑 >
     *  workbook 为空，默认初始化在内存中数据大小5000，不开启文件压缩的SXSSFWorkbook
     *  excelHandlerList校验处理器为空，默认加载三大默认校验
     *  执行处理器校验
     *  兼容开始行 : 小于0，默认取0
     *  兼容开始列 : 小于0，默认取0
     *  兼容数据量限制 : 小于0，默认10000
     *
     * @param param 参数信息
     * @return 校验和兼容之后的ExcelDataParam数据
     */
    public static ExcelDataParam checkFillParam(ExcelDataParam param) {
        if (Objects.isNull(param)) {
            param = new ExcelDataParam();
        }

        if (Objects.isNull(param.getWorkbook())) {
            // 默认在内存中数据大小5000，不开启文件压缩，提高性能
            param.setWorkbook(ExcelWorkbookUtil.initSXSSFWorkbook(ExcelConstant.DEFAULT_MEMORY_LOAD_SIZE, Boolean.FALSE));
        }

        if (CollectionUtils.isEmpty(param.getExcelHandlerList())) {
            // 加载默认处理
            loadDefaultHandler(param);
        }
        // 处理器执行校验
        checkHandler(param);

        // 参数值为空设置默认值
        param.setStartCell(Objects.isNull(param.getStartCell()) || param.getStartCell() < 0 ? ExcelConstant.DEFAULT_START_CELL : param.getStartCell());
        param.setStartRow(Objects.isNull(param.getStartRow()) || param.getStartRow() < 0 ? ExcelConstant.DEFAULT_START_CELL : param.getStartRow());
        param.setDataSizeLimit(Objects.isNull(param.getDataSizeLimit()) || param.getDataSizeLimit() < 0 ? ExcelConstant.EXPORT_MAX_ROW_SIZE : param.getDataSizeLimit());

        return param;
    }


    /**
     * 根据excelHandlerList取值 进行处理器 ExcelHandler 循环执行参数校验 check
     *
     * @param param ExcelHandlerParam
     */
    public static void checkHandler(ExcelDataParam param) {
        if (Objects.isNull(param) || CollectionUtils.isEmpty(param.getExcelHandlerList())) {
            return;
        }
        for (ExcelHandler excelHandler : param.getExcelHandlerList()) {
            excelHandler.check(ExcelHandlerParam.valueOf(param.getDataList(), param.getDataClz(), param.getDataSizeLimit()));
        }
    }

    public void addHandler(ExcelHandler excelHandler) {
        addHandler(this, excelHandler);
    }

    /**
     * 添加处理类（用于扩展实现 ExcelHandler）
     *
     * @param excelHandler ExcelHandler实现
     */
    public static void addHandler(ExcelDataParam excelDataParam, ExcelHandler excelHandler) {
        if (CollectionUtils.isEmpty(excelDataParam.getExcelHandlerList())) {

            excelDataParam.setExcelHandlerList(new ArrayList<>());
        }
        excelDataParam.getExcelHandlerList().add(excelHandler);
    }

    /**
     * 加载默认自定义 handler 处理器
     *
     * @param excelDataParam ExcelDataParam 对象
     */
    public static void loadDefaultHandler(ExcelDataParam excelDataParam) {
        excelDataParam.addHandler(new ExcelModelHandler());
        excelDataParam.addHandler(new ExcelDataHandler());
        excelDataParam.addHandler(new ExcelDataSizeLimitHandler());
    }



    public static ExcelDataParam valueOf(List<?> dataList, Class<?> dataClz) {
        return valueOf(dataList, dataClz, ExcelConstant.EXPORT_MAX_ROW_SIZE, ExcelConstant.DEFAULT_START_ROW, ExcelConstant.DEFAULT_START_CELL);
    }

    public static ExcelDataParam valueOf(List<?> dataList, Class<?> dataClz, int dataSizeLimit, int row, int cell) {
        ExcelDataParam result = new ExcelDataParam();
        result.setDataList(dataList);
        result.setDataClz(dataClz);
        result.setDataSizeLimit(dataSizeLimit);
        result.setStartRow(row);
        result.setStartCell(cell);
        return result;
    }


}
