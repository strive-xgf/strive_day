package com.xgf.crawler.inner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.xgf.common.JsonUtil;
import com.xgf.common.LogUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.file.FileUtil;
import com.xgf.task.TaskUtil;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author strive_day
 * @create 2023-03-07 17:57
 * @description 爬取音频工具类
 */
public class CrawlerAudioUtil {

    /**
     * 下载 喜马拉雅 音频，通过专辑id下载
     *
     * @param albumId          专辑 id
     * @param fileDirPath      文件存储目录
     * @param fileFormat       文件格式，eg: .mp3
     * @param reqHeadUserAgent 请求头模拟 user-agent
     */
    public static void downloadAudioByAlbumWithXimalaya(String albumId, String fileDirPath, String fileFormat, String reqHeadUserAgent) throws IOException {

        fileFormat = StringConstantUtil.defaultStartWith(StringConstantUtil.defaultIfBlank(fileFormat, ".mp3"), StringConstantUtil.DOT);
        // 组装获取专辑信息接口链接，eg: https://www.ximalaya.com/revision/album/v1/getTracksList?albumId=49139569&pageNum=1&pageSize=30
        // "https://www.ximalaya.com/revision/album?albumId=" + albumId;
        String targetUrl = "https://www.ximalaya.com/revision/album/v1/getTracksList?albumId=" + albumId;

        Document document = CrawlerJsoupCommonUtil.getDocumentWithPage(targetUrl, reqHeadUserAgent);

        // 解析 json 对象
        JSONObject bodyJson = JsonUtil.parseObject(document.text());
        // data 对象
        JSONObject dataJson = bodyJson.getJSONObject("data");
        // 每页数据量大小
        Integer pageSize = dataJson.getInteger("pageSize");
        // 当前专辑总文件数
        Integer trackTotalCount = dataJson.getInteger("trackTotalCount");
        // 总页数
        int totalPageNum = trackTotalCount / pageSize + 1;

        LogUtil.info(">>>>>> downloadAudioByAlbumWithXimalaya execute targetUrl = {}, totalCount = {}, pageSize = {}, totalPageNum = {}",
                targetUrl, trackTotalCount, pageSize, totalPageNum);

        // 下载进度统计
        AtomicInteger count = new AtomicInteger();

        // 分页下载
        for (int pageNum = 1; pageNum < totalPageNum; pageNum++) {
            // 组装获取专辑分页信息接口链接
            String trackUrl = "https://www.ximalaya.com/revision/album/v1/getTracksList?albumId=" + albumId + "&pageNum=" + pageNum + "&pageSize=" + pageSize;
            Document trackDocument = CrawlerJsoupCommonUtil.getDocumentWithPage(trackUrl, reqHeadUserAgent);
            // 解析属性值，获取 data 下的 tracks 属性（章节信息）
            JSONArray tracksArray = JsonUtil.parseObject(trackDocument.text()).getJSONObject("data").getJSONArray("tracks");
            // 异步future集合
            List<CompletableFuture<?>> futureList = Lists.newArrayList();
            // 循环遍历下载
            for (int i = 0; i < tracksArray.size(); i++) {
                // 异步执行
                int finalI = i;
                String finalFileFormat = fileFormat;
                CompletableFuture<Void> future = TaskUtil.runAsync(() -> {
                    JSONObject currentJson = tracksArray.getJSONObject(finalI);
                    // 总标题
                    String albumTitle = currentJson.getString("albumTitle");
                    // 章节标题
                    String trackName = currentJson.getString("title");
                    // 章节id
                    String trackId = currentJson.getString("trackId");

                    // 获取章节的音频信息链接
                    Document audioDocument = null;
                    try {
                        audioDocument = CrawlerJsoupCommonUtil.getDocumentWithPage(
                                "https://www.ximalaya.com/revision/play/v1/audio" + "?id=" + trackId + "&ptype=1", reqHeadUserAgent);
                    } catch (IOException e) {
                        LogUtil.warn("====== downloadAudioByAlbumWithXimalaya future execute exception message = {}", e.getLocalizedMessage(), e);
                        return;
                    }
                    // 获取音频的src地址eg: https://aod.cos.tx.xmcdn.com/storages/28b9-audiofreehighqps/E2/16/CKwRIUEEYfqOAK4kcgClPmf7.m4a
                    String audioUrl = JsonUtil.parseObject(audioDocument.text()).getJSONObject("data").getString("src");

                    // 存储文件路径
                    String filePath = FileUtil.assembleFilePath(fileDirPath, albumTitle, trackName) + finalFileFormat;
                    // 下载
                    CrawlerCommonUtil.downloadByUrlWithRetry(audioUrl, filePath, 5, reqHeadUserAgent);
                    LogUtil.info(">>>>>> 当前下载信息: {}【{}】 成功，剩余: {} 个未下载",
                            trackName, audioUrl, (trackTotalCount - count.incrementAndGet()));
                });
                futureList.add(future);
            }

            // 异步等待执行
            try {
                TaskUtil.waitAnyException(futureList);
            } catch (Exception e) {
                LogUtil.warn("====== downloadAudioByAlbumWithXimalaya execute exception message = {}", e.getLocalizedMessage(), e);
            }

        }

    }


}
