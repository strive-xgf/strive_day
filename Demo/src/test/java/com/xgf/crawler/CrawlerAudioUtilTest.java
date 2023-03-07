package com.xgf.crawler;

import com.xgf.DemoApplication;
import com.xgf.crawler.inner.CrawlerAudioUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author strive_day
 * @create 2023-03-08 0:02
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class CrawlerAudioUtilTest {

    private static final String CHROME_REQ_HEAD_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36";

    @Test
    public void test() throws IOException {
        CrawlerAudioUtil.downloadAudioByAlbumWithXimalaya("49139569", "I:\\plan\\\\audio", "mp3", CHROME_REQ_HEAD_USER_AGENT);
    }
}
