package com.xgf.util.excel;

import com.xgf.bean.ExcelUser;
import com.xgf.bean.User;
import com.xgf.create.CreateRandomObjectUtil;
import com.xgf.excel.ExcelUtil;
import com.xgf.excel.constant.ExcelConstant;
import com.xgf.system.SystemUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xgf
 * @create 2022-05-30 23:49
 * @description
 **/

public class ExcelUtilTest {

    /**
     * 测试时文件路径前缀
     */
    private static final String FILE_DIR = "/Users/xiagaofeng/Desktop/临时文件";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * 测试数据为空值，只显示标题咧
     */
    @Test
    public void testNoValue() {
        List<User> userList = new ArrayList<>();
        boolean resultFlag = ExcelUtil.writeDataToExcelFile(
                userList, User.class, ExcelConstant.EXPORT_MAX_ROW_SIZE,  FILE_DIR + SystemUtil.getFileSeparator() + "testNoValue.xlsx", 6, 2);
        System.out.println("resultFlag = " + resultFlag);
    }

    @Test
    public void testDataSize500() {
        List<User> userList = CreateRandomObjectUtil.createDataList(User.class, 500);
        boolean resultFlag = ExcelUtil.writeDataToExcelFile(
                userList, User.class,500001, FILE_DIR + SystemUtil.getFileSeparator() + "testDataSize500.xlsx", 0, 2);
        System.out.println("resultFlag = " + resultFlag);
    }

    /**
     * 测试超大数据 50w 数据 excel
     */
    @Test
    public void testDataSize500000() {
        List<User> userList = CreateRandomObjectUtil.createDataList(User.class, 500000);
        boolean resultFlag = ExcelUtil.writeDataToExcelFile(
                userList, User.class,500001, FILE_DIR + SystemUtil.getFileSeparator() + "testDataSize500000.xlsx", 0, 2);
        System.out.println("resultFlag = " + resultFlag);
    }


    @Test
    public void test_data_size_over_limit() {
        List<User> userList = CreateRandomObjectUtil.createDataList(User.class, 50000);
        boolean resultFlag = ExcelUtil.writeDataToExcelFile(
                userList, User.class,ExcelConstant.EXPORT_MAX_ROW_SIZE, FILE_DIR + SystemUtil.getFileSeparator() + "test_data_size_over_limit.xlsx", 0, 2);
        System.out.println("resultFlag = " + resultFlag);
    }


    @Test
    public void testAnnotation() {

        List<ExcelUser> excelUserList = CreateRandomObjectUtil.createDataList(ExcelUser.class, 500);
        boolean resultFlag = ExcelUtil.writeDataToExcelFile(
                excelUserList, ExcelUser.class,ExcelConstant.EXPORT_MAX_ROW_SIZE, FILE_DIR + SystemUtil.getFileSeparator() + "testAnnotation.xlsx", 0, 2);
        System.out.println("resultFlag = " + resultFlag);
    }


}
