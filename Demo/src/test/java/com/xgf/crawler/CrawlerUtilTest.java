package com.xgf.crawler;

import com.xgf.DemoApplication;
import com.xgf.common.JsonUtil;
import com.xgf.crawler.config.CrawlerBaseProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author strive_day
 * @create 2023-01-25 21:38
 * @description 爬取图片工具类测试
 */


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class CrawlerUtilTest {

    @Autowired
    private CrawlerUtilClient crawlerUtilClient;


    @Test
    public void testConvertUrl() {
        CrawlerBaseProperties.WebPageUrlParseProperties webPageUrl =
                CrawlerBaseProperties.WebPageUrlParseProperties.parseWebPageUrl("https://striveday.blog.csdn.net/article/details/128212025/[1 23 - 68]/a");
        System.out.println(JsonUtil.toJsonString(webPageUrl));

        for (int i = webPageUrl.getDataRange().getLower(); i < webPageUrl.getDataRange().getUpper(); i ++) {
            System.out.println(webPageUrl.getSourceUrl().replace(webPageUrl.getWildcardStr(), String.valueOf(i)));
        }
    }


    @Test
    public void testDownloadImageByConfig() {
        crawlerUtilClient.downloadImageByConfig();
    }


}
