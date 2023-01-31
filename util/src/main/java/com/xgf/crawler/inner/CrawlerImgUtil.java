package com.xgf.crawler.inner;

import com.google.common.collect.Lists;
import com.xgf.common.JsonUtil;
import com.xgf.common.LogUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.crawler.config.CrawlerBaseProperties;
import com.xgf.crawler.config.CrawlerImgProperties;
import com.xgf.date.DateUtil;
import com.xgf.file.FileUtil;
import com.xgf.java8.BooleanFunctionUtil;
import com.xgf.system.SystemUtil;
import com.xgf.task.TaskUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author strive_day
 * @create 2023-01-16 19:38
 * @description 爬虫（图片）工具类 此方法需要注入访问
 */

@Component
public class CrawlerImgUtil {

    @Autowired
    protected CrawlerImgProperties crawlerImgProperties;

    /**
     * 图片爬取日志文件名
     */
    public static final String CRAWLER_LOG_FILE_NAME = "crawlerStaticImgLog";

    /**
     * 是否需要添加爬虫日志
     */
    private static Boolean CRAWLER_LOG_FLAG;

    /**
     * 给静态变量 FILE_RENAME_LOG_FLAG 读取赋值，默认为 false
     */
    @Value("${custom.config.log.crawler.crawlerLogFlag:false}")
    public void setStaticFileReNameFlag(Boolean crawlerLogFlag) {
        CrawlerImgUtil.CRAWLER_LOG_FLAG = crawlerLogFlag;
    }


    /**
     * 通过读取配置属性，下载网页图片
     * todo: 异步、消息 提速方式？
     */
    public void downloadImageOpenWebPage() {
        try {
            downloadImage(crawlerImgProperties);
        } catch (Exception e) {
            LogUtil.warn("downloadImageByConfig exception = {}", e.getLocalizedMessage(), e);
            throw e;
        }
    }


    /**
     * 通过属性，下载网页图片（会一个个打开网页），做通配符解析
     *
     * @param crawlerImgProperties CrawlerImgProperties 对象
     *                             todo: 并行、异步、消息 提速方式？  并行的问题，没法确认数据量有多少（url数）
     */
    public static void downloadImage(CrawlerImgProperties crawlerImgProperties) {

        long startTime = System.currentTimeMillis();
        BooleanFunctionUtil.trueRunnable(CRAWLER_LOG_FLAG).run(() ->
                LogUtil.info("downloadImageByConfig start, crawlerImgProperties = {}", JsonUtil.toJsonString(crawlerImgProperties)));

        // 校验和填充默认参数
        crawlerImgProperties.checkAndDefaultFill();

        // 日志文件路径
        String logFilePath = crawlerImgProperties.getCrawlerBaseProperties().getDownloadPath() + SystemUtil.getFileSeparator() + "crawlerImgLog.txt";
        // 记录到文档中
        FileUtil.fileAppendData(logFilePath, "========== >>>>>>" + SystemUtil.getLineSeparator() + "param = " + JsonUtil.toJsonString(crawlerImgProperties)
                + ", start time = " + DateUtil.dateFormatString(new Date(startTime), DateUtil.FORMAT_SECOND) + SystemUtil.getLineSeparator());

        // 基本属性
        CrawlerBaseProperties baseProperties = crawlerImgProperties.getCrawlerBaseProperties();
        // 获取驱动 webDriver
        WebDriver webDriver = CrawlerSeleniumCommonUtil.getWebDriver(baseProperties.getBrowserType(), baseProperties.getWebDriverPath());

        for (String webPageUrl : baseProperties.getWebPageUrlList()) {
            // 解析网址，通配符适用
            CrawlerBaseProperties.WebPageUrlParseProperties parseProperties = CrawlerBaseProperties.WebPageUrlParseProperties.parseWebPageUrl(webPageUrl);

            // 非通配符匹配多个网址
            if (parseProperties.getDataRange() == null) {
                CrawlerImgUtil.downloadImageOpenWebPage(webDriver, webPageUrl, baseProperties.getDownloadPath(),
                        crawlerImgProperties.getUrlPreAppend(), crawlerImgProperties.getFormat(),
                        Arrays.asList(crawlerImgProperties.getExcludeUrls()), baseProperties.getReqHeadParamUserAgent());
                continue;
            }

            // 通配符替换url下载
            // todo 并行 n 个一批并行，全部完成进入下一步
            for (int i = parseProperties.getDataRange().getLower(); i <= parseProperties.getDataRange().getUpper(); i++) {
                CrawlerImgUtil.downloadImageOpenWebPage(webDriver, webPageUrl.replace(parseProperties.getWildcardStr(), String.valueOf(i)),
                        baseProperties.getDownloadPath(), crawlerImgProperties.getUrlPreAppend(), crawlerImgProperties.getFormat(),
                        Arrays.asList(crawlerImgProperties.getExcludeUrls()), baseProperties.getReqHeadParamUserAgent());
                // sessionId 问题，需要打开多个浏览器，不能共用一个webdriver对象
//                TaskUtil.runAsync(() -> CrawlerImgUtil.downloadImageByConfig(webDriver, webPageUrl.replace(parseProperties.getWildcardStr(), String.valueOf(i)),
//                        baseProperties.getDownloadPath(), crawlerImgProperties.getUrlPreAppend(), crawlerImgProperties.getFormat(), Arrays.asList(crawlerImgProperties.getExcludeUrls()), baseProperties.getReqHeadParamUserAgent()))
            }
        }
        // 关闭浏览器所有打开的窗口
        webDriver.quit();

        // 记录到文档中
        FileUtil.fileAppendData(logFilePath, "execute end, time = " + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_SECOND)
                + ", cost = " + (System.currentTimeMillis() - startTime) + "ms" + SystemUtil.getLineSeparator()
                + "========== <<<<<<" + SystemUtil.getLineSeparator() + SystemUtil.getLineSeparator());
    }



    /**
     * 打开网页，加载完成后，下载网页图片
     *
     * @param webDriver        浏览器驱动
     * @param targetUrl        目标网址url
     * @param saveDirPath      图片存储路径
     * @param imgUrlPrefix     图片路径前缀 /xx/yy 的路径图片
     * @param imgFormat        图片格式  eg: jpg/png
     * @param excludeUrlList   排除图片内容 eg: logo
     * @param reqHeadUserAgent 请求头模拟 user-agent
     */
//    @GuavaRetryAnnotation(attemptNumber = 3, waitStrategySleepTime = 100)
    public static void downloadImageOpenWebPage(WebDriver webDriver,
                                                String targetUrl,
                                                String saveDirPath,
                                                String imgUrlPrefix,
                                                String imgFormat,
                                                List<String> excludeUrlList,
                                                String reqHeadUserAgent) {
        try {

            System.out.println(">>>>>> targetUrl = " + targetUrl);
            // 设置窗口最大化
//                webDriver.manage().window().maximize();

            // 设置网页最大等待时间
            webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            // 打开指定网页
            webDriver.get(targetUrl);

//            // 设置等待时间
//            Thread.sleep(1000);

            // 路径 + 页面标题 作为目录
            String webTitle = webDriver.getTitle();
            // 存储目录
            String targetDirPath = saveDirPath + SystemUtil.getFileSeparator() + webTitle;

            // 日志文件路径
            String logFilePath = saveDirPath + SystemUtil.getFileSeparator() + "crawlerImgLog.txt";
            // todo 加配置？
            // 当前目录存在，则不继续执行（否则继续执行，然后把文件重写一份，浪费时间），eg: 重定向跳到首页，导致一直下载首页图片，同时避免重复图片下载
            if (new File(targetDirPath).exists()) {
                System.out.println(">>>>>> exist return");
                FileUtil.fileAppendData(logFilePath, "targetUrl = " + targetUrl + ", actualUrl = " + webDriver.getCurrentUrl()
                        + StringConstantUtil.stringAppendChineseMidBracket(webTitle) + "<<<已存在，不继续遍历 >>>" + SystemUtil.getLineSeparator());
                return;
            }

            // todo By.xpath() 修改添加配置，选择标签下载图片，否则下载全部 img 标签
            List<String> imgUrlList = webDriver.findElements(By.tagName("img"))
                    .stream()
                    .filter(Objects::nonNull)
                    .map((p -> p.getAttribute("src")))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(imgUrlList)) {
                return;
            }

            // 添加路径前缀
            if (StringUtils.isNotBlank(imgUrlPrefix)) {
                imgUrlList = imgUrlList.stream().map(p -> (imgUrlPrefix + p)).collect(Collectors.toList());
            }

            // 排除指定url集合
            if (CollectionUtils.isNotEmpty(excludeUrlList)) {
                imgUrlList = imgUrlList.stream().filter(p -> !excludeUrlList.contains(p)).collect(Collectors.toList());
            }


            List<String> finalImgUrlList = imgUrlList;
            BooleanFunctionUtil.trueRunnable(CRAWLER_LOG_FLAG).run(() ->
                    LogUtil.info("url = {}, crawler imgUrlList = {}", webDriver.getCurrentUrl(), JsonUtil.toJsonString(finalImgUrlList)));

            AtomicInteger count = new AtomicInteger();
            // 分组 并行
            Lists.partition(imgUrlList, 50).forEach(subList -> {
                List<CompletableFuture<?>> futureList = Lists.newArrayList();
                for (String urlStr : subList) {
                    // 文件名 todo 文件名读取加载文件配置
                    String fileName = webTitle + StringConstantUtil.UNDERLINE + (StringConstantUtil.preFillZero(count.incrementAndGet(), 4)) + StringConstantUtil.DOT + imgFormat;
                    futureList.add(TaskUtil.runAsync(() -> CrawlerImgUtil.downloadImgByUrl(urlStr, targetDirPath + SystemUtil.getFileSeparator() + fileName, reqHeadUserAgent)));
                }
                TaskUtil.waitAnyException(futureList);
            });


            FileUtil.fileAppendData(logFilePath, "targetUrl = " + targetUrl + ", actualUrl = " + webDriver.getCurrentUrl()
                    + StringConstantUtil.stringAppendChineseMidBracket(webTitle) + "，数量: " + imgUrlList.size() + SystemUtil.getLineSeparator());

//            // 关闭当前浏览器窗口/所有窗口
//            webDriver.close();
//            webDriver.quit();
        } catch (Exception e) {
            LogUtil.warn("execute exception message = {}", e.getLocalizedMessage(), e);
        }
    }


    /**
     * 通过图片 url 下载图片到指定路径
     *
     * @param urlStr           图片url
     * @param filePath         下载文件路径
     * @param reqHeadUserAgent 请求头模拟 user-agent
     * @return 1: 成功, 0: 失败
     */
    public static int downloadImgByUrl(String urlStr, String filePath, String reqHeadUserAgent) {
        try {
//            System.out.println(">>>>>> downloadImgByUrl download " + urlStr);
            FileUtil.downloadImgByUrl(urlStr, filePath, reqHeadUserAgent);
            return 1;
        } catch (Exception e) {
            LogUtil.warn("downloadImgByUrl execute exception message = {}", e.getLocalizedMessage(), e);
            return 0;
        }
    }



    /**
     * 获取页面截图
     *
     * @param webPageTypeEnum 浏览器类型
     * @param driverPath      浏览器驱动路径阿
     * @param targetUrl       目标网址url
     * @param saveDirPath     保存路径目录
     */
    public static void cutWebPageByUrl(CrawlerSeleniumCommonUtil.WebPageTypeEnum webPageTypeEnum,
                                       String driverPath,
                                       String targetUrl,
                                       String saveDirPath) {
        cutWebPageByUrl(CrawlerSeleniumCommonUtil.getWebDriver(webPageTypeEnum, driverPath), targetUrl, saveDirPath);
    }

    /**
     * 读取配置，截取网页作为图片存储
     */

    /**
     * 获取页面截图
     * todo: 滚动截图实现
     *
     * @param webDriver   WebDriver 对象
     * @param targetUrl   目标网址url
     * @param saveDirPath 保存路径目录
     */
    public static void cutWebPageByUrl(WebDriver webDriver,
                                       String targetUrl,
                                       String saveDirPath) {


        // 设置 窗口最大化
        webDriver.manage().window().maximize();
        // 设置 网页等待时间
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        // 打开网页
        webDriver.get(targetUrl);
//        Thread.sleep(3000);

        // 当前页面截图（原图片）
        File sourceFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);

        // 目标路径
        String title = StringUtils.isNotBlank(webDriver.getTitle()) ? webDriver.getTitle() : webDriver.getCurrentUrl();
        File targetFile = new File(saveDirPath + SystemUtil.getFileSeparator() + title + ".jpg");

        try {
            Files.copy(sourceFile.toPath(), targetFile.toPath());
        } catch (IOException e) {
            LogUtil.warn("cutWebPageByUrl exception message = {}", e.getLocalizedMessage(), e);
        }

        // 关闭 webDriver
        webDriver.close();
    }


    // >>>>>>>>>>>>>>>>>>>> 以下是 jsoup 静态网页下载 <<<<<<<<<<<<<<<<<<<<

    /**
     * 批量下载图片（静态网页）
     * 日志路径: saveDirPath 目录下的 CRAWLER_LOG_FILE_NAME 的.txt 文件
     *
     * @param targetUrlList    目标网址url 集合
     * @param saveDirPath      图片存储路径
     * @param imgUrlPrefix     图片路径前缀 /xx/yy 的路径图片
     * @param imgFormat        图片格式 eg: jpg/png
     * @param domPath          网页dom树的路径，eg: #top > div > div > div > div > img，默认为 img
     * @param attrName         属性名，默认 src
     * @param threadCount      批量线程数，范围 [1-100]
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
                    downloadImgWithStaticWebPageCatch(url, saveDirPath, imgUrlPrefix, imgFormat, domPath, attrName, reqHeadUserAgent))));

            // 等待执行完全
            TaskUtil.waitAnyException(futureList);
        });

    }


    /**
     * 捕获异常并记录日志
     */
    public static void downloadImgWithStaticWebPageCatch(String targetUrl,
                                                         String saveDirPath,
                                                         String imgUrlPrefix,
                                                         String imgFormat,
                                                         String domPath,
                                                         String attrName,
                                                         String reqHeadUserAgent) {

        try {
            downloadImgWithStaticWebPage(targetUrl, saveDirPath, imgUrlPrefix, imgFormat, domPath, attrName, reqHeadUserAgent);
        } catch (Exception e) {
            LogUtil.warn("downloadImgWithStaticWebPageCatch execute exception message = {}", e.getLocalizedMessage(), e);
            // 记录异常信息
            FileUtil.fileAppendData(saveDirPath + SystemUtil.getFileSeparator() + CRAWLER_LOG_FILE_NAME + ".txt",
                    "targetUrl = " + targetUrl + StringConstantUtil.stringAppendChineseMidBracket("exception : " + e.getLocalizedMessage())
                            + ", time = " + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_SECOND) + SystemUtil.getLineSeparator());
        }

    }


    /**
     * 默认值赋值调用
     */
    public static void downloadImgWithStaticWebPage(String targetUrl,
                                                    String saveDirPath,
                                                    String imgUrlPrefix,
                                                    String imgFormat,
                                                    String domPath,
                                                    String attrName,
                                                    String reqHeadUserAgent) throws Exception {

        imgFormat = StringUtils.defaultIfBlank(imgFormat, "jpg");
        domPath = StringUtils.defaultIfBlank(domPath, "img");
        attrName = StringUtils.defaultIfBlank(attrName, "src");

        downloadImgWithStaticWebPageInner(targetUrl, saveDirPath, imgUrlPrefix, imgFormat, domPath, attrName, reqHeadUserAgent);

    }

    /**
     * 使用 Jsoup 静态网页图片下载（url打开元素加载成功，不会被js渲染才出来）
     * 日志路径: saveDirPath 目录下的 CRAWLER_LOG_FILE_NAME 的.txt 文件
     *
     * @param targetUrl        目标网址url
     * @param saveDirPath      图片存储路径
     * @param imgUrlPrefix     图片路径前缀 /xx/yy 的路径图片
     * @param imgFormat        图片格式 eg: jpg/png
     * @param domPath          网页dom树的路径，eg: #top > div > div > div > div > img，默认为 img
     * @param attrName         属性名，默认 src
     * @param reqHeadUserAgent 请求头模拟 user-agent
     */
    private static void downloadImgWithStaticWebPageInner(String targetUrl,
                                                          String saveDirPath,
                                                          String imgUrlPrefix,
                                                          String imgFormat,
                                                          String domPath,
                                                          String attrName,
                                                          String reqHeadUserAgent) throws Exception {

        System.out.println(">>>>>> targetUrl = " + targetUrl);

        // Jsoup 模拟浏览器发起请求
        Connection.Response response = CrawlerJsoupCommonUtil.getConnectWithWebPage(targetUrl, 20000, reqHeadUserAgent).execute();

        //Jsoup 解析HTML dom树
        Document document = Jsoup.parse(response.body());
        // 标题作为目录，替换掉特殊字符
        String title = FileUtil.replaceAllSpecialFileChar(document.title());

        String targetDirPath = saveDirPath + SystemUtil.getFileSeparator() + title;

        // 当前目录存在，则不继续执行（否则继续执行，然后把文件重写一份，浪费时间），eg: 重定向跳到首页，导致一直下载首页图片，同时避免重复图片下载 todo 加配置？
        if (new File(targetDirPath).exists()) {
            System.out.println(">>>>>> exist return : " + targetDirPath);
            return;
        }


        // 选择器 选取满足条件的多个元素
        Elements allImgElements = document.select(domPath);

        if (allImgElements.size() > 0) {
            new File(targetDirPath).mkdirs();
        }

        // 图片名字下标递增
        AtomicInteger count = new AtomicInteger();

        // 分组 异步并行
        Lists.partition(allImgElements, 30).forEach(subImgElements -> {
            List<CompletableFuture<?>> futureList = Lists.newArrayList();
            for (Element imgElement : subImgElements) {
                // 根据属性查找图片src
                String imgUrl = imgElement.attr(attrName);
                if (StringUtils.isBlank(imgUrl)) {
                    continue;
                }

                // 追加前缀
                if (StringUtils.isNotBlank(imgUrlPrefix)) {
                    imgUrl = imgUrlPrefix + imgUrl;
                }

                String finalImgUrl = imgUrl;
                futureList.add(TaskUtil.runAsync(() ->
                        CrawlerImgUtil.downloadImgByUrl(finalImgUrl,
                                targetDirPath + SystemUtil.getFileSeparator() + title + StringConstantUtil.UNDERLINE + (StringConstantUtil.preFillZero(count.incrementAndGet(), 4)) + StringConstantUtil.DOT + imgFormat,
                                reqHeadUserAgent)));
            }
            TaskUtil.waitAnyException(futureList);
        });

        FileUtil.fileAppendData(saveDirPath + SystemUtil.getFileSeparator() + CRAWLER_LOG_FILE_NAME + ".txt",
                "targetUrl = " + targetUrl + StringConstantUtil.stringAppendChineseMidBracket(title) + "，数量: " + allImgElements.size()
                        + ", time = " + DateUtil.dateFormatString(new Date(), DateUtil.FORMAT_SECOND) + SystemUtil.getLineSeparator());

    }


}
