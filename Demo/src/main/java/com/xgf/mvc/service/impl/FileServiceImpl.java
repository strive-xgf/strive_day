package com.xgf.mvc.service.impl;

import com.xgf.bean.ExcelUser;
import com.xgf.create.CreateRandomObjectUtil;
import com.xgf.excel.ExcelUtil;
import com.xgf.mvc.service.FileService;
import com.xgf.mvc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xgf
 * @create 2022-05-17 00:42
 * @description
 **/

@Slf4j
@Service("fileService")
public class FileServiceImpl implements FileService {

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private UserService userService;

    @Override
    public void mockExportExcel() {

        // 模拟数据
        List<ExcelUser> excelUserList = CreateRandomObjectUtil.createDataList(ExcelUser.class, 10);

        ExcelUtil.exportExcelData(response, excelUserList, ExcelUser.class,500001, 1, 1, "mockExportExcel");
//        ExcelUtil.exportExcelTitle(response, ExcelUser.class, "ExcelUser标题");
    }
}
