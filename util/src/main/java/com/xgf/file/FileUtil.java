package com.xgf.file;

import com.xgf.constant.StringConstantUtil;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.system.SystemUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author xgf
 * @create 2022-05-03 09:45
 * @description 通用文件工具
 **/

public class FileUtil {



    /**
     * 文件格式
     */
    public static String FILE_FORMAT = "txt";

    /**
     * 根据系统时间，生成文件及其目录（.txt文件, 目录:项目根路径）
     * @return 项目根路径 > log > 年 > 月 > 日 + 时.txt
     */
    public static File createFileBySysTime() {
        return createFileBySysTime(null, null);
    }

    /**
     * 根据系统时间，生成文件及其目录（.txt文件）
     * @param basePath 根路径
     * @return （根路径basePath(为空则是项目根路径) > log > 年 > 月 > 日 + 时.txt
     */
    public static File createFileBySysTime(String basePath) {
        return createFileBySysTime(basePath, null);
    }


    /**
     * 根据系统时间，生成文件及其目录
     * @param basePath 根路径
     * @param fileFormat 文件格式，默认 txt 文件
     * @return （根路径basePath(为空则是项目根路径) > log > 年 > 月 > 日 + 时(.文件格式）
     */
    public static File createFileBySysTime(String basePath, String fileFormat) {
        if (StringUtils.isBlank(basePath)) {
            try {
                // 获取项目根路径
                basePath = new File(StringConstantUtil.EMPTY).getCanonicalPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 获取东八区时间
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));

        // 拼装路径 （根路径 > log > 年 > 月 > 日) + 时.txt
        basePath = basePath + appendFileSeparator("log") + appendFileSeparator(c.get(Calendar.YEAR))
                + appendFileSeparator(c.get(Calendar.MONTH) + 1) + appendFileSeparator(c.get(Calendar.DAY_OF_MONTH))
                + appendFileSeparator(c.get(Calendar.HOUR_OF_DAY))
                + StringConstantUtil.DOT + (StringUtils.isBlank(fileFormat) ? FILE_FORMAT : fileFormat);

        File file = new File(basePath);
        if (!file.getParentFile().exists()) {
            // 创建父级目录
            if (!file.getParentFile().mkdirs()) {
                throw CustomExceptionEnum.FILE_CREATE_EXCEPTION.generateException("dir path = " + basePath);
            }
        }

        if (!file.exists()) {
            // 创建文件
            try {
                if (!file.createNewFile()) {
                    throw CustomExceptionEnum.FILE_CREATE_EXCEPTION.generateException("file path = " + basePath);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }


    /**
     * 追加文件分隔符
     * @param str 路径
     * @return 分隔符 + str
     */
    private static String appendFileSeparator(Object str) {
        return SystemUtil.getFileSeparator() + String.valueOf(str);
    }

    /**
     * 向文件追加数据
     * @param file 文件
     * @param data 数据
     */
    public static void fileAppendData(File file, String data) {

        // JDK1.7 之后，try语句内创建的流将会自动关闭，不需要显式的关闭流 close
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(data);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}