package com.xgf.util;

import com.xgf.file.FileUtil;
import com.xgf.system.SystemUtil;
import org.junit.Test;

import java.io.File;

/**
 * @author xgf
 * @create 2022-05-03 11:58
 * @description
 **/

public class FileUtilTest {

    /**
     * 文件默认根路径
     */
    public static String FILE_MAC_SOURCE_PATH = "/Users/xiagaofeng/Desktop/document-00/strive_day/log";

    @Test
    public void testCreateFileBySysTime() {
        File file1 = FileUtil.createFileBySysTime();
        System.out.println("file1 path = " + file1.getPath());
        File file2 = FileUtil.createFileBySysTime(null, "doc");
        System.out.println("file2 path = " + file2.getPath());
        File file3 = FileUtil.createFileBySysTime(FILE_MAC_SOURCE_PATH, "ppt");
        System.out.println("file3 path = " + file3.getPath());
    }

    @Test
    public void testFileAppendData() {
        File file = FileUtil.createFileBySysTime(FILE_MAC_SOURCE_PATH, "txt");
        System.out.println("file path = " + file.getPath());
        FileUtil.fileAppendData(file, "\r\n我要添加内容");
    }

    @Test
    public void testFileAppendData2() {
        String pathUrl = SystemUtil.getCurrentDir() + SystemUtil.getFileSeparator() + "testFileAppendData2.txt";
        System.out.println("file path = " + pathUrl);
        FileUtil.fileAppendData(pathUrl, "\r\n我要添加内容");
    }


    @Test
    public void testCreateFileDir() {
        FileUtil.createFileDir("F:\\wqq\\Demo\\a");
        FileUtil.createFileDir("F:\\wqq\\Demo\\b");
        FileUtil.createFileDir("F:\\wqq\\Demo\\a");
        FileUtil.createFileDir("F:\\wqq\\Demo\\b");
    }


}
