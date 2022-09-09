package com.xgf.system.bean;

/**
 * @author strive_day
 * @create 2021-07-10 21:26
 * @description 系统参数枚举
 */
public enum SystemEnum {
    WINDOWS("windows", "windows系统"),
    LINUX("linux", "linux系统"),
    UNIX("unix", "unix系统"),
    MAC_OS("mac os", "mac os系统"),
    ;

    private final String code;
    private final String value;

    SystemEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
