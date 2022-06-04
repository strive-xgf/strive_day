package com.xgf.excel;

import com.xgf.constant.StringConstantUtil;
import com.xgf.excel.bean.ExcelDataParam;
import com.xgf.excel.bean.ExcelWorkbookUtil;
import com.xgf.excel.constant.ExcelConstant;
import com.xgf.exception.CustomExceptionEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xgf
 * @create 2022-05-16 00:52
 * @description apache poi 封装 Excel 工具类，外部直接使用工具类
 **/


public class ExcelUtil {

    protected final static transient Logger log = LoggerFactory.getLogger(ExcelUtil.class);


    /**
     * 导出数据到 HttpServletResponse 响应中，页面直接下载对应的excel文件
     *
     * @param resp     HttpServletResponse
     * @param dataList 数据集合
     * @param fileName 导出文件名
     */
    public static void exportExcelData(HttpServletResponse resp, List<?> dataList, String fileName) {
        Class<?> dataClz = Optional.ofNullable(dataList).orElseThrow(CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION::generateException)
                .stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION::generateException)
                .getClass();

        exportExcelData(resp, dataList, dataClz, dataList.size(), ExcelConstant.DEFAULT_START_ROW, ExcelConstant.DEFAULT_START_CELL, fileName);
    }

    /**
     * 导出 excel 标题行（无数据，字段名称（注解名称优先）的标题行）
     *
     * @param resp HttpServletResponse
     * @param dataClz 数据类型
     * @param fileName 导出文件名
     */
    public static void exportExcelTitle(HttpServletResponse resp, Class<?> dataClz, String fileName) {
        exportExcelData(resp, new ArrayList<>(), dataClz, 1, ExcelConstant.DEFAULT_START_ROW, ExcelConstant.DEFAULT_START_CELL, fileName);
    }


    /**
     * 导出数据到 HttpServletResponse 响应中，页面直接下载对应的excel文件
     *
     * @param resp          HttpServletResponse
     * @param dataList      数据集合
     * @param dataClz       数据类型
     * @param dataSizeLimit 数据量限制大小
     * @param startRow      excel 文件写入开始行
     * @param startCell     excel 文件写入开始列
     * @param fileName      文件名（导出时的文件名称）
     */
    public static void exportExcelData(HttpServletResponse resp, List<?> dataList, Class<?> dataClz, int dataSizeLimit, int startRow, int startCell, String fileName) {

        long startTime = System.currentTimeMillis();
        try {
            // 下载文件名: template_ + 文件名
            String downloadFileName = URLEncoder.encode(StringUtils.isBlank(fileName) ? "strive_day下载" : fileName, "UTF-8");

            // 设置响应头 Content-disposition 是 MIME 协议的扩展，MIME 协议指示 MIME 用户代理如何显示附加的文件。当 Internet Explorer 接收到头时，它会激活文件下载对话框，它的文件名框自动填充了头中指定的文件名。（请注意，这是设计导致的；无法使用此功能将文档保存到用户的计算机上，而不向用户询问保存位置。）
            //Content-disposition：以什么格式打开响应体数据（inline默认当前页面打开；attachment;filename=xxx 以附件形式打开响应体，文件下载）
            resp.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName + ExcelConstant.EXCEL_XLSX);

            // 设置字符编码格式
            resp.setCharacterEncoding("utf-8");
            // 是文件，统一标识流格式（不知道文件是什么格式，统一设置为）
            resp.setContentType("application/octet-stream");

            // 响应输出流
            ServletOutputStream os = resp.getOutputStream();

            // 默认从第0行第0列开始，所以输入行列要减1，为负数，会兼容为0
            SXSSFWorkbook workbook = ExcelWorkbookUtil.convertData2WorkBook(ExcelDataParam.valueOf(dataList, dataClz, dataSizeLimit, startRow - 1, startCell - 1));

            workbook.write(os);
            os.flush();
            os.close();

        } catch (Exception e) {
            log.error("====== {}, exportExcelData Exception message = {}", log.getName(), e.getLocalizedMessage(), e);
            throw CustomExceptionEnum.FILE_DOWNLOAD_EXCEPTION.generateException(e.getLocalizedMessage());
        } finally {
            log.info("====== {}, exportExcelData cost = {} ms", log.getName(), System.currentTimeMillis() - startTime);
        }
    }

    /**
     * 基于SXSSFWorkbook将数据写到指定路径的excel文件中【注意原文件有值，则清空覆盖】
     */
    public static boolean writeDataToExcelFile(List<?> dataList, Class<?> dataClz, int dataSizeLimit, String filePath) {
        return writeDataToExcelFile(dataList, dataClz, dataSizeLimit, filePath, ExcelConstant.DEFAULT_START_ROW, ExcelConstant.DEFAULT_START_CELL);
    }

    /**
     * 基于SXSSFWorkbook将数据写到指定路径的excel文件中【注意原文件有值，则清空覆盖】
     * 数据集合为空则只写标题列
     *
     * @param dataList 数据集合
     * @param dataClz 数据类型
     * @param dataSizeLimit 数据量限制大小
     * @param filePath 文件路径
     * @param startRow excel 文件写入开始行
     * @param startCell excel 文件写入开始列
     * @return true: 写入成功， false: 写入失败
     */
    public static boolean writeDataToExcelFile(List<?> dataList, Class<?> dataClz, int dataSizeLimit, String filePath, int startRow, int startCell) {
        long startTime = System.currentTimeMillis();
        try {
            // 默认从第0行第0列开始，所以输入行列要减1，为负数，会兼容为0
            SXSSFWorkbook workbook = ExcelWorkbookUtil.convertData2WorkBook(ExcelDataParam.valueOf(dataList, dataClz, dataSizeLimit, startRow-1, startCell-1));
            if (Objects.isNull(workbook)) {
                log.warn("====== {} writeDataToExcelFile workbook result is null", log.getName());
                return false;
            }

            //  JDK1.7 之后，try语句内创建的流将会自动关闭，不需要显式的关闭流 close
            OutputStream out = new FileOutputStream(filePath, Boolean.FALSE);
            workbook.write(out);
        } catch (Exception e) {
            log.warn("====== {} writeDataToExcelFile error message = {}", log.getName(), e.getLocalizedMessage(), e);
            return false;
        }finally {
            log.info("====== {} writeDataToExcelFile cost = {} ms", log.getName(), System.currentTimeMillis() - startTime);
        }
        return true;

    }

    /**
     * 根据文件路径 + 扩展信息 下载excel模板（页面点击下载模板，传入路径，实现直接下载功能）
     * 此方法使用的是 HSSFWorkbook / XSSFWorkbook，注意可能导致内存溢出问题OutOfMemoryError
     * 当excel文档行数过多，请使用 SXSSFWorkbook 方式
     *
     * @param resp     HttpServletResponse
     * @param path     文件路径信息
     */
    public static void downloadExcelTemplate(HttpServletResponse resp, String path) {

        // 文件类型下标（从后往前，到第一个出现点.的内容）
        int fileTypeIndex = path.lastIndexOf(StringConstantUtil.DOT);
        // 文件名下标（从后往前，找到第一个出现 / 文件扩展名的内容作为开始，到第一个出现点的内容结束）
        int fileNameIndex = path.lastIndexOf(StringConstantUtil.UP_DIAGONAL) + 1;

        // 文件类型
        String fileType = path.substring(fileTypeIndex);
        // 文件名
        String fileName = path.substring(fileNameIndex, fileTypeIndex);

        if (!(ExcelConstant.EXCEL_XLS.equals(fileType) || ExcelConstant.EXCEL_XLSX.equals(fileType))) {
            throw CustomExceptionEnum.PARAM_TYPE_ILLEGAL_EXCEPTION.generateCustomMessageException("ExcelUtil getWorkbookByExcelType no support excel file type : " + fileType);
        }

        try {

            // 下载文件名: template_ + 文件名
            String downloadFileName = URLEncoder.encode("template_" + fileName, "UTF-8");

            // 设置响应头 Content-disposition 是 MIME 协议的扩展，MIME 协议指示 MIME 用户代理如何显示附加的文件。当 Internet Explorer 接收到头时，它会激活文件下载对话框，它的文件名框自动填充了头中指定的文件名。（请注意，这是设计导致的；无法使用此功能将文档保存到用户的计算机上，而不向用户询问保存位置。）
            //Content-disposition：以什么格式打开响应体数据（inline默认当前页面打开；attachment;filename=xxx 以附件形式打开响应体，文件下载）
            resp.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName + fileType);

            // 设置字符编码格式
            resp.setCharacterEncoding("utf-8");
            // 是文件，统一标识流格式（不知道文件是什么格式，统一设置为）
            resp.setContentType("application/octet-stream");


            // JDK1.7 之后，try语句内创建的流将会自动关闭，不需要显式的关闭流 close
            InputStream is = getResourcesFileInputStream(path);

            // 根据文件格式 获取 POI 的 实现 HSSFWorkbook / XSSFWorkbook
            Workbook wb = getWorkbookByExcelType(is);

            ServletOutputStream os = resp.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();

        } catch (IOException | InvalidFormatException e) {
            log.error("====== ExcelUtil downloadExcelTemplate Exception message = {}", e.getLocalizedMessage(), e);
            throw CustomExceptionEnum.FILE_DOWNLOAD_EXCEPTION.generateException(e.getLocalizedMessage());
        }
    }





    /**
     * 获取文件输入流（ resources 目录下）
     * getResourceAsStream() 动态的获取某个文件的位置, 从而能够获取此文件的资源（不用每次去修改代码中文件的绝对地址或详细地址）
     *
     * @param filePathName 文件路径
     * @return InputStream 输入流，方便对此文件资源通过IO流进行获取
     */
    public static InputStream getResourcesFileInputStream(String filePathName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(filePathName);
    }


    /**
     * 根据文件流，判断 excel 文件类型，返回对应的 Workbook
     * 【注意】这里不能直接用后缀名扩展名来判断，如果直接改后缀扩展名 (eg: xlsx 改为 xls)，会报错不支持，直接改扩展名不会导致本质变更，因为【03及其以前的的最多只有65536行，07及其以后的无限制】，
     *
     * @param is 文件输入流
     * @return HSSFWorkbook / XSSFWorkbook
     */
    private static Workbook getWorkbookByExcelType(InputStream is) throws InvalidFormatException, IOException {

        // 根据流信息，判断 excel 版本，若为2007 （包括2007）以后的版本，扩展名是.xls，则实例化 XSSFWorkbook，若为 Excel 2003 （包括2003）以前的版本，扩展名是.xls，则实例化 HSSFWorkbook
        return WorkbookFactory.create(is);
    }


//    /**
//     * POI 升级后，没有 hasOOXMLHeader 方法了，通过工厂 WorkbookFactory.create() 直接创建就行
//     * {@linkplain ExcelUtil#getWorkbookByExcelType}
//     */
//    private static Workbook getWorkbookByExcelType(InputStream is) throws IOException {
//        // XSSFWorkbook: 操作 Excel 2007 （包括2007）以后的版本，扩展名是.xls
//        if(POIXMLDocument.hasOOXMLHeader(is)) {
//            return new XSSFWorkbook(is);
//        }
//
//        // HSSFWorkbook: 操作 Excel 2003 （包括2003）以前的版本，扩展名是.xls
//        if (POIFSFileSystem.hasPOIFSHeader(is)) {
//            return new HSSFWorkbook(is);
//        }
//        throw CustomExceptionEnum.PARAM_TYPE_ILLEGAL_EXCEPTION.generateCustomMessageException("ExcelUtil getWorkbookByExcelType no support excel file type : " + fileType);
//    }

}
