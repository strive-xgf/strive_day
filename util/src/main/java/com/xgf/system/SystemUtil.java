package com.xgf.system;

import com.xgf.system.bean.SystemEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author strive_day
 * @create 2021-07-10 20:59
 * @description 系统相关工具类
 */
public class SystemUtil {

    /**
     * 返回操作系统类型
     * @return
     */
    public static String systemType(){
        return System.getProperties().getProperty("os.name");
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

}
