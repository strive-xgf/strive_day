package com.xgf.crawler.inner;

import com.xgf.constant.EnumBase;
import com.xgf.exception.CustomExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.util.stream.Stream;

/**
 * @author strive_day
 * @create 2023-02-16 15:23
 * @description 爬虫通用工具类（基于 selenium 封装）【静态网址）
 */
public class CrawlerSeleniumCommonUtil {


    /**
     * 获取 WebDriver
     *
     * @param webPageTypeEnum 浏览器类型信息枚举
     * @param driverPath 驱动路径
     * @return WebDriver
     */
    public static WebDriver getWebDriver(WebPageTypeEnum webPageTypeEnum, String driverPath) {
        return webPageTypeEnum.getWebDriver(driverPath);
    }

    /**
     * 获取 WebDriver
     *
     * @param webPageTypeCode 浏览器类型信息枚举code WebPageTypeEnum
     * @param driverPath 驱动路径
     * @return WebDriver
     */
    public static WebDriver getWebDriver(String webPageTypeCode, String driverPath) {
        return CrawlerSeleniumCommonUtil.WebPageTypeEnum.getWebDriver(webPageTypeCode, driverPath);
    }




    /**
     * 基于 selenium 封装浏览器 webDriver 信息枚举
     */
    @Getter
    @AllArgsConstructor
    public enum WebPageTypeEnum implements EnumBase {
        CHROME("chrome", "谷歌浏览器", "webdriver.chrome.driver"),
        FIREFOX("fireFox", "火狐浏览器", "webdriver.gecko.driver"),
        EDGE("edge", "edge浏览器", "webdriver.edge.driver"),
        IE("ie", "ie浏览器", "webdriver.ie.driver"),
        ;

        /**
         * 浏览器 code
         */
        private final String code;

        /**
         * 浏览器描述
         */
        private final String value;

        /**
         * 驱动 key
         */
        private final String key;

        public static WebPageTypeEnum getByCodeThrow(String code) {
            WebPageTypeEnum result = getByCode(code);

            if (result == null) {
                throw CustomExceptionEnum.ENUM_TYPE_ILLEGAL_EXCEPTION.generateException("WebPageTypeEnum code = " + code);
            }

            return result;
        }

        public static WebPageTypeEnum getByCode(String code) {
            return Stream.of(WebPageTypeEnum.values()).filter(p -> p.getCode().equals(code)).findFirst().orElse(null);
        }

        public WebDriver getWebDriver(String driverPath) {
            // 指定驱动路径 和 属性
            System.setProperty(this.key, driverPath);
            // 初始化Chrome浏览器实例
            return this == CHROME
                    ? new ChromeDriver()
                    : this == FIREFOX
                    ? new FirefoxDriver()
                    : this == EDGE
                    ? new EdgeDriver()
                    : this == IE
                    ? new InternetExplorerDriver()
                    : null;
        }

        public static WebDriver getWebDriver(String webPageTypeCode, String driverPath) {
            return WebPageTypeEnum.getByCodeThrow(webPageTypeCode).getWebDriver(driverPath);
        }

    }
}
