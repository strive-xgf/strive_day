package com.xgf.file;

import com.xgf.regex.RegexUtil;
import com.xgf.system.SystemUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author strive_day
 * @create 2021-07-10 17:02
 * @description 文件复制工具类
 */
public class FileCopyUtil {

    /**
     * 是否存在子文件
     * @param file 文件
     * @return true：该文件下存在子文件
     */
    public static Boolean existSubFile(File file){
        if(!file.exists()){
            throw new IllegalArgumentException("文件不存在");
        }
        if(file.isDirectory()){
            File[] subFiles = file.listFiles();
            if(Objects.isNull(subFiles)){
                return Boolean.FALSE;
            }
            for(File sub : subFiles){
                if(sub.isFile()){
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 是否存在子文件夹-目录
     * @param file 文件
     * @return true：该文件下存在子文件夹
     */
    public static Boolean existSubDirectory(File file){
        if(!file.exists()){
            throw new IllegalArgumentException("文件不存在");
        }
        if(file.isDirectory()){
            File[] subFiles = file.listFiles();
            if(Objects.isNull(subFiles)){
                return Boolean.FALSE;
            }
            for(File sub : subFiles){
                if(sub.isDirectory()){
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 是否存在子文件夹或者文件
     * @param file 文件
     * @return true：该文件下存在子文件夹
     */
    public static Boolean existSubFileOrDirectory(File file){
        if(!file.exists()){
            throw new IllegalArgumentException("文件不存在");
        }
        if(file.isDirectory()){
            File[] subFiles = file.listFiles();
            if(Objects.isNull(subFiles)){
                return Boolean.FALSE;
            }
            for(File sub : subFiles){
                if(sub.isDirectory() || sub.isFile()){
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }


    /**
     * 复制 - 复制原文件下的所有文件和目录（包含子文件和子目录下的所有）
     *
     * @param sourcePath 原路径
     * @param targetPath 目标路径
     * @return true：复制成功
     */
    public static boolean copyCurrentAndSubFileAndDirectory(String sourcePath, String targetPath) {
        long start = System.currentTimeMillis();
        // 校验入参合法性
        if (isLegalCopyFileParam(sourcePath, targetPath)) {
            File sourceFile = new File(sourcePath);
            File targetFile = new File(targetPath);
            boolean flag = copyCurrentAndSubFileAndDirectoryCore(sourceFile, targetFile);
            System.out.println("------ copyCurrentAndSubFileAndDirectory cost time ： " + (System.currentTimeMillis() - start) + " ms");
            return flag;
        }
        return false;
    }

    /**
     * 复制 - 复制原路径下的目录结构到目标路径下（只复制目录，不复制文件）
     *
     * @param sourcePath 原路径
     * @param targetPath 目标路径
     * @return true：复制成功
     */
    public static boolean copyCurrentAndSubDirectory(String sourcePath, String targetPath) {
        long start = System.currentTimeMillis();
        // 校验入参合法性
        if (isLegalCopyFileParam(sourcePath, targetPath)) {
            File sourceFile = new File(sourcePath);
            File targetFile = new File(targetPath);
            boolean flag = copyCurrentAndSubDirectoryCore(sourceFile, targetFile);
            System.out.println("------ copyCurrentAndSubDirectory cost time ： " + (System.currentTimeMillis() - start) + " ms");
            return flag;
        }
        return false;
    }


    /**
     * 递归 - 复制原文件下的所有文件和目录（包含子文件和子目录下的所有）
     *
     * @param sourceFile 原文件
     * @param targetFile 目标文件
     * @return 复制是否成功
     */
    private static boolean copyCurrentAndSubFileAndDirectoryCore(File sourceFile, File targetFile) {

        if (!sourceFile.exists()) {
            // 源文件不存在时抛出非法参数异常
            throw new IllegalArgumentException("sourceFile is not exist，sourceFile：" + sourceFile);
        }

        if (!targetFile.exists() || !targetFile.isDirectory()) {
            // 系统中不存在目标目录，或者不是一个目录，则创建一个 targetFile 目录。
            targetFile.mkdirs();
        }
        // 文件输入输出流
        FileInputStream fis = null;
        FileOutputStream fos = null;
        // 当前级别的目标子文件或子目录对象
        File targetChildFile = null;
        // 保存读取或写入流数据的字节数组
        byte[] bs = new byte[1024 * 1024];
        // 当前读取的字节长度
        int len = 0;
        try {
            if (sourceFile.isFile()) {
                // 源目录是一个普通文件，初始化源文件输入流
                fis = new FileInputStream(sourceFile);
                // 初始化目标文件对象
                targetChildFile = new File(targetFile, sourceFile.getName());
                if (!targetChildFile.exists()) {
                    // 目标文件子文件不存在，创建一个targetChildFile对象对应的目标文件
                    targetChildFile.createNewFile();
                }
                // 初始化目标文件的输出流
                fos = new FileOutputStream(targetChildFile);
                // 读数据
                while ((len = fis.read(bs)) != -1) {
                    // 写数据
                    fos.write(bs, 0, len);
                }
                // 关闭流
                close(fis, fos);
            } else if (sourceFile.isDirectory()) {
                // 源文件是目录，获取当前源目录下所有的下一级文件、下一级目录，保存在File数据中
                File[] childFiles = sourceFile.listFiles();

                // 递归处理：遍历当前目录下的文件、目录
                for (File childFile : childFiles) {
                    if (childFile.isFile()) {
                        fis = new FileInputStream(childFile);
                        targetChildFile = new File(targetFile, childFile.getName());
                        if (!targetChildFile.exists())
                            targetChildFile.createNewFile();
                        fos = new FileOutputStream(targetChildFile);
                        // 读入数据
                        while ((len = fis.read(bs)) != -1) {
                            // 向targetChildFile对象对应的文件上写入数据
                            fos.write(bs, 0, len);
                        }
                        // 将targetChildFile赋空值，方便垃圾回收器收回内存
                        targetChildFile = null;
                        // 关闭流
                        close(fis, fos);
                    } else if (childFile.isDirectory()) {
                        // 下级元素是一个目录时，递归调用该方法处理
                        targetChildFile = new File(targetFile.getAbsoluteFile() + SystemUtil.getFileSeparator() + childFile.getName());
                        // 在磁盘上创建一个targetChildFile对象对应的目录
                        if (!targetChildFile.exists()) {
                            targetChildFile.mkdirs();
                        }
                        if(existSubFileOrDirectory(childFile)){
                            // 存在子文件或目录，递归调用方法本身
                            copyCurrentAndSubFileAndDirectoryCore(childFile, targetChildFile);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 递归：复制原路径下的目录结构到目标路径下（只复制目录，不复制文件）
     *
     * @param sourceFile 原文件
     * @param targetFile 目标文件
     * @return 目录是否复制成功
     */
    private static boolean copyCurrentAndSubDirectoryCore(File sourceFile, File targetFile) {

        if (!sourceFile.exists()) {
            // 源文件不存在时抛出非法参数异常
            throw new IllegalArgumentException("sourceFile is not exist，sourceFile：" + sourceFile);
        }

        if (sourceFile.isDirectory()) {
            if (!targetFile.exists() || !targetFile.isDirectory()) {
                // 系统中不存在目标目录，或者不是一个目录，则创建一个 targetFile 目录
                targetFile.mkdirs();
            }
            // 当前级别的目标子文件或子目录对象
            File targetChildFile = null;

            // 源文件是目录，列出当前源目录下所有的下一级文件、下一级目录，保存在File数据中
            File[] childFiles = sourceFile.listFiles();

            // 递归处理：遍历当前级源目录的子文件、子目录，
            for (File childFile : childFiles) {
                // 下级元素是一个目录时，递归调用
                if (childFile.isDirectory()) {
                    // 初始化一个在当前级目标目录下，和当前级源目录子目录同名的目录对象
                    targetChildFile = new File(targetFile.getAbsoluteFile() + SystemUtil.getFileSeparator() + childFile.getName());
                    // 在磁盘上创建一个targetChildFile对象对应的目录
                    if (!targetChildFile.exists()) {
                        targetChildFile.mkdirs();
                    }
                    if(existSubDirectory(childFile)){
                        // 存在子目录，递归调用方法本身
                        copyCurrentAndSubDirectoryCore(childFile, targetChildFile);
                    }
                }
            }
        }
        return true;
    }


    /**
     * 关闭输入流
     *
     * @param iStream 输入流
     */
    public static void closeInputStream(InputStream... iStream) {
        Arrays.stream(iStream).forEach(i -> {
            try {
                i.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 关闭输出流
     * @param oStream
     */
    public static void closeOutputStream(OutputStream... oStream) {
        Arrays.stream(oStream).forEach(o -> {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 关闭流
     * @param closeable
     */
    public static void close(Closeable... closeable) {
        List<Closeable> closeableList = (List<Closeable>) Arrays.asList(closeable);
        if (CollectionUtils.isNotEmpty(closeableList)) {

            closeableList.stream().forEach(c -> {
                try {
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 复制文件路径是否合法
     * @param sourcePath 原文件路径
     * @param targetPath 目标文件路径
     * @return true：合法
     */
    private static Boolean isLegalCopyFileParam(String sourcePath, String targetPath) {
        if (StringUtils.isBlank(sourcePath)) {
            System.err.println("====== sourcePath can not be blank");
            return Boolean.FALSE;
        }
        if (StringUtils.isBlank(targetPath)) {
            System.err.println("====== targetPath can not be blank");
            return Boolean.FALSE;
        }
        if (!RegexUtil.isLegalPath(sourcePath)) {
            throw new NullPointerException("T====== sourcePath is illegal：" + sourcePath);
        }
        if (!RegexUtil.isLegalPath(targetPath)) {
            System.err.println("====== targetPath is not legal：" + targetPath);
            throw new NullPointerException("T====== targetPath is illegal：" + targetPath);
        }
        return Boolean.TRUE;
    }


}
