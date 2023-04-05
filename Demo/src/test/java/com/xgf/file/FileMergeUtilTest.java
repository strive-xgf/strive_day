package com.xgf.file;

import com.xgf.constant.enumclass.FileTypeEnum;
import com.xgf.system.SystemUtil;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author strive_day
 * @create 2023-04-06 1:02
 * @description 文件合并工具类测试
 */

public class FileMergeUtilTest {

    @Test
    public void test_mergeSingleFile() {

        final String dir = "G:\\Demo\\txt集合";
        FileUtil.createFileDir(dir);
        for (int i = 1; i < 1222; i++) {
            try(BufferedWriter out = new BufferedWriter(new FileWriter(FileUtil.createFileAndDir(dir + SystemUtil.getFileSeparator() + i + ".txt")))) {
                try {
                    out.write(i + SystemUtil.getLineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        System.out.println(FileMergeUtil.mergeSingleFile("G:\\Demo\\txt集合",
//                FileTypeEnum.TXT,
//                "G:\\Demo\\txt集合\\合并",
//                null,
//                FileTypeEnum.TXT,
//                false,
//                false
//        ));
        System.out.println(FileMergeUtil.mergeSingleFile("G:\\Demo\\txt集合",
                FileTypeEnum.TXT,
                "G:\\Demo\\txt集合\\合并",
                "merge_txt",
                FileTypeEnum.TXT,
                false,
                false
        ));

    }

    @Test
    public void test_mergeAllFile() {
        FileMergeUtil.mergeAllFile("G:\\Demo\\ts集合",
                FileTypeEnum.TS,
                "G:\\Demo\\合并集合",
                FileTypeEnum.MP4,
                false,
                true
        );
    }


}
