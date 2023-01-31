package com.xgf.crawler;

import com.google.common.collect.Lists;
import com.xgf.common.LogUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.crawler.config.CrawlerImgProperties;
import com.xgf.crawler.inner.CrawlerJsoupCommonUtil;
import com.xgf.crawler.inner.CrawlerDocumentUtil;
import com.xgf.crawler.inner.CrawlerImgUtil;
import com.xgf.date.DateUtil;
import com.xgf.file.FileUtil;
import com.xgf.system.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author strive_day
 * @create 2023-01-27 23:28
 * @description 爬虫工具类
 */

@Component
public class CrawlerUtilClient {

    @Autowired
    private CrawlerImgUtil crawlerImgUtil;


    /**
     * 使用 selenium 通过配置属性，打开网页，然后下载网页图片
     */
    public void downloadImageByConfig() {
        crawlerImgUtil.downloadImageOpenWebPage();
    }

    /**
     * 使用 selenium 通过属性，下载网页上的图片
     *
     * @param crawlerImgProperties CrawlerImgProperties 对象
     */
    public void downloadImage(CrawlerImgProperties crawlerImgProperties) {
        CrawlerImgUtil.downloadImage(crawlerImgProperties);
    }

    /**
     * 通过图片 url 下载图片到指定路径
     *
     * @param urlStr   图片url
     * @param filePath 下载文件路径（文件不是目录）
     */
    public static void downloadImgByUrl(String urlStr, String filePath) {
        CrawlerImgUtil.downloadImgByUrl(urlStr, filePath, null);
    }

    /**
     * 使用 Jsoup 静态网页图片下载（url打开元素加载成功，不会被js渲染才出来）
     * 【静态网页直接用这个方法，速度快很多】
     * 爬虫日志文件: saveDirPath 目录下的 CrawlerDocumentUtil.CRAWLER_LOG_FILE_NAME.txt 文件
     *
     * @param targetUrl 目标网址url
     * @param saveDirPath 图片存储路径
     * @param imgUrlPrefix 图片路径前缀 /xx/yy 的路径图片
     * @param imgFormat 图片格式 eg: jpg/png
     */
    public static void downloadImgWithStaticWebPage(String targetUrl,
                                                    String saveDirPath,
                                                    String imgUrlPrefix,
                                                    String imgFormat) {

        try {
            CrawlerImgUtil.downloadImgWithStaticWebPage(targetUrl, saveDirPath, imgUrlPrefix, imgFormat, null, null,null);
        } catch (Exception e) {
            LogUtil.warn("downloadImgWithStaticWebPage execute exception message = {}", e.getLocalizedMessage(), e);
        }

    }

    public static void downloadImgWithStaticWebPage(String targetUrl,
                                                    String saveDirPath,
                                                    String imgUrlPrefix,
                                                    String imgFormat,
                                                    String domPath,
                                                    String attrName,
                                                    String reqHeadUserAgent) {
        try {
            CrawlerImgUtil.downloadImgWithStaticWebPage(targetUrl, saveDirPath, imgUrlPrefix, imgFormat, domPath, attrName, reqHeadUserAgent);
        } catch (Exception e) {
            LogUtil.warn("downloadImgWithStaticWebPage execute exception message = {}", e.getLocalizedMessage(), e);
            FileUtil.fileAppendData(saveDirPath + SystemUtil.getFileSeparator() + CrawlerImgUtil.CRAWLER_LOG_FILE_NAME + ".txt",
                    "targetUrl = " + targetUrl + StringConstantUtil.stringAppendChineseMidBracket("exception : " + e.getLocalizedMessage())
                            + ", time = " + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_SECOND) + SystemUtil.getLineSeparator());
        }
    }

    /**
     * 批量下载图片
     * 日志路径: saveDirPath 目录下的 CRAWLER_LOG_FILE_NAME 的.txt 文件
     *
     * @param targetUrlList        目标网址url 集合
     * @param saveDirPath      图片存储路径
     * @param imgUrlPrefix     图片路径前缀 /xx/yy 的路径图片
     * @param imgFormat        图片格式 eg: jpg/png
     * @param domPath          网页dom树的路径，eg: #top > div > div > div > div > img，默认为 img
     * @param attrName         属性名，默认 src
     * @param threadCount       批量线程数，范围 [1-100]
     * @param reqHeadUserAgent 请求头模拟 user-agent
     */
    public static void batchDownloadImgWithStaticWebPage(List<String> targetUrlList,
                                                    String saveDirPath,
                                                    String imgUrlPrefix,
                                                    String imgFormat,
                                                    String domPath,
                                                    String attrName,
                                                    Integer threadCount,
                                                    String reqHeadUserAgent) {
        try {
            CrawlerImgUtil.batchDownloadImgWithStaticWebPage(targetUrlList, saveDirPath, imgUrlPrefix, imgFormat, domPath, attrName, threadCount, reqHeadUserAgent);
        } catch (Exception e) {
            LogUtil.warn("batchDownloadImgWithStaticWebPage execute exception message = {}", e.getLocalizedMessage(), e);
            FileUtil.fileAppendData(saveDirPath + SystemUtil.getFileSeparator() + CrawlerImgUtil.CRAWLER_LOG_FILE_NAME + ".txt",
                    "targetUrlList = " + StringConstantUtil.listToString(targetUrlList) + StringConstantUtil.stringAppendChineseMidBracket("exception : " + e.getLocalizedMessage())
                            + ", time = " + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_SECOND) + SystemUtil.getLineSeparator());
        }
    }



    /**
     * 下载页面内容到指定路径中
     *
     * @param targetUrl         目标网址url
     * @param domPath           网页dom树的路径，eg: #top > div > div > div > div[class=className] > img
     * @param contentTypeEnum   获取网页信息格式，html / txt
     * @param contentReplaceMap 内容替换map集合, key: 替换前的字符串，支持正则表达式， value: 替换后的值
     * @param saveDirPath       存储路径目录
     * @param fileFormat        文件格式后缀, eg: txt, word 等
     * @param reqHeadUserAgent  请求头模拟 user-agent
     */
    public static void downloadContentWithStaticWebPage(String targetUrl,
                                                        String domPath,
                                                        CrawlerDocumentUtil.PageContentTypeEnum contentTypeEnum,
                                                        Map<String, String> contentReplaceMap,
                                                        String saveDirPath,
                                                        String fileFormat,
                                                        String reqHeadUserAgent) {
        try {
            CrawlerDocumentUtil.downloadContentWithStaticWebPage(targetUrl, domPath, contentTypeEnum, contentReplaceMap, saveDirPath, fileFormat, reqHeadUserAgent);
        } catch (Exception e) {
            LogUtil.warn("getElementAttrWithStaticWebPage execute exception message = {}", e.getLocalizedMessage(), e);
        }
    }

    /**
     * 批量下载页面内容到指定路径中
     * 日志文件: saveDirPath 目录下的 CrawlerDocumentUtil.CRAWLER_LOG_FILE_NAME.txt 文件
     *
     * @param targetUrlList     目标网址url 集合
     * @param domPath           网页dom树的路径，eg: #top > div > div > div > div[class=className] > img
     * @param contentTypeEnum   获取网页信息格式，html / txt
     * @param contentReplaceMap 内容替换map集合, key: 替换前的字符串，支持正则表达式， value: 替换后的值
     * @param saveDirPath       存储路径目录
     * @param fileFormat        文件格式后缀, eg: txt, word 等
     * @param threadCount       批量线程数，范围 [1-100]
     * @param reqHeadUserAgent  请求头模拟 user-agent
     */
    public static void batchDownloadContentWithStaticWebPage(List<String> targetUrlList,
                                                             String domPath,
                                                             CrawlerDocumentUtil.PageContentTypeEnum contentTypeEnum,
                                                             Map<String, String> contentReplaceMap,
                                                             String saveDirPath,
                                                             String fileFormat,
                                                             Integer threadCount,
                                                             String reqHeadUserAgent) {
        try {
            CrawlerDocumentUtil.batchDownloadContentWithStaticWebPage(targetUrlList, domPath, contentTypeEnum, contentReplaceMap, saveDirPath, fileFormat, threadCount, reqHeadUserAgent);
        } catch (Exception e) {
            LogUtil.warn("getElementAttrWithStaticWebPage execute exception message = {}", e.getLocalizedMessage(), e);
            FileUtil.fileAppendData(saveDirPath + SystemUtil.getFileSeparator() + CrawlerDocumentUtil.CRAWLER_LOG_FILE_NAME + ".txt",
                    "targetUrlList = " + StringConstantUtil.listToString(targetUrlList) + StringConstantUtil.stringAppendChineseMidBracket("exception : " + e.getLocalizedMessage())
                            + ", time = " + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_SECOND) + SystemUtil.getLineSeparator());
        }

    }

    /**
     * 获取页面元素属性值 （eg: 模拟页面访问分页遍历）
     *
     * @param targetUrl        目标网址url
     * @param domPath          网页dom树的路径，eg: #top > div > div > div > div > img
     * @param attrName         获取对应标签的具体属性，为null则返回整个标签内容
     * @param reqHeadUserAgent 请求头模拟 user-agent
     */
    public static List<String> getAttrNameWithStaticWebPage(String targetUrl,
                                                            String domPath,
                                                            String attrName,
                                                            String reqHeadUserAgent) {
        try {
            return CrawlerJsoupCommonUtil.getAttrNameWithStaticWebPage(targetUrl, domPath, attrName, reqHeadUserAgent);
        } catch (Exception e) {
            LogUtil.warn("getElementAttrWithStaticWebPage execute exception message = {}", e.getLocalizedMessage(), e);
            return Lists.newArrayList();
        }
    }





}
