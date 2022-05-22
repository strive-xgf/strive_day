package com.xgf.mvc.controller;

import com.xgf.excel.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xgf
 * @create 2022-05-17 00:31
 * @description 文件 Controller
 **/

@Slf4j
@RestController
@RequestMapping(value = "/mvc/fileController")
@Api(description = "file_Controller", tags = "file_controller")
public class FileController {

    @Autowired
    private HttpServletResponse response;

    @GetMapping(value = "/fileDownload")
    @ApiOperation(value="文件下载", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void fileDownload() {
        try {
            // url 访问: http://localhost:8080/static/com/xgf/template/excel/testExcel.xlsx
            // get 访问: http://localhost:8080/mvc/fileController/fileDownload
//            ExcelUtil.downloadExcelTemplate(response, "static/com/xgf/template/excel/我是一个xlsx模板" + ExcelUtil.EXCEL_XLSX);
            ExcelUtil.downloadExcelTemplate(response, "static/com/xgf/template/excel/我是一个xls模板" + ExcelUtil.EXCEL_XLS);
        } catch (Exception e) {
            log.info("====== FileController fileDownload exception message = {}", e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
    }


}
