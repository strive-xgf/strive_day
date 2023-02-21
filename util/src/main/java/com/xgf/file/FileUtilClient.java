package com.xgf.file;

import com.xgf.constant.DataEntry;
import com.xgf.date.DateUtil;
import com.xgf.java8.BooleanFunctionUtil;
import com.xgf.system.SystemUtil;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author strive_day
 * @create 2022-12-20 15:46
 * @description 通用文件工具客户端（对外提供）
 */
public class FileUtilClient {

    public static int fileReName(String path, List<DataEntry<String, String>> fileOld2NewNameEntryList) {
        return fileReName(path, fileOld2NewNameEntryList, null, null);
    }

    public static int fileReName(String path, List<DataEntry<String, String>> fileOld2NewNameEntryList, String fileType) {
        return fileReName(path, fileOld2NewNameEntryList, fileType, null);
    }

    public static int fileReName(String path, List<DataEntry<String, String>> fileOld2NewNameEntryList, File addReNameLogFile) {
        return fileReName(path, fileOld2NewNameEntryList, null, addReNameLogFile);
    }

    /**
     * 对目标路径下的文件进行按要求更改名字
     *
     * @param path                     目标路径下的满足要求的文件进行更名（找到最内部文件夹先改名）
     * @param fileOld2NewNameEntryList 替换文件名EntryList，key: 原文件名称需要被替换的字符串，value: 新文件名替换的字符串，为 null 则不执行
     *                                 保证顺序替换，eg: 123456.txt，替换map为  123 = abc, bc4 = def, 那么改名之后结果为: adef56.txt
     *                                 todo: 应该用抽象Map？传参用LinkedHashMap保证有序
     * @param fileType                 文件类型 eg: .txt, .mp4【只是根据当前文件后缀来区分，不区分文件具体是什么类型（改后缀名，效率太低）】
     *                                 传 null 则为所有文件
     * @param addReNameLogFile         记录更改文件名称的记录日志到参数文件中
     * @return 更改文件数量
     */
    public static int fileReName(String path, List<DataEntry<String, String>> fileOld2NewNameEntryList, String fileType, File addReNameLogFile) {

        long startTime = System.currentTimeMillis();
        // 记录开始时间文件信息
        BooleanFunctionUtil.trueRunnable(addReNameLogFile != null).run(() ->
                FileUtil.fileAppendDataThrow(addReNameLogFile,  "execute fileReName start, path = " + path + ", time = "
                        + DateUtil.dateFormatString(new Date(startTime), DateUtil.FORMAT_MILL)  + SystemUtil.getLineSeparator()));

        int reNameFileCount;
        try {
            reNameFileCount = FileRenameUtil.fileReName(path, fileOld2NewNameEntryList, fileType, addReNameLogFile);
        } catch (Exception e) {
            BooleanFunctionUtil.trueRunnable(addReNameLogFile != null).run(() ->
                    FileUtil.fileAppendDataThrow(addReNameLogFile,  "execute fileReName end, path = " + path + ", time = "
                            + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_MILL) + ", execute exception =" + e.getLocalizedMessage() + SystemUtil.getLineSeparator()));
            throw e;
        }

        long endTime = System.currentTimeMillis();
        // 记录执行结束文件信息
//        int finalReNameFileCount = reNameFileCount;
        BooleanFunctionUtil.trueRunnable(addReNameLogFile != null).run(() ->
                FileUtil.fileAppendDataThrow(addReNameLogFile, "execute fileReName end, path = " + path + ", time = "
                        + DateUtil.dateFormatString(new Date(endTime), DateUtil.FORMAT_MILL) + ", reNameFileCount = " + reNameFileCount + ", cost = " + (endTime - startTime) + " ms " + SystemUtil.getLineSeparator() + SystemUtil.getLineSeparator()));

        return reNameFileCount;
    }


}
