package com.xgf.crawler.config;


import com.xgf.java8.ThrowExceptionFunctionUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author strive_day
 * @create 2023-01-23 12:10
 * @description Crawler 图片属性配置
 */

@Data
@Component
@ConfigurationProperties("custom.config.crawler.properties.img")
public class CrawlerImgProperties {

    /**
     * 基本属性类
     */
    @Autowired
    private CrawlerBaseProperties crawlerBaseProperties;

    /**
     * 图片格式 jpg/png等
     */
    private String format;

    /**
     * 图片路径前缀添加（eg: 网页相对路径 /xx/xx）
     */
    private String urlPreAppend;

    /**
     * 不包含的图片路径 url 集合
     */
    private String[] excludeUrls;

    /**
     * 校验和默认填充值，让大部分情况只需要 webPageUrlList 就可以下载
     */
    public void checkAndDefaultFill() {
        ThrowExceptionFunctionUtil.isTureThrow(crawlerBaseProperties == null).throwMessage("基本配置不能为空");
        crawlerBaseProperties.checkAndDefaultFill();
        format = StringUtils.defaultIfBlank(format, "jpg");
    }


}
