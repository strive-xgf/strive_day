package com.xgf.file;

import com.xgf.common.LogUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.constant.enumclass.FileTypeEnum;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.java8.BooleanFunctionUtil;
import com.xgf.java8.ThrowExceptionFunctionUtil;
import com.xgf.system.SystemUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author strive_day
 * @create 2023-04-05 21:37
 * @description 文件合并工具类
 */
public class FileMergeUtil {

    /**
     * 是否需要添加文件合并日志
     */
    private static Boolean FILE_MERGE_LOG_FLAG = true;

    /**
     * 给静态变量 FILE_MERGE_LOG_FLAG 读取赋值，默认为 false
     */
    @Value("${custom.config.log.file.fileMergeLogFlag:false}")
    public void setStaticFileReNameFlag(Boolean fileMergeLogFlag) {
        FileMergeUtil.FILE_MERGE_LOG_FLAG = fileMergeLogFlag;
    }


    /**
     * 合并指定目录下的指定类型文件到目标文件下【合并所有文件（目录下的目录也处理）】
     *
     * @param sourceDir      需要合并的文件目录（合并这个目录下的文件）
     * @param sourceFileType 需要合并的文件类型后缀（eg：txt、mp4、mp3等等）
     * @param targetDir      合并后的目标文件目录（合并后的文件存储目录）
     * @return true：合并成功，false：失败
     */
    public static void mergeAllFile(String sourceDir, FileTypeEnum sourceFileType, String targetDir) {
        FileMergeUtil.mergeAllFile(sourceDir, sourceFileType, targetDir, null, Boolean.FALSE, Boolean.FALSE);
    }

    /**
     * 合并指定目录下的指定类型文件到目标文件下【合并所有文件（目录下的目录也处理）】
     *
     * @param sourceDir        需要合并的文件目录（合并这个目录下的文件）
     * @param sourceFileType   需要合并的文件类型后缀（eg：txt、mp4、mp3等等）
     * @param targetDir        合并后的目标文件目录（合并后的文件存储目录）
     * @param targetFileType   合并后的文件类型后缀（eg：txt、mp4、mp3等等）
     * @param targetExistCover 目标文件存在是否覆盖（targetDir+targetFileName+targetFileType），true：覆盖，false：不覆盖
     * @param deleteSourceFile 合并之后是否删除合并前的所有文件（true：删除，flase：不删除）
     * @return true：合并成功，false：失败
     */
    public static void mergeAllFile(String sourceDir, FileTypeEnum sourceFileType,
                                    String targetDir, FileTypeEnum targetFileType,
                                    Boolean targetExistCover, Boolean deleteSourceFile) {

        // 合并当前目录下的文件
        try {
            FileMergeUtil.mergeSingleFile(sourceDir, sourceFileType, targetDir, null, targetFileType, targetExistCover, deleteSourceFile);
        } catch (Exception e) {
            BooleanFunctionUtil.trueRunnable(FILE_MERGE_LOG_FLAG).run(() -> LogUtil.warn("exist exception : {}", e));
        }

        // 获取目录下的所有文件夹，递归调用执行
        Arrays.stream(Optional.ofNullable(new File(sourceDir).listFiles()).orElseGet(() -> new File[0]))
                .filter(File::isDirectory)
                .forEach(file -> FileMergeUtil.mergeAllFile(
                        file.getPath(),
                        sourceFileType,
                        StringConstantUtil.defaultEndWith(targetDir, SystemUtil.getFileSeparator()) + file.getName(),
                        targetFileType,
                        targetExistCover,
                        deleteSourceFile
                ));
    }


    /**
     * 合并指定目录下的指定类型文件到目标文件下【合并单个文件（目录下的目录不处理）】
     *
     * @param sourceDir      需要合并的文件目录（合并这个目录下的文件）
     * @param sourceFileType 需要合并的文件类型后缀（eg：txt、mp4、mp3等等）
     * @param targetDir      合并后的目标文件目录（合并后的文件存储目录）
     * @return true：合并成功，false：失败
     */
    public static boolean mergeSingleFile(String sourceDir, FileTypeEnum sourceFileType, String targetDir) {
        return FileMergeUtil.mergeSingleFile(sourceDir, sourceFileType, targetDir, null);
    }

    /**
     * 合并指定目录下的指定类型文件到目标文件下【合并单个文件（目录下的目录不处理）】
     *
     * @param sourceDir      需要合并的文件目录（合并这个目录下的文件）
     * @param sourceFileType 需要合并的文件类型后缀（eg：txt、mp4、mp3等等）
     * @param targetDir      合并后的目标文件目录（合并后的文件存储目录）
     * @param targetFileType 合并后的文件类型后缀（eg：txt、mp4、mp3等等）
     * @return true：合并成功，false：失败
     */
    public static boolean mergeSingleFile(String sourceDir, FileTypeEnum sourceFileType, String targetDir, FileTypeEnum targetFileType) {
        return FileMergeUtil.mergeSingleFile(sourceDir, sourceFileType, targetDir, null, targetFileType, Boolean.FALSE, Boolean.FALSE);
    }

    /**
     * 合并指定目录下的指定类型文件到目标文件下【合并单个文件（目录下的目录不处理）】
     * todo：分而治之优化，并行优化
     *
     * @param sourceDir        需要合并的文件目录（合并这个目录下的文件）
     * @param sourceFileType   需要合并的文件类型后缀（eg：txt、mp4、mp3等等）
     * @param targetDir        合并后的目标文件目录（合并后的文件存储目录）
     * @param targetFileName   合并后的目标文件名（合并后的文件名称，如果为null，则为sourceDir的目录名称）
     * @param targetFileType   合并后的文件类型后缀（eg：txt、mp4、mp3等等）
     * @param targetExistCover 目标文件存在是否覆盖（targetDir+targetFileName+targetFileType），true：覆盖，false：不覆盖
     * @param deleteSourceFile 合并之后是否删除合并前的所有文件（true：删除，flase：不删除）
     * @return true：合并成功，false：失败
     */
    public static boolean mergeSingleFile(String sourceDir, FileTypeEnum sourceFileType,
                                          String targetDir, String targetFileName, FileTypeEnum targetFileType,
                                          Boolean targetExistCover, Boolean deleteSourceFile) {

        if (StringConstantUtil.isBlank(sourceDir) || sourceFileType == null || StringConstantUtil.isBlank(targetDir)) {
            throw CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION.generateException();
        }

        long startTime = System.currentTimeMillis();
        File sourceDirFile = new File(sourceDir);
        ThrowExceptionFunctionUtil.isFalseThrow(sourceDirFile.exists() && sourceDirFile.isDirectory())
                .throwMessage("目录路径：" + sourceDir + "，" + (sourceDirFile.exists() ? "不是一个目录文件" : "目录不存在"));

        // 文件名，为空，则取sourceDir 目录名
        targetFileName = targetFileName == null ? sourceDirFile.getName() : targetFileName;
        // 目标文件类型为空，则和sourceFileType相同
        targetFileType = targetFileType == null ? sourceFileType : targetFileType;
        // 文件存储路径
        String targetFilePath = StringConstantUtil.defaultEndWith(targetDir, SystemUtil.getFileSeparator()) + targetFileName + targetFileType.getFileTypeAddDot();
        if (BooleanUtils.isNotTrue(targetExistCover) && new File(targetFilePath).exists()) {
            // 目标文件存在，且不能覆盖，则返回
            BooleanFunctionUtil.trueRunnable(FILE_MERGE_LOG_FLAG).run(() ->
                    LogUtil.info("targetFilePath = {} exist no cover", targetFilePath));
            return false;
        }

        // 获取指定类型的文件列表
        File[] files = sourceDirFile.listFiles((dir1, name) -> name.endsWith(sourceFileType.getFileTypeAddDot()));
        if (files == null || files.length == 0) {
            BooleanFunctionUtil.trueRunnable(FILE_MERGE_LOG_FLAG).run(() ->
                    LogUtil.info("sourceDir = {}, no exist {} type file", sourceDir, sourceFileType.getCode()));
            return false;
        }

        File targetFile = FileUtil.createFileAndDir(targetFilePath);
        // 文件合并（try内自动关闭资源）
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile))) {
            for (File file : files) {
                try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
                    byte[] buffer = new byte[4096];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                }
            }
        } catch (IOException e) {
            BooleanFunctionUtil.trueRunnable(FILE_MERGE_LOG_FLAG).run(() -> LogUtil.warn("exist exception : {}", e));
            return false;
        }

        // 记录日志
        String finalTargetFileName = targetFileName;
        BooleanFunctionUtil.trueRunnable(FILE_MERGE_LOG_FLAG).run(() ->
                LogUtil.info("success >>> sourceDir = {} , sourceType = {}, targetFileName = {}, merge filesCount = {}, targetFileSize = {}, cost {} ms ",
                        sourceDir, sourceFileType.getCode(), finalTargetFileName,
                        StringConstantUtil.convertFileSize(BigDecimal.valueOf(targetFile.length()), 2),
                        targetFile.length(), System.currentTimeMillis() - startTime));

        // 删除文件
        BooleanFunctionUtil.trueRunnable(deleteSourceFile).run(() -> Arrays.asList(files).forEach(File::delete));

        return true;
    }

}
