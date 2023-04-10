package com.xgf.file;

import com.google.common.collect.Lists;
import com.xgf.common.LogUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.constant.enumclass.FileTypeEnum;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.file.internal.ImageScaleUtil;
import com.xgf.java8.BooleanFunctionUtil;
import com.xgf.java8.ThrowExceptionFunctionUtil;
import com.xgf.system.SystemUtil;
import com.xgf.task.TaskUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author strive_day
 * @create 2023-04-08 1:00
 * @description 图片文件工具类
 */

public class ImageFileUtil {

    /**
     * 是否需要添加文件合并日志
     */
    private static Boolean FILE_IMAGE_FLAG = true;

    /**
     * 给静态变量 FILE_MERGE_LOG_FLAG 读取赋值，默认为 false
     */
    @Value("${custom.config.log.file.fileImageLogFlag:false}")
    public void setStaticFileReNameFlag(Boolean fileImageLogFlag) {
        ImageFileUtil.FILE_IMAGE_FLAG = fileImageLogFlag;
    }


    /**
     * 图片按照中心点进行缩放和裁剪，达到需要的图片宽高像素比
     *  根据目录层级来处理，目录下的子目录也会递归执行
     *
     * @param sourceDir    原图片文件目录（子目录也会被处理）
     * @param targetDir    目标图片文件目录
     * @param fileTypeEnum 处理的文件类型（eg: jpg, png, gif, bmp等) 只有同类型的文件才会被处理，null : 所有类型都处理
     * @param targetWidth      目标图片像素宽度
     * @param targetHeight     目标图片像素高度
     * @param dealOptimizeFlag 处理优化标识，true：处理优化，如果原图片宽高宽高比大，则自动替换宽和高的值
     *                         eg: 图片宽度: 1000，高度: 800，传入参数: targetWidth: 3840, targetHeight: 2160，那么会优化成：targetWidth: 2160, targetHeight: 3840，保证最佳展示
     * @param targetExistCover 目标文件存在是否覆盖，true：覆盖，false：不覆盖
     * @param deleteSourceFile 合并之后是否删除合并前的所有文件（true：删除，flase：不删除）
     * @throws IOException IO异常
     */
    public static void imgDpiModifyByDir(String sourceDir, String targetDir, FileTypeEnum fileTypeEnum,
                                         Integer targetWidth, Integer targetHeight,
                                         Boolean dealOptimizeFlag, Boolean targetExistCover, Boolean deleteSourceFile) {

        if (StringConstantUtil.isBlank(sourceDir) || StringConstantUtil.isBlank(targetDir)) {
            throw CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION.generateException();
        }


        File sourceDirFile = new File(sourceDir);
        ThrowExceptionFunctionUtil.isFalseThrow(sourceDirFile.exists() && sourceDirFile.isDirectory())
                .throwMessage("目录路径：" + sourceDir + "，" + (sourceDirFile.exists() ? "不是一个目录文件" : "目录不存在"));

        List<File> allFileList = Arrays.asList(Optional.ofNullable(sourceDirFile.listFiles()).orElseGet(() ->new File[0]));

        // 目录下的目录递归执行
        allFileList.stream().filter(File::isDirectory).forEach(file ->
                imgDpiModifyByDir(file.getPath(), StringConstantUtil.defaultEndWith(targetDir, SystemUtil.getFileSeparator()) + file.getName(), fileTypeEnum,
                        targetWidth, targetHeight, dealOptimizeFlag, targetExistCover, deleteSourceFile));


        // 获取指定类型的文件列表，进行缩放裁剪
        List<File> sourceFileList = allFileList.stream().filter(File::isFile)
                .filter(file -> fileTypeEnum == null || file.getName().endsWith(fileTypeEnum.getFileTypeAddDot())).collect(Collectors.toList());

        // 图片缩放裁剪,分批并行30一组进行裁剪，提效
        Lists.partition(sourceFileList, 30).forEach(subSourceFileList -> {
            // 异步future集合
            List<CompletableFuture<?>> futureList = new ArrayList<>(30);
            subSourceFileList.forEach(sourceFile -> {
                try {
                    imgDpiModify(sourceFile, new File(StringConstantUtil.defaultEndWith(targetDir, SystemUtil.getFileSeparator()) + sourceFile.getName()),
                            targetWidth, targetHeight, dealOptimizeFlag, targetExistCover, deleteSourceFile);
                } catch (Exception e) {
                   LogUtil.warn("execute exception = {}", e);
                }
            });

            // 等待完成
            TaskUtil.waitAnyException(futureList);
        });

    }




    public static void imgDpiModify(String sourceImgPath, String targetImgPath, Integer targetWidth, Integer targetHeight, Boolean dealOptimizeFlag) throws IOException {
        imgDpiModify(new File(sourceImgPath), new File(targetImgPath), targetWidth, targetHeight, dealOptimizeFlag, false, false);
    }


    /**
     * 图片按照中心点进行缩放和裁剪，达到需要的图片宽高像素比
     *
     * @param sourceImgFile    原图片文件
     * @param targetImgFile    目标图片文件
     * @param targetWidth      目标图片像素宽度
     * @param targetHeight     目标图片像素高度
     * @param dealOptimizeFlag 处理优化标识，true：处理优化，如果原图片宽高宽高比大，则自动替换宽和高的值
     *                         eg: 图片宽度: 1000，高度: 800，传入参数: targetWidth: 3840, targetHeight: 2160，那么会优化成：targetWidth: 2160, targetHeight: 3840，保证最佳展示
     * @param targetExistCover 目标文件存在是否覆盖，true：覆盖，false：不覆盖
     * @param deleteSourceFile 合并之后是否删除合并前的所有文件（true：删除，flase：不删除）
     * @return true: 成功，false: 失败
     * @throws IOException IO异常
     */
    public static boolean imgDpiModify(File sourceImgFile, File targetImgFile, Integer targetWidth, Integer targetHeight, Boolean dealOptimizeFlag, Boolean targetExistCover, Boolean deleteSourceFile) throws IOException {

        if (BooleanUtils.isNotTrue(targetExistCover) && targetImgFile.exists()) {
            // 目标文件存在，且不能覆盖，则返回
            BooleanFunctionUtil.trueRunnable(FILE_IMAGE_FLAG).run(() ->
                    LogUtil.info("targetFilePath = {} exist no cover", targetImgFile.getPath()));
            return false;
        }
        // 目标文件，不存在则创建
        FileUtil.createFileAndDir(targetImgFile);

        BufferedImage sourceBufImg = ImageIO.read(sourceImgFile);
        int originalWidth = sourceBufImg.getWidth();
        int originalHeight = sourceBufImg.getHeight();

        // 优化宽高比
        if (Boolean.TRUE.equals(dealOptimizeFlag)) {
            // 原图片宽度比高度大，那么应该满足: targetWidth >= targetHeight，如果不满足，则替换，反之，原图片宽度比高度小，那么应该满足: targetWidth <= targetHeight，否则替换
            if ((originalWidth > originalHeight && targetWidth < targetHeight)
                    || (originalWidth < originalHeight && targetWidth > targetHeight)) {
                Integer temp = targetWidth;
                targetWidth = targetHeight;
                targetHeight = temp;
            }
        }


        // 计算图片需要的缩放比例（宽高和原宽高做对比，取大值）【就是满足目标宽度和高度，需要对图片进行的缩放比】
        double imgScaleValue = Math.max((double) targetWidth / originalWidth, (double) targetHeight / originalHeight);
        // 缩放后的图片宽度
        int scaledImgWidth = (int) Math.round(originalWidth * imgScaleValue);
        // 缩放后的图片高度
        int scaledImgHeight = (int) Math.round(originalHeight * imgScaleValue);

        // 取中间区域的左上角坐标x，y开始截图下标【缩放后变更为要求的图片比例尺寸，进行截取的开始坐标】
        int xStartIndex = (scaledImgWidth - targetWidth) / 2;
        int yStartIndex = (scaledImgHeight - targetHeight) / 2;

        // 对图像进行缩放，获取缩放后的图片BufferedImage
        BufferedImage scaleBufImg = ImageScaleUtil.scale(sourceBufImg, scaledImgWidth, scaledImgHeight);

        // 对缩放后的图片，从中间区域开始截取（用于获取原始图像中指定区域的子图像），达到希望得到的目标图片的宽高比例
        // 参数分别是：截取图像左上角的 x 坐标、y 坐标， 截取图像的宽度、高度
        BufferedImage croppedTargetBufImg = scaleBufImg.getSubimage(xStartIndex, yStartIndex, targetWidth, targetHeight);

        // 将截取好的目标图像写入到目标文件中，参数1 (RenderedImage): 要写入文件或输出流的图像数据。
        // 参数2 (formatName): 指定要写入的图像格式的名称，如 jpg、png、bmp、gif，（这里默认使用JPG），告知 ImageIO 使用哪个图像编解码器来处理图像数据
        // 参数3 (output): 要写入的文件或输出流。可以是一个 File 对象或 OutputStream 对象
        boolean writeFlag = ImageIO.write(croppedTargetBufImg, FileTypeEnum.JPG.getCode(), targetImgFile);

        BooleanFunctionUtil.trueRunnable(FILE_IMAGE_FLAG).run(() ->
                LogUtil.info("====== sourcePath = {}, targetPath = {}, result success = {}", sourceImgFile.getPath(), targetImgFile.getPath(), writeFlag));

        // 删除文件
        BooleanFunctionUtil.trueRunnable(deleteSourceFile).run(sourceImgFile::delete);

        return writeFlag;
    }

}

