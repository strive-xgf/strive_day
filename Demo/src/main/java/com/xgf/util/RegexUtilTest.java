package com.xgf.util;

import com.xgf.regex.RegexUtil;
import org.junit.Test;

import java.io.File;

/**
 * @author strive_day
 * @create 2021-07-10 22:15
 * @description 正则表达式工具类测试
 */
public class RegexUtilTest {

    @Test
    public void testLegalPath(){

//        String path = "L://备份";
        String path = "L:\\备份\\000000";
        File file = new File(path);
        System.out.println(file);
        if(RegexUtil.isWindowsLegalPath(path) && RegexUtil.isLegalPath(path)){
            System.out.println(path + "：合法");
        }else {
            System.err.println(path + "：路径不合法");
        }
    }
}
