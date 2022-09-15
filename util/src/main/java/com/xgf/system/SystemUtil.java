package com.xgf.system;

import com.xgf.system.bean.SystemEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * @author strive_day
 * @create 2021-07-10 20:59
 * @description 系统相关工具类
 */
public class SystemUtil {

    private static final Properties systemProperty = System.getProperties();

    /**
     * 按入参获取系统属性值
     * @param param 属性名
     * @return
     */
    public static String getSystemProperty(String param){
        return systemProperty.getProperty(param);
    }

    /**
     * 返回操作系统名称
     * @return
     */
    public static String systemType(){
        return getSystemProperty("os.name");
    }

    /**
     * 当前操作系统是否是windows
     * @return
     */
    public static Boolean isWindows(){
        String system = systemType();
        return StringUtils.isNotBlank(system)
                ? system.toLowerCase().contains(SystemEnum.WINDOWS.getCode().toLowerCase())
                : Boolean.FALSE;
    }

    /**
     * 当前操作系统是否是Linux
     * @return
     */
    public static Boolean isLinux(){
        String system = systemType();
        return StringUtils.isNotBlank(system)
                ? system.toLowerCase().contains(SystemEnum.LINUX.getCode().toLowerCase())
                : Boolean.FALSE;
    }

    public static boolean judgeSystemType(SystemEnum systemEnum){
        String system = systemType();
        return StringUtils.isNotBlank(system)
                ? system.toLowerCase().contains(systemEnum.getCode().toLowerCase())
                : Boolean.FALSE;
    }


    /**
     * 获取行分隔符
     * @return windows 下是 \r\n，在Linux下面是 \n， 在Mac下是 \r
     */
    public static String getLineSeparator(){
        return getSystemProperty("line.separator");
    }

    /**
     * 获取路径分隔符 ;
     * @return
     */
    public static String getPathSeparator(){
        return getSystemProperty("path.separator");
    }

    /**
     * 文件分隔符
     * @return windows下是 \ ，在LInux下是 /
     */
    public static String getFileSeparator(){
        return getSystemProperty("file.separator");
    }

    /**
     * @return 获取当前工作目录
     */
    public static String getCurrentDir() {
        return System.getProperty("user.dir");
    }

}
