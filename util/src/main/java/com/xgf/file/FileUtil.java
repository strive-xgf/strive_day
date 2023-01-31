package com.xgf.file;

import com.xgf.constant.StringConstantUtil;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.java8.BranchHandleUtil;
import com.xgf.java8.ThrowExceptionFunctionUtil;
import com.xgf.system.SystemUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Objects;
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
     * @return 项目根路径 > log > sysTimeLog > 年 > 月 > 日 + 时.txt
     */
    public static File createFileBySysTime() {
        return createFileBySysTime(null, null);
    }

    /**
     * 根据系统时间，生成文件及其目录（.txt文件）
     * @param basePath 根路径
     * @return （根路径basePath(为空则是项目根路径) > log > sysTimeLog > 年 > 月 > 日 + 时.txt
     */
    public static File createFileBySysTime(String basePath) {
        return createFileBySysTime(basePath, null);
    }


    /**
     * 根据系统时间，生成文件及其目录
     * @param basePath 根路径
     * @param fileFormat 文件格式，默认 txt 文件
     * @return （根路径basePath(为空则是项目根路径) > log > sysTimeLog > 年 > 月 > 日 + 时(.文件格式）
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
        basePath = basePath + appendFileSeparator(StringConstantUtil.LOG) + appendFileSeparator(StringConstantUtil.SYS_TIME_LOG)
                + appendFileSeparator(c.get(Calendar.YEAR))
                + appendFileSeparator(c.get(Calendar.MONTH) + 1) + appendFileSeparator(c.get(Calendar.DAY_OF_MONTH))
                + appendFileSeparator(c.get(Calendar.HOUR_OF_DAY))
                + StringConstantUtil.DOT + (StringUtils.isBlank(fileFormat) ? FILE_FORMAT : fileFormat);

        return createFileAndDir(basePath);
    }


    /**
     * 创建文件目录
     *
     * @param dirPath 文件目录路径
     * @return File
     */
    public static File createFileDir(String dirPath) {

        File file = new File(dirPath);

        if (!file.exists()) {
            // 创建目录
            if (!file.mkdirs()) {
                // 文件目录创建失败（多线程环境，可能被其它线程创建，校验文件是否存在，不存在再抛出异常
                ThrowExceptionFunctionUtil.isFalseThrow(file.exists()).throwMessage("文件目录创建异常，file path = " + dirPath);
            }
        }

        return file;
    }

    /**
     * 创建目录和文件
     *
     * @param url 路径url
     * @return File
     */
    public static File createFileAndDir(String url) {

        File file = new File(url);

        if (!file.getParentFile().exists()) {
            // 创建父级目录
            if (!file.getParentFile().mkdirs()) {
                // 文件目录创建失败（多线程环境，可能被其它线程创建，校验文件是否存在，不存在再抛出异常
                ThrowExceptionFunctionUtil.isFalseThrow(file.getParentFile().exists()).throwMessage("文件目录创建异常，file path = " + url);
            }
        }

        if (!file.exists()) {
            try {
                // 创建文件
                if (!file.createNewFile()) {
                    // 文件创建失败判断文件是否已存在，不存在抛出异常
                    ThrowExceptionFunctionUtil.isFalseThrow(file.exists()).throwMessage("文件创建异常，file path = " + url);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }


    /**
     * 文件路径，替换特殊字符为空
     *      windows 文件名不能包含下列任何字符:  \/:*?"<>|
     *
     * @param filePath 文件路径、
     * @return 替换后的文件路径
     */
    public static String replaceAllSpecialFileChar(String filePath) {

        if (StringUtils.isBlank(filePath)) {
            return filePath;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < filePath.length(); i++) {
            // 特殊文件字符替换为空
            char current = filePath.charAt(i);
            if (current == '\\'
                    || current == '/'
                    || current == '*'
                    || current == ':'
                    || current == '?'
                    || current == '"'
                    || current == '<'
                    || current == '>'
                    || current == '|') {
                continue;
            }
            sb.append(current);
        }

        return sb.toString();
    }


    /**
     * 追加文件分隔符
     * @param str 路径
     * @return 分隔符 + str
     */
    private static String appendFileSeparator(Object str) {
        if (Objects.isNull(str)) {
            return StringConstantUtil.EMPTY;
        }

        return SystemUtil.getFileSeparator() + String.valueOf(str);
    }


    /**
     * 向指定路径文件追加数据
     *
     * @param pathUrl 文件路径
     * @param data 数据
     */
    public static void fileAppendData(String pathUrl, String data) {
        fileAppendData(createFileAndDir(pathUrl), data);
    }

    /**
     * 向文件追加数据，路径不存在或非文件抛出异常
     *
     * @param file 文件
     * @param data 数据
     */
    public static void fileAppendDataThrow(File file, String data) {

        // 是文件记录到指定文件中，不是文件抛出异常
        BranchHandleUtil.isTrueOrFalse(file.exists() && file.isFile())
                .handle(() -> FileUtil.fileAppendData(file, data),
                        (() -> {
                            throw CustomExceptionEnum.FILE_ILLEGAL_EXCEPTION.generateCustomMessageException(file.getPath() + file.getName() + " 非法文件异常");
                        }));
    }


    /**
     * 向文件追加数据
     *
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

    public static void downloadImgByUrl(String urlStr, String filePath) throws Exception {
        FileUtil.downloadImgByUrl(urlStr, filePath, null);
    }

    /**
     * 通过 url 下载指定网页图片到存储路径
     *
     * @param urlStr           网页url地址
     * @param filePath         文件下载路径
     * @param reqHeadUserAgent 模拟请求头 user-agent 参数，避免403异常
     * @throws Exception 异常信息 （异常重试机制配置）
     */
    public static void downloadImgByUrl(String urlStr, String filePath, String reqHeadUserAgent) throws Exception {

        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection();
        // Server returned HTTP response code: 403 for URL（403 无权限），设置请求头 User-Agent 属性来模拟浏览器运行
        if (reqHeadUserAgent != null) {
            urlConnection.setRequestProperty("User-Agent", reqHeadUserAgent);
        }

        BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(FileUtil.createFileAndDir(filePath)));

        byte[] bytes = new byte[1024];
        int len;
        while ((len = bufferedInputStream.read(bytes)) != -1) {
            bufferedOutputStream.write(bytes, 0, len);
        }

        bufferedInputStream.close();
        bufferedOutputStream.close();
    }


    /**
     * 判断文件类型和目标类型是否一致（只是文件后缀的判断，没有判断具体的文件类型[更改文件后缀的情况]）
     *
     * @param file       文件
     * @param targetType 目标类型
     * @return true: 文件类型和目标类型一致
     */
    protected static boolean judgeFileTypeSuffix(File file, String targetType) {
        if (file == null || targetType == null) {
            return false;
        }

        if (!file.isFile()) {
            return false;
        }

        String fileName = file.getName();
        int dotSuffixIndex = fileName.lastIndexOf(".");

        if (dotSuffixIndex == -1) {
            return false;
        }
        return targetType.equals(fileName.substring(dotSuffixIndex + 1));
    }

}