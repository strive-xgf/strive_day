package com.xgf.crawler.inner;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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






}
