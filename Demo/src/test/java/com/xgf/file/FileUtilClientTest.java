package com.xgf.file;

import com.xgf.DemoApplication;
import com.xgf.common.JsonUtil;
import com.xgf.common.LogUtil;
import com.xgf.constant.DataEntry;
import com.xgf.task.TaskUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.*;

/**
 * @author strive_day
 * @create 2022-12-19 14:28
 * @description FileUtil Test
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class FileUtilClientTest {


    @Test
    public void testFileReName() {
        List<DataEntry<String, String>> fileOld2NewNameEntryList = new ArrayList<>();
        fileOld2NewNameEntryList.add(DataEntry.valueOf("123", "ABC"));

//        fileOld2NewNameEntryList.add(DataEntry.valueOf("Lile改名啦", "ile"));
        fileOld2NewNameEntryList.add(DataEntry.valueOf("ile", "Lile改名啦"));
//        fileOld2NewNameEntryList.add(DataEntry.valueOf("LLLLLLile改名啦改名啦改名啦改名啦改名啦改名啦", "ile"));

        System.out.println("fileOld2NewNameEntryList = " + JsonUtil.toJsonString(fileOld2NewNameEntryList));

//        int reNameCount = FileUtilClient.fileReName("I:\\Demo", null, null, new File("I:\\Demo\\fileReNameDemo\\execute result\\reNameResult.txt"));
//        int reNameCount = FileUtilClient.fileReName("I:\\Demo", fileOld2NewNameMap, null, new File("I:\\Demo\\fileReNameDemo\\execute result\\reNameResult.txt"));
//        int reNameCount = FileUtilClient.fileReName("I:\\Demo", fileOld2NewNameMap, null, null);
        int reNameCount = FileUtilClient.fileReName("I:\\Demo", fileOld2NewNameEntryList, "txt", new File("I:\\Demo\\fileReNameDemo\\execute result\\reNameResult.txt"));
        System.out.println(">>>>>> rename file count = " + reNameCount);
    }

    @Test
    public void test_FileReNameInner() {
        // 123456.txt 改名为 68CDSUCCESS.txt
        List<DataEntry<String, String>> fileOld2NewNameEntryList = new ArrayList<>();
        fileOld2NewNameEntryList.add(DataEntry.valueOf("123", "ABC"));
        fileOld2NewNameEntryList.add(DataEntry.valueOf("234", "BCD"));
        fileOld2NewNameEntryList.add(DataEntry.valueOf("345", "CDE"));
        fileOld2NewNameEntryList.add(DataEntry.valueOf("456", "DEF"));
        fileOld2NewNameEntryList.add(DataEntry.valueOf("AB", "68"));
        fileOld2NewNameEntryList.add(DataEntry.valueOf("EF", "SUCCESS"));
        System.out.println("fileOld2NewNameEntryList " + JsonUtil.toJsonString(fileOld2NewNameEntryList));
        boolean reNameCount = FileRenameUtil.fileReNameInner(new File("F:\\wqq\\demo\\reName\\111\\123456.txt"), fileOld2NewNameEntryList, null, FileUtil.createFileAndDir("F:\\wqq\\demo\\reName\\execute result\\reNameResult.txt"));
        System.out.println(">>>>>> rename file count = " + reNameCount);
    }

    @Test
    public void test_FileReName() {
        // 123456.txt 改名为 68CDSUCCESS.txt
        List<DataEntry<String, String>> fileOld2NewNameEntryList = new ArrayList<>();
        fileOld2NewNameEntryList.add(DataEntry.valueOf("123", "ABC"));
        fileOld2NewNameEntryList.add(DataEntry.valueOf("234", "BCD"));
        fileOld2NewNameEntryList.add(DataEntry.valueOf("345", "CDE"));
        fileOld2NewNameEntryList.add(DataEntry.valueOf("456", "DEF"));
        fileOld2NewNameEntryList.add(DataEntry.valueOf("AB", "68"));
        fileOld2NewNameEntryList.add(DataEntry.valueOf("EF", "SUCCESS"));
        System.out.println("fileOld2NewNameEntryList " + JsonUtil.toJsonString(fileOld2NewNameEntryList));
        int reNameCount = FileUtilClient.fileReName("F:\\wqq\\demo\\reName", fileOld2NewNameEntryList, null, FileUtil.createFileAndDir("F:\\wqq\\demo\\reName\\execute result\\reNameResult.txt"));
        System.out.println(">>>>>> rename file count = " + reNameCount);
    }

    @Test
    public void testJudgeFileTypeSuffix() {
        System.out.println(FileUtil.judgeFileTypeSuffix(new File("I:\\Demo\\file1\\file2\\file3"), ""));
        System.out.println(FileUtil.judgeFileTypeSuffix(new File("I:\\Demo\\file1\\file2\\file3\\file3_text.txt"), ""));
        System.out.println(FileUtil.judgeFileTypeSuffix(new File("I:\\Demo\\file1\\file2\\file3\\file3_text.txt"), "txt"));
        System.out.println(FileUtil.judgeFileTypeSuffix(new File("I:\\Demo\\file1\\file2\\file3\\file3_text.txt"), "mp3"));
    }
}
