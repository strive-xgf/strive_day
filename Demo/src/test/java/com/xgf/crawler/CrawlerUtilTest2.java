package com.xgf.crawler;

import com.xgf.crawler.inner.CrawlerJsoupCommonUtil;
import com.xgf.crawler.inner.CrawlerDocumentUtil;
import com.xgf.crawler.inner.CrawlerImgUtil;
import com.xgf.crawler.inner.CrawlerSeleniumCommonUtil;
import com.xgf.system.SystemUtil;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author strive_day
 * @create 2023-01-29 0:14
 * @description 不需要启动Spring
 */
public class CrawlerUtilTest2 {

    /**
     * chrome 浏览器，请求头 user_agent 参数
     */
    private static final String CHROME_REQ_HEAD_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36";

    /**
     * 测试用 selenium 打开谷歌浏览器，进入百度，搜索关键词，然后打出指定元素图片的 src
     */
    @Test
    public void openWebPage() {
        WebDriver webDriver = CrawlerSeleniumCommonUtil.WebPageTypeEnum.CHROME.getWebDriver("E:\\000common file\\driver\\browser\\chrome\\chromedriver.exe");
        // 打开指定网页
        webDriver.get("http://www.baidu.com");
        // 获取输入框，输入selenium（id = kw是百度输入框）
        webDriver.findElement(By.id("kw")).sendKeys("百度搜索selenium");
        // id = su是百度搜索按钮进行搜索
        webDriver.findElement(By.id("su")).click();

        String imgScr = webDriver.findElement(By.id("s_lg_img")).getAttribute("src");
        System.out.println("imgSrc = " + imgScr);

        // 退出浏览器
        webDriver.quit();
    }


    @Test
    public void testDownloadImgByUrl() {
        CrawlerUtilClient.downloadImgByUrl("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png", "F:\\wqq\\downloadImg\\百度log.jpg");
    }

    @Test
    public void testDownloadImgWithStaticWebPage() {
//        CrawlerUtilClient.downloadImgWithStaticWebPage("https://www.quanjing.com/image/chengshi.html",
//                "F:\\wqq\\downloadImg",
//                null,
//                "png");

        CrawlerUtilClient.downloadImgWithStaticWebPage("http://wap.yesky.com/pic/364/585172864.shtml",
                "F:\\wqq\\downloadImg",
                "http://",
                "png");
    }



    @Test
    public void test_CrawlerCommonUtil_getAttrNameWithStaticWebPage() throws IOException {
        // 牛客 log 的 a 标签跳转地址
        System.out.println(CrawlerJsoupCommonUtil.getAttrNameWithStaticWebPage("https://www.nowcoder.com/", "#jsApp > header > header > nav > a", "href", CHROME_REQ_HEAD_USER_AGENT));
        System.out.println(CrawlerJsoupCommonUtil.getAttrNameWithStaticWebPage("https://www.nowcoder.com/", "#jsApp > header > header > nav > a", "href", null));

        // 牛客 log 的 img 标签图片地址
        System.out.println(CrawlerJsoupCommonUtil.getAttrNameWithStaticWebPage("https://www.nowcoder.com/", "#jsApp > header > header > nav > a > img", "src", null));

        // 牛客 log 的 a 标签包含内容信息
        System.out.println(CrawlerJsoupCommonUtil.getAttrNameWithStaticWebPage("https://www.nowcoder.com/", "#jsApp > header > header > nav > a", null, null));
    }

    /**
     * 起点中文网 小说下载测试
     */
    @Test
    public void test_CrawlerUtilClient_downloadContentWithStaticWebPage_1() {
        // 替换内容
        Map<String, String> contentReplaceMap = new HashMap<>();
        contentReplaceMap.put("<p>", "");
        contentReplaceMap.put("</p>", SystemUtil.getLineSeparator());
        contentReplaceMap.put("<br>", SystemUtil.getLineSeparator());
        contentReplaceMap.put("<br/>", SystemUtil.getLineSeparator());
        contentReplaceMap.put("\n", SystemUtil.getLineSeparator());

        // 获取小说内容的 html 标签，然后替换 <p> <p/> <br/> <br> 标签内容
        CrawlerUtilClient.downloadContentWithStaticWebPage("https://read.qidian.com/chapter/D4Pic8a3frqLTMDvzUJZaQ2/pdyqJ3Rujv62uJcMpdsVgA2/",
                "#j_chapterBox > div > div > div[class = read-content j_readContent]",
                CrawlerDocumentUtil.PageContentTypeEnum.HTML,
                contentReplaceMap,
                "F:\\wqq\\demo\\novel",
                "txt",
                CHROME_REQ_HEAD_USER_AGENT);

        // 获取小说内容的 html 标签，不替换内容
        CrawlerUtilClient.downloadContentWithStaticWebPage("https://read.qidian.com/chapter/D4Pic8a3frqLTMDvzUJZaQ2/w0Xq2d90qPxOBDFlr9quQA2/",
                "#j_chapterBox > div > div > div[class = read-content j_readContent]",
                CrawlerDocumentUtil.PageContentTypeEnum.HTML,
                null,
                "F:\\wqq\\demo\\novel",
                "txt",
                CHROME_REQ_HEAD_USER_AGENT);

        // 获取小说内容的 text 标签，不替换内容
        CrawlerUtilClient.downloadContentWithStaticWebPage("https://read.qidian.com/chapter/D4Pic8a3frqLTMDvzUJZaQ2/o9u2qont57Rp4rPq4Fd4KQ2/",
                "#j_chapterBox > div > div > div[class = read-content j_readContent]",
                CrawlerDocumentUtil.PageContentTypeEnum.TEXT,
                null,
                "F:\\wqq\\demo\\novel",
                "txt",
                CHROME_REQ_HEAD_USER_AGENT);
    }


    @Test
    public void test_CrawlerImgUtil_cutWebPageByUrl() {
        CrawlerImgUtil.cutWebPageByUrl(CrawlerSeleniumCommonUtil.WebPageTypeEnum.CHROME, "E:\\000common file\\driver\\browser\\chrome\\chromedriver.exe",
                "https://www.baidu.com/", "F:\\wqq\\demo\\cutImg");

        CrawlerImgUtil.cutWebPageByUrl(CrawlerSeleniumCommonUtil.WebPageTypeEnum.CHROME, "E:\\000common file\\driver\\browser\\chrome\\chromedriver.exe",
                "https://www.yiibai.com/java/java-copy-file.html", "F:\\wqq\\demo\\cutImg");
    }


}
