package com.xgf.file;

import com.xgf.DemoApplication;
import com.xgf.common.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.HashMap;

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
        HashMap<String, String> fileOld2NewNameMap = new HashMap<>();
//        fileOld2NewNameMap.put("Lile改名啦", "ile");
        fileOld2NewNameMap.put("ile", "Lile改名啦");
//        fileOld2NewNameMap.put("LLLLLLile改名啦改名啦改名啦改名啦改名啦改名啦", "ile");

        System.out.println("replaceStrMapList = " + JsonUtil.toJsonString(fileOld2NewNameMap));

//        int reNameCount = FileUtilClient.fileReName("I:\\Demo", null, null, new File("I:\\Demo\\fileReNameDemo\\execute result\\reNameResult.txt"));
//        int reNameCount = FileUtilClient.fileReName("I:\\Demo", fileOld2NewNameMap, null, new File("I:\\Demo\\fileReNameDemo\\execute result\\reNameResult.txt"));
//        int reNameCount = FileUtilClient.fileReName("I:\\Demo", fileOld2NewNameMap, null, null);
        int reNameCount = FileUtilClient.fileReName("I:\\Demo", fileOld2NewNameMap, "txt", new File("I:\\Demo\\fileReNameDemo\\execute result\\reNameResult.txt"));
        System.out.println(">>>>>> rename file count = " + reNameCount);
    }

//    @Test
//    public void testJudgeFileTypeSuffix() {
//        FileRenameUtil fileRenameUtil = new FileRenameUtil();
//        fileRenameUtil.setStaticFileReNameFlag();
//        System.out.println(fileRenameUtil.judgeFileTypeSuffix(new File("I:\\Demo\\file1\\file2\\file3"), ""));
//        System.out.println(fileRenameUtil.judgeFileTypeSuffix(new File("I:\\Demo\\file1\\file2\\file3\\file3_text.txt"), ""));
//        System.out.println(fileRenameUtil.judgeFileTypeSuffix(new File("I:\\Demo\\file1\\file2\\file3\\file3_text.txt"), "txt"));
//        System.out.println(fileRenameUtil.judgeFileTypeSuffix(new File("I:\\Demo\\file1\\file2\\file3\\file3_text.txt"), "mp3"));
//    }
}
