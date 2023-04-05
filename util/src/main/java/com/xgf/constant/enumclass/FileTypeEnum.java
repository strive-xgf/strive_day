package com.xgf.constant.enumclass;

import com.xgf.constant.EnumBase;
import com.xgf.constant.StringConstantUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author strive_day
 * @create 2023-04-05 21:59
 * @description 文件类型枚举
 */

@Getter
@AllArgsConstructor
public enum FileTypeEnum implements EnumBase {


    /**
     * 文本文件
     */
    TXT("txt", "txt文本文件"),

    /**
     * 图片文件
     */
    JPG("jpg", "jpg图像文件"),
    PNG("png", "png图像文件"),
    GIF("gif", "gif图像文件"),
    BMP("bmp", "bmp图像文件"),

    /**
     * 音频文件
     */
    MP3("mp3", "mp3音频文件"),
    WAV("wav", "wav音频文件"),
    FLAC("flac", "flac音频文件"),
    AAC("aac", "aac音频文件"),

    /**
     * 视频文件
     */
    MP4("mp4", "mp4视频文件"),
    AVI("avi", "avi视频文件"),
    MOV("mov", "mov视频文件"),
    WMV("wmv", "wmv视频文件"),
    TS("ts", "ts视频文件"),

    /**
     * 压缩文件
     */
    ZIP("zip", "zip压缩文件"),
    RAR("rar", "rar压缩文件"),
    SEVENZ("7z", "7z压缩文件"),

    /**
     * 可执行文件
     */
    EXE("exe", "exe可执行文件"),
    DLL("dll", "dll动态链接库"),

    /**
     * 数据库文件
     */
    MDB("mdb", "mdb数据库文件"),
    ACCDB("accdb", "accdb数据库文件"),
    SQL("sql", "sql数据库文件"),

    /**
     * 网页文件
     */
    HTML("html", "html网页文件"),
    CSS("css", "css网页文件"),
    JS("js", "js网页文件"),

    /**
     * 代码文件
     */
    C("c", "c代码文件"),
    CPP("cpp", "cpp代码文件"),
    JAVA("java", "java代码文件"),
    PY("py", "py代码文件"),

    /**
     * PDF文件
     */
    PDF("pdf", "pdf文件"),

    /**
     * Word文件
     */
    DOC("doc", "doc Word文件"),
    DOCX("docx", "docx Word文件"),

    /**
     * 表格文件
     */
    XLS("xls", "xls表格文件"),
    XLSX("xlsx", "xlsx表格文件"),

    /**
     * 幻灯片文件
     */
    PPT("ppt", "ppt幻灯片文件"),
    PPTX("pptx", "pptx幻灯片文件"),

    /**
     * 软件安装包
     */
    MSI("msi", "msi安装包"),
    DMG("dmg", "dmg安装包"), PKG("pkg", "pkg安装包"),

    /**
     * 日志文件
     */
    LOG("log", "log日志文件"),

    /**
     * 字体文件
     */
    TTF("ttf", "ttf字体文件"),
    OTF("otf", "otf字体文件"),

    /**
     * 数据文件
     */
    CSV("csv", "csv数据文件"),
    JSON("json", "json数据文件"),
    XML("xml", "xml数据文件"),

    /**
     * 邮件文件
     */
    EML("eml", "eml邮件文件"),

    /**
     * 资源文件
     */
    RES("res", "res资源文件"),
    ICO("ico", "ico图标文件"),

    /**
     * 系统文件
     */
    SYS("sys", "sys系统文件"),
    ;


    private String code;
    private String value;

    public static FileTypeEnum getByCode(String code) {
        return Stream.of(FileTypeEnum.values()).filter(p -> p.getCode().equals(code)).findFirst().orElse(null);
    }

    public static FileTypeEnum getByValue(String value) {
        return Stream.of(FileTypeEnum.values()).filter(p -> p.getValue().equals(value)).findFirst().orElse(null);
    }

    /**
     * @return 获取文件类型（添加前置.点）
     */
    public String getFileTypeAddDot() {
        return StringConstantUtil.defaultStartWith(this.getCode(), StringConstantUtil.DOT);
    }
}
