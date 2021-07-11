package com.xgf.util;

import com.xgf.file.FileCopyUtil;
import org.junit.Test;

import java.io.File;

/**
 * @author strive_day
 * @create 2021-07-11 12:02
 * @description 文件复制工具类测试
 */
public class FileCopyUtilTest {

    @Test
    public void testCopyFile() {
        System.out.println(FileCopyUtil.copyCurrentAndSubFileAndDirectory("G:\\WQQ\\demo", "G:\\WQQ\\demov10"));
        System.out.println(FileCopyUtil.copyCurrentAndSubDirectory("G:\\WQQ\\demo", "G:\\WQQ\\demov20"));

    }

    @Test
    public void testExistSub(){
        File file1 = new File("G:\\hello\\demo\\000000");
        File file2 = new File("G:\\hello\\demo");
        File file3 = new File("G:\\demo");
        System.out.println(file1.isDirectory());
        System.out.println(file2.isDirectory());
        System.out.println(file3.isDirectory());
    }

}
