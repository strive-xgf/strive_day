package com.xgf.excel.constant;

/**
 * @author xgf
 * @create 2022-05-16 00:56
 * @description excel 通用常量
 **/

public class ExcelConstant {

    /**
     * 2003年（包含） 以下版本
     */
    public static final String EXCEL_XLS = ".xls";

    /**
     * 2007年（包含） 以上版本
     */
    public static final String EXCEL_XLSX = ".xlsx";

    /**
     * 导出数据默认行数量
     */
    public static final int EXPORT_DEFAULT_ROW_SIZE = 500;

    /**
     * 导出数据最大行数量
     */
    public static final int EXPORT_MAX_ROW_SIZE = 10000;

    /**
     * 导出数据默认列数量
     */
    public static final int EXPORT_DEFAULT_COLUMN_SIZE = 500;

    /**
     * 导出数据最大列数量
     */
    public static final int EXPORT_MAX_COLUMN_SIZE = 10000;


    /**
     * 默认excel文档开始行（标题行）
     */
    public static final int DEFAULT_START_ROW = 0;

    /**
     * 默认excel文档开始列
     */
    public static final int DEFAULT_START_CELL = 0;

    /**
     * SXSSFWorkbook 保存在内存中的默认数据大小，其它的数据都会写到磁盘里，来减少占用的内存
     */
    public static final int DEFAULT_MEMORY_LOAD_SIZE = 5000;


}
