package com.xgf.file;

import com.xgf.common.LogUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.date.DateUtil;
import com.xgf.java8.BooleanFunctionUtil;
import com.xgf.java8.ThrowExceptionFunctionUtil;
import com.xgf.system.SystemUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.Map;

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
    private static Boolean FILE_RENAME_LOG_FLAG;

    /**
     * 给静态变量 FILE_RENAME_LOG_FLAG 读取赋值，默认为 false
     */
    @Value("${custom.config.log.file.fileReNameLogFlag:false}")
    public void setStaticFileReNameFlag(Boolean fileReNameLogFlag) {
        FileRenameUtil.FILE_RENAME_LOG_FLAG = fileReNameLogFlag;
    }


    /**
     * @param path               目标路径下的所有文件进行更名（找到最内部文件夹先改名）
     * @param fileOld2NewNameMap 替换文件名Map，key: 原文件名称需要被替换的字符串，value: 新文件名替换的字符串，为 null 则不执行
     * @param fileType           文件类型 eg: .txt, .mp4【只是根据当前文件后缀来区分，不区分文件具体是什么类型（改后缀名，效率太低）】
     *                           传 null 则为所有文件
     * @param addReNameLogFile   记录更改文件名称的记录日志到参数文件中
     * @return 更改文件数量
     */
     protected static int fileReName(String path,
                                  Map<String, String> fileOld2NewNameMap,
                                  String fileType,
                                  File addReNameLogFile) {

        ThrowExceptionFunctionUtil.isTureThrow(MapUtils.isEmpty(fileOld2NewNameMap)).throwMessage("重命名字段映射map不能为空");
        // 重命名文件数
        int reNameCount = 0;
        long startTime = System.currentTimeMillis();

        // 获取目录下的所有文件夹
        File[] dirFileList = new File(path).listFiles();
        if (dirFileList == null) {
            BooleanFunctionUtil.trueRunnable(FILE_RENAME_LOG_FLAG).run(() ->
                    LogUtil.info("path = {}, no exist file, cost = {} ms", path, System.currentTimeMillis() - startTime));
            return reNameCount;
        }

        // 遍历路径下所有文件信息
        for (File currentFile : dirFileList) {

            // 是文件夹目录，递归执行
            if (currentFile.isDirectory()) {
                // 异步执行 todo
//                TaskUtil.runAsync(() ->
//                        fileReName(currentFile.getAbsolutePath(), fileOld2NewNameMap, fileType));
                reNameCount += fileReName(currentFile.getAbsolutePath(), fileOld2NewNameMap, fileType, addReNameLogFile);
            }

            if (!currentFile.isFile()) {
                // 不是文件继续执行
                continue;
            }

            // 当前文件名（未改名前）
            String oldFileName = currentFile.getName();

            // fileType 要求文件类型（后缀）不为空，则判断文件类型是否一致，不一致则继续下一个文件
            if (fileType != null && !FileUtil.judgeFileTypeSuffix(currentFile, fileType)) {
                continue;
            }

            // 遍历更换map集合
            for (Map.Entry<String, String> old2NewEntry : fileOld2NewNameMap.entrySet()) {

                // 需要替换的旧的文件名中的字符串
                String replaceName = old2NewEntry.getKey();
                // 找到对应文件中是否包含该字符串，并返回下标位置
                int existIndex = oldFileName.indexOf(replaceName);
                if (existIndex == -1) {
                    // 不存在继续遍历
                    continue;
                }

                //  更改文件名（路径 + 原文件名字符串（并替换指定字符串）
                String newFileName = currentFile.getParent() + File.separator +
                        oldFileName.substring(0, existIndex) + old2NewEntry.getValue() + oldFileName.substring(existIndex + replaceName.length());

                // 更改文件名（todo：重试/告警）
                boolean reNameResult = currentFile.renameTo(new File(newFileName));
                reNameCount++;

                // 记录日志
                Integer finalReNameCount = reNameCount;
                BooleanFunctionUtil.trueRunnable(FILE_RENAME_LOG_FLAG).run(() ->
                        LogUtil.info("oldFileName = {}, replaceName = {}, newFileName = {}, currentReNameCount = {}, reNameResult = {}",
                                oldFileName, replaceName, newFileName, finalReNameCount, reNameResult));

                // 追加记录到文件中
                BooleanFunctionUtil.trueRunnable(addReNameLogFile != null).run(() ->
                                FileUtil.fileAppendData(addReNameLogFile,
                                        oldFileName + StringConstantUtil.CHANGE_SEPARATOR + newFileName
                                                + StringConstantUtil.stringAppendChineseMidBracket("replaceName = " + replaceName + ", reNameResult = " + reNameResult + ", currentReNameCount = " + finalReNameCount)
                                                + "," + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_MILL) + SystemUtil.getLineSeparator()));

            }
        }

        int finalReNameCount = reNameCount;
        BooleanFunctionUtil.trueRunnable(FILE_RENAME_LOG_FLAG).run(() ->
                LogUtil.info("path = {}, execute end, reNameCount = {}, total cost = {} ms",
                        path, finalReNameCount, System.currentTimeMillis() - startTime));


        return reNameCount;
    }



}
