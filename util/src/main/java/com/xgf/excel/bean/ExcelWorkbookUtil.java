package com.xgf.excel.bean;

import com.xgf.common.JsonUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.excel.constant.ExcelConstant;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.reflect.CommonReflectUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2022-05-16 01:10
 * @description excel的领域类 添加相关excel属性，获取 Workbook 信息
 **/

public class ExcelWorkbookUtil {

    private final static transient Logger log = LoggerFactory.getLogger(ExcelWorkbookUtil.class);

    /**
     * 构建初始化限制内存读取长度的workbook，可设置 tempFile 化，让数据保护提升，但是会牺牲性能
     * tempOpen 此变量是用来开启文件压缩的。【谨慎使用。 SXSSFWorkbook 应该是必定会保存临时文件的】
     *
     * @param memoryLoadSize SXSSFWorkbook保存在内存中的数据大小，其它的数据都会写到磁盘里，来减少占用的内存
     * @param tempOpen       是否开启文件压缩
     * @return ExcelWorkbook对象
     */
    public static SXSSFWorkbook initSXSSFWorkbook(int memoryLoadSize, boolean tempOpen) {
        SXSSFWorkbook sxssfWorkbook = initSXSSFWorkbook(memoryLoadSize);
        sxssfWorkbook.setCompressTempFiles(tempOpen);
        return sxssfWorkbook;
    }

    /**
     * 构建初始化限制内存读取长度的workbook，此处不进行tempFile化，以提升性能
     *
     * @param memoryLoadSize SXSSFWorkbook保存在内存中的数据大小，其它的数据都会写到磁盘里，来减少占用的内存
     * @return ExcelWorkbook对象
     */
    public static SXSSFWorkbook initSXSSFWorkbook(int memoryLoadSize) {
        return new SXSSFWorkbook(memoryLoadSize);
    }


    /**
     * 配置全走默认，返回SXSSFWorkbook
     * @param dataList 数据集合
     * @param dataClz 数据类型
     * @return SXSSFWorkbook
     */
    public static SXSSFWorkbook convertData2WorkBook(List<?> dataList, Class<?> dataClz) {
        return convertData2WorkBook(ExcelDataParam.valueOf(dataList, dataClz));
    }

    /**
     * 基于参数写入SXSSFWorkbook，返回SXSSFWorkbook
     *
     * @param param SXSSFWorkbook配置及相关数据
     * @returnSXSSFWorkbook
     */
    public static SXSSFWorkbook convertData2WorkBook(ExcelDataParam param) {
        if (Objects.isNull(param)) {
            throw CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION.generateException();
        }

        // 校验和填充参数默认值
        param.checkFillParam();

        // 获取字段信息，基于注解 ExcelFieldAnnotation 有序排序
        Map<String, String> field2AnnotationNameLinkedMap = ExcelFieldInfo.getField2AnnotationNameLinkedMap(param.getDataClz());

        // 类字段信息为空，则直接返回
        if (MapUtils.isEmpty(field2AnnotationNameLinkedMap)) {
            log.warn("====== {}, convertData2WorkBook {} field is null", log.getName(), param.getDataClz().getName());
            return param.getWorkbook();
        }

        // 数据是空，只需要组装标题行
        if (CollectionUtils.isEmpty(param.getDataList())) {
            log.warn("====== {}, convertData2WorkBook dataList is empty", log.getName());
            // 标题行
            fillTitleRow(param.getWorkbook(), new ArrayList<>(field2AnnotationNameLinkedMap.values()), param.getStartRow(), param.getStartCell());
            return param.getWorkbook();
        }

        // 填充数据
        fillExcelDataColumn(param.getWorkbook(), param.getDataList(), param.getStartCell(), param.getStartCell());

        return param.getWorkbook();

    }

    /**
     * 将数据填充到 workbook 中
     *
     * @param workbook SXSSFWorkbook
     * @param dataList 数据集合
     * @param startRow excel 文件写开始行
     * @param startCell excel 文件写开始列
     */
    public static void fillExcelDataColumn(SXSSFWorkbook workbook, List<?> dataList, int startRow, int startCell) {

        dataList = dataList.stream().filter(Objects::nonNull).collect(Collectors.toList());

        // 获取数据对象的类信息
        Class<?> dataClz = dataList.get(0).getClass();

        // 获取字段信息，基于注解 ExcelFieldAnnotation 有序排序
        Map<String, String> fieldName2AnnotationNameLinkedMap = ExcelFieldInfo.getField2AnnotationNameLinkedMap(dataClz);

        // 初始化标题行
        int sheetIndex = fillTitleRow(workbook, new ArrayList<>(fieldName2AnnotationNameLinkedMap.values()), startRow, startCell);
        SXSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        // 标题占一行，所以要+1
        startRow++;

        // 获取数据 dataList 对象的所有字段信息（根据字段名称去重）【包含父类]
        Map<String, Field> dataFieldName2FieldMap = Optional.ofNullable(CommonReflectUtil.getAllFieldByNameDistinct(dataClz))
                .orElseGet(ArrayList::new)
                .stream()
                .collect(Collectors.toMap(Field::getName, Function.identity(), (v1, v2) -> v1));


        for (Object data : dataList) {
            SXSSFRow sxssfRow = sheet.createRow(startRow);
            int cell = startCell;
            // 标题行根据注解排序之后，得到的结果：fieldName2AnnotationNameLinkedMap，根据这个顺序，获取字段的信息，填充到excel中去
            for (Map.Entry<String, String> entry : fieldName2AnnotationNameLinkedMap.entrySet()) {
                String fieldValue = getDataFieldValue(data, dataFieldName2FieldMap, entry);
                sxssfRow.createCell(cell).setCellValue(fieldValue);
                cell++;
            }
            startRow++;
        }
    }


    /**
     * 基于顺序，反射获取数据对象的对应字段值（保证数据都写到对应的数据列下【排序后】）
     *
     * @param data 数据对象
     * @param dataFieldName2FieldMap key: 数据对象字段名称 value: 对应字段信息Field
     * @param fieldName2AnnotationNameLinkedMap 基于注解ExcelFieldInfoAnnotation order排序后（如果无注解，则根据类字段排序）的有序map key: 字段名 value: ExcelFieldInfoAnnotation的name，无注解则为字段名
     * @return 对应字段在对象中的值信息的json数据
     */
    private static String getDataFieldValue(Object data, Map<String, Field> dataFieldName2FieldMap, Map.Entry<String, String> fieldName2AnnotationNameLinkedMap) {
        Field field;
        try {
            field = dataFieldName2FieldMap.get(fieldName2AnnotationNameLinkedMap.getKey());
            field.setAccessible(true);
            Object fieldValue = field.get(data);
            return Objects.nonNull(fieldValue) ? JsonUtil.toJsonString(fieldValue) : StringConstantUtil.EMPTY;
        } catch (Exception e) {
            log.error("====== {}, getDataFieldValue write excel read data error, message = {}", log.getName(), e.getLocalizedMessage(), e);
            return StringConstantUtil.EMPTY;
        }finally {
            // 手动释放资源，提高效率
            field = null;
        }
    }


    /**
     * 初始化 excel 标题行（默认第0行）
     *
     * @param workbook SXSSFWorkbook
     * @param fieldNameList 标题行字段名称信息
     * @param row 开始展示行
     * @param cell 开始展示列
     * @return SXSSFWorkbook sheet 返回工作表的索引（比如excel三个sheet文档，索引分别为0，1，2）
     */
    public static int fillTitleRow(SXSSFWorkbook workbook, List<String> fieldNameList, int row, int cell) {
        SXSSFSheet sxssfSheet = workbook.createSheet();

        SXSSFRow sxssfRow = sxssfSheet.createRow(row < 0 ? ExcelConstant.DEFAULT_START_ROW : row);
        cell = cell < 0 ? ExcelConstant.DEFAULT_START_CELL : cell;
        
        for (String fieldName : fieldNameList) {
            sxssfRow.createCell(cell).setCellValue(fieldName);
            cell ++;
        }

        return workbook.getSheetIndex(sxssfSheet);
    }

    


}
