package com.xgf.regex;

import com.xgf.regex.bean.RegexEnum;
import com.xgf.system.SystemUtil;

/**
 * @author strive_day
 * @create 2021-07-10 18:14
 * @description 正则表达式工具类
 */
public class RegexUtil {

    /**
     * 判断参数是否符合正则表达式
     * @param param 参数
     * @param regex 正则表达式
     * @return 返回bool值
     */
    public static Boolean conformRegex(String param, String regex){
        return param.matches(regex);
    }

    /**
     * 判断文件路径是否符合windows格式
     * @param param 文件路径
     * @return
     */
    public static Boolean isWindowsLegalPath(String param){
        return conformRegex(param, RegexEnum.WINDOWS_PATH_REGEX.getRegex());
    }

    /**
     * 判断文件路径是否符合Linux格式
     * @param param 文件路径
     * @return
     */
    public static Boolean isLinuxLegalPath(String param){
        return conformRegex(param, RegexEnum.LINUX_PATH_REGEX.getRegex());
    }

    /**
     * 判断文件路径是否合法
     * @param param 文件路径
     * @return
     */
    public static Boolean isLegalPath(String param){
        if(SystemUtil.isWindows()){
            return conformRegex(param, RegexEnum.WINDOWS_PATH_REGEX.getRegex());
        }else if(SystemUtil.isLinux()){
            return conformRegex(param, RegexEnum.LINUX_PATH_REGEX.getRegex());
        }
        System.err.println("system type currently only supported：windows and linux");
        return Boolean.FALSE;
    }

}
