package com.xgf.file;

import com.google.common.collect.Lists;
import com.xgf.common.JsonUtil;
import com.xgf.common.LogUtil;
import com.xgf.constant.DataEntry;
import com.xgf.constant.StringConstantUtil;
import com.xgf.date.DateUtil;
import com.xgf.java8.BooleanFunctionUtil;
import com.xgf.java8.ThrowExceptionFunctionUtil;
import com.xgf.system.SystemUtil;
import com.xgf.task.TaskUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author strive_day
 * @create 2022-12-17 17:01
 * @description 文件重命名工具类
 */
@Component
public class FileRenameUtil {

    /**
     * 是否需要添加文件重命名日志
     */
    private static Boolean FILE_RENAME_LOG_FLAG = true;

    /**
     * 给静态变量 FILE_RENAME_LOG_FLAG 读取赋值，默认为 false
     */
    @Value("${custom.config.log.file.fileReNameLogFlag:false}")
    public void setStaticFileReNameFlag(Boolean fileReNameLogFlag) {
        FileRenameUtil.FILE_RENAME_LOG_FLAG = fileReNameLogFlag;
    }


    /**
     * @param path                     目标路径下的所有文件进行更名（找到最内部文件夹先改名）
     * @param fileOld2NewNameEntryList 替换文件名EntryList，key: 原文件名称需要被替换的字符串，value: 新文件名替换的字符串，为 null 则不执行
     * @param fileType                 文件类型 eg: .txt, .mp4【只是根据当前文件后缀来区分，不区分文件具体是什么类型（改后缀名，效率太低）】
     *                                 传 null 则为所有文件
     * @param addReNameLogFile         记录更改文件名称的记录日志到参数文件中
     * @return 更改文件数量
     */
    protected static int fileReName(String path,
                                    List<DataEntry<String, String>> fileOld2NewNameEntryList,
                                    String fileType,
                                    File addReNameLogFile) {

        ThrowExceptionFunctionUtil.isTureThrow(fileOld2NewNameEntryList == null).throwMessage("重命名字段映射不能为空");
        // 重命名文件数
        AtomicInteger reNameCount = new AtomicInteger();
        long startTime = System.currentTimeMillis();

        // 获取目录下的所有文件夹
        File[] dirFileList = new File(path).listFiles();
        if (dirFileList == null) {
            BooleanFunctionUtil.trueRunnable(FILE_RENAME_LOG_FLAG).run(() ->
                    LogUtil.info("path = {}, no exist file, cost = {} ms", path, System.currentTimeMillis() - startTime));
            return reNameCount.get();
        }

        // 文件和目录集合（文件50一批异步，目录同步递归执行）
        List<File> fileList = Arrays.stream(dirFileList).filter(p -> p.exists() && p.isFile()).collect(Collectors.toList());
        List<File> dirList = Arrays.stream(dirFileList).filter(p -> p.exists() && p.isDirectory()).collect(Collectors.toList());

        // 异步 feature 集合
        List<CompletableFuture<Boolean>> futureList = Lists.newArrayList();

        // 所有文件按50个一批异步，文件夹同步
        Lists.partition(Optional.of(fileList).orElseGet(ArrayList::new), 50).forEach(subList -> {
            // 50个一组异步
            subList.forEach(file -> futureList.add(TaskUtil.supplyAsync(() -> FileRenameUtil.fileReNameInner(file, fileOld2NewNameEntryList, fileType, addReNameLogFile))));
            // 等待一组全部完成
            // TaskUtil.waitAnyException(futureList);
            futureList.forEach(future -> {
                // 进行统计改名成功数
                try {
                    if (future.get()) {
                        // 成功为true，改名成功数自增1
                        reNameCount.getAndIncrement();
                    }
                } catch (Exception e) {
                    LogUtil.warn("fileReName exception message = {}", e.getLocalizedMessage(), e);
                }
            });
        });

        // 当前目录下的所有文件（不包含文件夹）执行成功记录日志
        BooleanFunctionUtil.trueRunnable(FILE_RENAME_LOG_FLAG).run(() ->
                LogUtil.info("path = {}, execute end, reNameCount = {}, total cost = {} ms",
                        path, reNameCount.get(), System.currentTimeMillis() - startTime));

        BooleanFunctionUtil.trueRunnable(addReNameLogFile != null).run(() ->
                FileUtil.fileAppendData(addReNameLogFile, ">>>>>>>>>> current dirPath = " + path + ", reNameCount = "
                        + reNameCount + ", cost = " + (System.currentTimeMillis() - startTime) + " ms" + SystemUtil.getLineSeparator()));


        // 所有目录递归同步执行
        Optional.of(dirList).orElseGet(ArrayList::new).forEach(dirFile -> {
            // 递归执行，并统计数量
            reNameCount.addAndGet(fileReName(dirFile.getAbsolutePath(), fileOld2NewNameEntryList, fileType, addReNameLogFile));
        });


        return reNameCount.get();
    }


    /**
     * 单个文件重命名
     */
    protected static boolean fileReNameInner(File currentFile,
                                             List<DataEntry<String, String>> fileOld2NewNameEntryList,
                                             String fileType,
                                             File addReNameLogFile) {


        if (!currentFile.exists() || !currentFile.isFile()) {
            // 不是文件继续执行
            return false;
        }

        // fileType 要求文件类型（后缀）不为空，则判断文件类型是否一致，不一致则继续下一个文件
        if (fileType != null && !FileUtil.judgeFileTypeSuffix(currentFile, fileType)) {
            return false;
        }

        // 当前文件名（未改名前）
        String oldFileName = currentFile.getName();
        String newFileName = oldFileName;

        // 遍历更换map集合，顺序遍历，同时满足条件替换 eg: 123456.txt，替换map为  123 = abc, bc4 = def, 那么改名之后结果为: adef56.txt
        for (DataEntry<String, String> entry : Optional.ofNullable(fileOld2NewNameEntryList).orElseGet(ArrayList::new)) {
            // 替换成新的文件名
            newFileName = newFileName.replace(entry.getKey(), entry.getValue());
        }

        // todo
        if (newFileName.equals(oldFileName)) {
            // 新旧文件名相同，不改名
            return false;
        }

        // 更改文件名（todo：重试/告警）
        String newFileAllPathName = currentFile.getParent() + File.separator + newFileName;
        boolean reNameResult = currentFile.renameTo(new File(newFileAllPathName));

        // 记录日志
        BooleanFunctionUtil.trueRunnable(FILE_RENAME_LOG_FLAG).run(() ->
                LogUtil.info("oldFileName = {}, fileOld2NewNameEntryList = {}, newFileName = {}, reNameResult = {}",
                        oldFileName, JsonUtil.toJsonString(fileOld2NewNameEntryList), newFileAllPathName, reNameResult));

        // 追加记录到文件中（每次满足条件重命名都记录）
        BooleanFunctionUtil.trueRunnable(addReNameLogFile != null).run(() ->
                FileUtil.fileAppendData(addReNameLogFile,
                        oldFileName + StringConstantUtil.CHANGE_SEPARATOR + newFileAllPathName
                                + StringConstantUtil.stringAppendChineseMidBracket("fileOld2NewNameEntryList = " + JsonUtil.toJsonString(fileOld2NewNameEntryList) + ", reNameResult = " + reNameResult)
                                + ", " + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_MILL) + SystemUtil.getLineSeparator()));

        return reNameResult;
    }


}
