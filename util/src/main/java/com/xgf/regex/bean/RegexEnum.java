package com.xgf.regex.bean;

/**
 * @author strive_day
 * @create 2021-07-10 17:39
 * @description 正则表达式枚举
 */

public enum RegexEnum {
    /**
     * 正则表达式判断文件路径是否合法（文件路径 /）：^[a-zA-Z]:(//[^///:"<>/|]+)+$  （末尾不能有/）
     */
    LINUX_PATH_REGEX("linuxPathRegex", "^[a-zA-Z]:(//[^///:\"<>/|]+)+$", "Linux文件路径正则表达式"),
    /**
     * windows匹配文件路径合法性（文件路径 \ ）
     */
    //  ^[A-z]:\\\\  ：  匹配绝对路径最前面的盘符名称，比如D:\（三个\来做转义）
    //  ([^|><?*\":\\/]*\\\\)*  ： 匹配的是D:\之后的路径地址，匹配除了 |><?*":\/ 这些Windows文件不允许出现的字符外的所有字符加上\的零次或多次（[]里的\只需要一个\就可以转义）
    //  ([^|><?*\":\\/]*)?$  ：  特殊处理，匹配以仅以一个文件夹名结尾的字符串零次或一次；，如果没有这个，只能匹配到 文件夹+\ ，最后的文件夹如果不带\，则无法匹配
    WINDOWS_PATH_REGEX("windowsPathRegex", "^[A-z]:\\\\([^|><?*\":\\/]*\\\\)*([^|><?*\":\\/]*)?$", "windows文件路径正则表达式")
    ;

    private final String code;
    private final String regex;
    private final String description;

    RegexEnum(String code, String regex, String description) {
        this.code = code;
        this.regex = regex;
        this.description = description;
    }

    public String getRegex() {
        return this.regex;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCode(){
        return this.code;
    }
}

