package com.xgf.crawler.inner;

import com.xgf.common.LogUtil;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.file.FileUtil;
import com.xgf.system.SystemUtil;
import org.apache.commons.lang3.BooleanUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author strive_day
 * @create 2023-02-28 17:02
 * @description 爬虫通用工具类
 */
public class CrawlerCommonUtil {

    public Integer downloadByUrl(String urlStr, String filePath) throws Exception {
        return CrawlerCommonUtil.downloadByUrl(urlStr, filePath, false, null);
    }

    /**
     * @param retryCount 重试次数
     */
    public static Integer downloadByUrlWithRetry(String urlStr, String filePath, Integer retryCount, String reqHeadUserAgent) {

        retryCount = retryCount == null || retryCount <= 0 ? 3 : retryCount;

        for (int i = 1; i <= retryCount; i++) {
            try {
                return CrawlerCommonUtil.downloadByUrl(urlStr, filePath, Boolean.FALSE, reqHeadUserAgent);
            } catch (Exception e) {
                LogUtil.info("第" + i + "次获取连接失败，targetUrl = " + urlStr + SystemUtil.getLineSeparator() + "\t exception message = " + e.getLocalizedMessage());
            }
        }

        // 重试次数结束，抛出异常
        throw CustomExceptionEnum.URL_CONNECTION_EXCEPTION.generateException();
    }

    /**
     * 通过 url 下载指定网页信息到到存储路径
     *
     * @param urlStr           网页url地址
     * @param filePath         文件下载路径
     * @param existCoverFlag   文件存在是否覆盖标识
     * @param reqHeadUserAgent 模拟请求头 user-agent 参数，避免403异常
     * @return 返回目标文件字节数
     * @throws Exception 异常信息 （异常重试机制配置）
     */
    public static Integer downloadByUrl(String urlStr, String filePath, Boolean existCoverFlag, String reqHeadUserAgent) throws Exception {

        if (BooleanUtils.isNotTrue(existCoverFlag) && new File(filePath).exists()) {
            LogUtil.info("file exist , path = {}", filePath);
        }

        int downloadByteCount = 0;

        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection();
        // Server returned HTTP response code: 403 for URL（403 无权限），设置请求头 User-Agent 属性来模拟浏览器运行
        if (reqHeadUserAgent != null) {
            urlConnection.setRequestProperty("User-Agent", reqHeadUserAgent);
        }

        BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
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
