package com.xgf.crawler.inner;

import com.google.common.collect.Lists;
import com.xgf.common.LogUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.file.FileUtil;
import com.xgf.randomstr.RandomStrUtil;
import com.xgf.system.SystemUtil;
import com.xgf.task.TaskUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author strive_day
 * @create 2023-02-07 22:17
 * @description 爬虫通用工具类（基于 jsoup 封装）【静态网址）
 */

@Component
public class CrawlerJsoupCommonUtil {


    /**
     * 获取页面元素属性值 （eg: 模拟页面访问分页遍历）  eg: <a href = ""></a> 获取a标签的href属性
     *
     * @param targetUrl        目标网址url
     * @param domPath          网页dom树的路径，eg: #top > div > div > div > div > img
     * @param attrName         获取对应标签的具体属性，为null则返回整个标签内容
     * @param reqHeadUserAgent 请求头模拟 user-agent
     */
    public static List<String> getAttrNameWithStaticWebPage(String targetUrl,
                                                            String domPath,
                                                            String attrName,
                                                            String reqHeadUserAgent) throws IOException {

        // 获取所有对应 dom树 的元素信息
        List<Element> allImgElementList = getElementWithWebPage(targetUrl, domPath, reqHeadUserAgent);


        if (StringUtils.isBlank(attrName)) {
            // 返回标签元素的所有内容
            // html / txt ？
//            return Optional.of(allImgElementList).orElseGet(Lists::newArrayList)
//                    .stream().map(e -> Objects.isNull(e.parent()) ? e.html() : e.parent().html()).collect(Collectors.toList());
            return Optional.of(allImgElementList).orElseGet(Lists::newArrayList)
                    .stream().map(Element::html).collect(Collectors.toList());
        }

        // 获取对应标签元素的某个具体属性值
        return Optional.of(allImgElementList).orElseGet(Lists::newArrayList)
                .stream().map(p -> p.attr(attrName)).collect(Collectors.toList());
    }


    /**
     * 获取页面元素信息 （eg: 模拟页面访问分页遍历）
     *
     * @param targetUrl        目标网址url
     * @param domPath          网页dom树的路径，eg: #top > div > div > div > div > img
     * @param reqHeadUserAgent 请求头模拟 user-agent
     */
    public static List<Element> getElementWithWebPage(String targetUrl,
                                                      String domPath,
                                                      String reqHeadUserAgent) throws IOException {

        // 选择器 选取满足条件的多个元素
        return getDocumentWithPage(targetUrl, reqHeadUserAgent).select(domPath);
    }


    /**
     * 获取页面body 的 Document 文档
     *
     * @param targetUrl        目标网址url
     * @param reqHeadUserAgent 请求头模拟 user-agent
     */
    public static Document getDocumentWithPage(String targetUrl,
                                               String reqHeadUserAgent) throws IOException {
        // Jsoup 模拟浏览器发起请求
        Connection.Response response = getConnectWithWebPage(targetUrl, 6000, reqHeadUserAgent).execute();

        // Jsoup 解析HTML dom 树
        return Jsoup.parse(response.body());
    }



    /**
     *  Jsoup 获取网页连接
     *
     * @param targetUrl 目标地址
     * @return Connection
     */
    public static Connection getConnectWithWebPage(String targetUrl) {
        return getConnectWithWebPage(targetUrl, 6000, null);
    }

    /**
     * Jsoup 获取网页连接
     *
     * @param targetUrl        目标网址
     * @param timeout          尝试连接超时时间，单位毫秒
     * @return Connection
     */
    public static Connection getConnectWithWebPage(String targetUrl, Integer timeout) {
        return getConnectWithWebPage(targetUrl, timeout, null);
    }

    /**
     * 增加重试次数
     * @param retryCount 重试次数
     */
    public static Connection.Response getResponseWithWebPageRetry(String targetUrl,
                                                        Integer retryCount,
                                                        Integer timeout,
                                                        String reqHeadUserAgent) {
        for (int i = 1; i <= retryCount; i++) {
            try {
                return getConnectWithWebPage(targetUrl, timeout, reqHeadUserAgent).execute();
            } catch (Exception e) {
                LogUtil.warn("第{}次获取连接失败，targetUrl = {}， exception message = {}", i, targetUrl, e);
            }
        }
        // 重试次数结束，抛出异常
        throw CustomExceptionEnum.URL_CONNECTION_EXCEPTION.generateException();
    }

    /**
     * Jsoup 获取网页连接
     *
     * @param targetUrl        目标网址
     * @param timeout          尝试连接超时时间，单位毫秒
     * @param reqHeadUserAgent 请求头 user-agent 参数
     * @return Connection
     */
    public static Connection getConnectWithWebPage(String targetUrl,
                                                      Integer timeout,
                                                      String reqHeadUserAgent) {
        return Jsoup.connect(targetUrl)
                .header("User-Agent", reqHeadUserAgent)
                .ignoreContentType(true)
                .timeout(timeout);
    }




    /**
     * 批量下载网页信息
     *
     * @param urlList          url集合
     * @param saveDir 文件存储目录  【最终存储路径：saveDir > 网页title_随机8位uuid字符串.format】
     * @param format 文件格式 eg: .txt、.jpg等
     * @param threadCount 线程数（批量下载数，暂时最大支持100，默认1）
     * @param existCoverFlag 目录存在是否覆盖，true：覆盖，false：不覆盖（不执行）
     * @param reqHeadUserAgent 请求头参数
     * @return 下载文件大小，单位: byte
     * @throws Exception 异常
     */
    public static void batchDownloadByUrlWithJsoup(List<String> urlList, String saveDir, String format, Integer threadCount, Boolean existCoverFlag, String reqHeadUserAgent) {

        // 线程数控制
        threadCount = ObjectUtils.defaultIfNull(threadCount, 1);
        threadCount = threadCount > 100 ? 100 : threadCount;

        Lists.partition(urlList, threadCount).forEach(subUrlList -> {
            // 异步集合
            List<CompletableFuture<?>> futureList = new ArrayList<>(subUrlList.size());
            // 异步执行
            subUrlList.forEach(url -> futureList.add(TaskUtil.runAsync(() ->
                    CrawlerJsoupCommonUtil.downloadByUrlWithJsoupCatch(url, saveDir, format, existCoverFlag, reqHeadUserAgent))));
            // 等待全部狭隘完成
            TaskUtil.waitAnyException(futureList);


        });

    }


    /**
     * 捕获异常
     */
    public static Integer downloadByUrlWithJsoupCatch(String urlStr, String saveDir, String format, Boolean existCoverFlag, String reqHeadUserAgent) {
        try {
            return downloadByUrlWithJsoup(urlStr, saveDir, format, existCoverFlag, reqHeadUserAgent);
        } catch (Exception e) {
            LogUtil.warn("downloadByUrlWithJsoupCatch exception message = {}",  e);
            return 0;
        }
    }

    /**
     * 下载指定 url 的内容
     *
     * @param urlStr url链接
     * @param saveDir 文件存储目录  【最终存储路径：saveDir > 网页title_随机8位uuid字符串.format】
     * @param format 文件格式 eg: .txt、.jpg等
     * @param existCoverFlag 目录存在是否覆盖，true：覆盖，false：不覆盖（不执行）
     * @param reqHeadUserAgent 请求头参数
     * @return 下载文件大小，单位: byte
     * @throws Exception 异常
     */
    public static Integer downloadByUrlWithJsoup(String urlStr, String saveDir, String format, Boolean existCoverFlag, String reqHeadUserAgent) throws Exception {

        System.out.println("====== downloadByUrlWithJsoup >>> " + urlStr);
        if (BooleanUtils.isNotTrue(existCoverFlag) && new File(saveDir).exists()) {
            LogUtil.info("file dir exist, path = {}", saveDir);
        }

        int downloadByteCount = 0;

        Connection.Response response = CrawlerJsoupCommonUtil.getResponseWithWebPageRetry(urlStr, 1, 6000, reqHeadUserAgent);

        // 响应流
        BufferedInputStream responseInputStream = response.bodyStream();
//        Document document = response.parse();

        // todo 标题有问题啊
        // 解析响应流，获取Document对象，然后用来获取标题（方法将输入流解析为 Document 对象，其中第二个参数为字符编码，如果不知道编码可以传入 null，url 参数为原始 URL）
        // 这个过程会将responseInputStream流数据读取遍历一遍，如果直接继续使用responseInputStream写入，会是空的！！！
//        Document document = Jsoup.parse(responseInputStream, null, urlStr);

        // 组装文件路径 saveDir > 网页title_随机8位uuid字符串.format（如果format为空，默认存储位 .txt 文件）
        String filePath = StringConstantUtil.defaultEndWith(saveDir, SystemUtil.getFileSeparator())
//                + document.title() + StringConstantUtil.UNDERLINE
//                + RandomStrUtil.createRandomStr(8)
                + RandomStrUtil.createRandomStr(16)
                + StringConstantUtil.defaultStartWith(StringConstantUtil.isBlank(format) ? ".txt" : format, StringConstantUtil.DOT);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(responseInputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(FileUtil.createFileAndDir(filePath)));

        byte[] bytes = new byte[1024];
        int len;
        while ((len = bufferedInputStream.read(bytes)) != -1) {
            bufferedOutputStream.write(bytes, 0, len);
            downloadByteCount += len;
        }

        bufferedInputStream.close();
        bufferedOutputStream.close();

        return downloadByteCount;
    }







}
