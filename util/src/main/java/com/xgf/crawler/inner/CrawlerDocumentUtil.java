package com.xgf.crawler.inner;

import com.google.common.collect.Lists;
import com.xgf.common.LogUtil;
import com.xgf.constant.EnumBase;
import com.xgf.constant.StringConstantUtil;
import com.xgf.date.DateUtil;
import com.xgf.file.FileUtil;
import com.xgf.system.SystemUtil;
import com.xgf.task.TaskUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author strive_day
 * @create 2023-02-07 22:38
 * @description 爬取文档工具类
 */
@Component
public class CrawlerDocumentUtil {

    /**
     * 图片爬取日志文件名 todo 读配置？
     */
    public static final String CRAWLER_LOG_FILE_NAME = "crawlerStaticDownloadLog";

    /**
     * 批量下载页面内容到指定路径中
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
                                                             PageContentTypeEnum contentTypeEnum,
                                                             Map<String, String> contentReplaceMap,
                                                             String saveDirPath,
                                                             String fileFormat,
                                                             Integer threadCount,
                                                             String reqHeadUserAgent) {
        if (CollectionUtils.isEmpty(targetUrlList)) {
            LogUtil.info("param targetUrlList is empty");
            return;
        }
        // 线程数控制
        threadCount = ObjectUtils.defaultIfNull(threadCount, 1);
        threadCount = threadCount > 100 ? 100 : threadCount;

        // 先创建目录，防止多线程目录创建报错
        FileUtil.createFileDir(saveDirPath);

        // 按线程数分组
        Lists.partition(targetUrlList, threadCount).forEach(subList -> {
            // CompletableFuture集合
            List<CompletableFuture<?>> futureList = Lists.newArrayList();
            subList.forEach(url -> futureList.add(TaskUtil.runAsync(() ->
                    downloadContentWithStaticWebPageCatch(url, domPath, contentTypeEnum, contentReplaceMap, saveDirPath, fileFormat, reqHeadUserAgent))));

            // 等待执行完全
            TaskUtil.waitAnyException(futureList);
        });

    }

    /**
     * 捕获异常并记录日志
     */
    public static void downloadContentWithStaticWebPageCatch(String targetUrl,
                                                             String domPath,
                                                             PageContentTypeEnum contentTypeEnum,
                                                             Map<String, String> contentReplaceMap,
                                                             String saveDirPath,
                                                             String fileFormat,
                                                             String reqHeadUserAgent) {
        try {
            CrawlerDocumentUtil.downloadContentWithStaticWebPage(targetUrl, domPath, contentTypeEnum, contentReplaceMap, saveDirPath, fileFormat, reqHeadUserAgent);
        } catch (Exception e) {
            LogUtil.warn("downloadContentWithStaticWebPageCatch execute exception message = {}", e.getLocalizedMessage(), e);
            // 记录异常信息
            FileUtil.fileAppendData(saveDirPath + SystemUtil.getFileSeparator() + CRAWLER_LOG_FILE_NAME + ".txt",
                    "targetUrl = " + targetUrl + StringConstantUtil.stringAppendChineseMidBracket("exception : " + e.getLocalizedMessage())
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
                                                        PageContentTypeEnum contentTypeEnum,
                                                        Map<String, String> contentReplaceMap,
                                                        String saveDirPath,
                                                        String fileFormat,
                                                        String reqHeadUserAgent) throws IOException {

        // 获取内容
        Document document = CrawlerJsoupCommonUtil.getDocumentWithPage(targetUrl, reqHeadUserAgent);

        // 默认值操作
        fileFormat = StringUtils.defaultIfBlank(fileFormat, "txt");
        contentTypeEnum = ObjectUtils.defaultIfNull(contentTypeEnum, PageContentTypeEnum.HTML);

        // 标题作为文件名，替换掉特殊字符
        String title = FileUtil.replaceAllSpecialFileChar(document.title());
        // 文件路径
        String targetFilePath = saveDirPath + SystemUtil.getFileSeparator() + title + StringConstantUtil.defaultStartWith(fileFormat, StringConstantUtil.DOT);
        if (new File(targetFilePath).exists()) {
            System.out.println(">>>>>> exist return : " + targetFilePath);
            return;
        }

        // 选则满足 dom树 条件的元素
        Elements allImgElements = document.select(domPath);

        // 获取所有内容文本，替换指定内容，然后返回  eg: <a href=""> this is content </a> 获取内容值: this is content
        // html ? text ?
        PageContentTypeEnum finalContentTypeEnum = contentTypeEnum;
        List<String> contentList = allImgElements.stream().filter(Objects::nonNull)
                .map(p -> finalContentTypeEnum == PageContentTypeEnum.HTML ? p.html() : finalContentTypeEnum == PageContentTypeEnum.TEXT ? p.text() : p.text())
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        // 指定内容Map替换
        List<String> resultContentList = StringConstantUtil.replaceStrByMap(contentList, contentReplaceMap);
        // 存储
        FileUtil.fileAppendData(targetFilePath, StringConstantUtil.listToString2(resultContentList, SystemUtil.getLineSeparator()));

        // 日志文件
        FileUtil.fileAppendData(saveDirPath + SystemUtil.getFileSeparator() + CRAWLER_LOG_FILE_NAME + ".txt",
                "targetUrl = " + targetUrl + StringConstantUtil.stringAppendChineseMidBracket(title)
                        + ", time = " + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_SECOND) + SystemUtil.getLineSeparator());

    }


    /**
     * 页面文档内容格式，html / text
     */
    @Getter
    @AllArgsConstructor
    public enum PageContentTypeEnum implements EnumBase {

        HTML("html", "获取页面html内容"),
        TEXT("text", "获取页面text文本内容"),
        ;

        private final String code;

        private final String value;
    }


}
