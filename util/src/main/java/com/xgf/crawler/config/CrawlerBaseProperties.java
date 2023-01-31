package com.xgf.crawler.config;

import com.xgf.constant.DataRange;
import com.xgf.constant.IntegerConstantUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.java8.ThrowExceptionFunctionUtil;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author strive_day
 * @create 2023-01-26 1:08
 * @description 爬虫配置基本属性
 */

@Data
@Component
@ConfigurationProperties("custom.config.crawler.properties")
public class CrawlerBaseProperties {

    /**
     * 浏览器类型 需要打开网页加载的情况需要指定浏览器类型  chrome / ie / firefox / edge ...
     */
    private String browserType;

    /**
     * 浏览器驱动路径 webdriver todo 自动获取
     */
    private String webDriverPath;

    /**
     * 设置请求头 User-Agent 属性来模拟浏览器运行，避免server returned HTTP response code: 403 for URL
     */
    private String reqHeadParamUserAgent;

    /**
     * 网页 url 集合
     */
    private String[] webPageUrlList;

    /**
     * 本地下载路径
     */
    private String downloadPath;


    /**
     * 校验和默认填充值，让大部分情况只需要 webPageUrlList 就可以下载
     */
    public void checkAndDefaultFill() {

//        ThrowExceptionFunctionUtil.isTureThrow(StringUtils.isBlank(browserType)).throwMessage("浏览器类型不能为空，请选择浏览器类型 chrome / ie / firefox / edge");
//        ThrowExceptionFunctionUtil.isTureThrow(StringUtils.isBlank(webDriverPath)).throwMessage("浏览器驱动路径，请选择浏览器驱动webdriver路径");
        ThrowExceptionFunctionUtil.isTureThrow(CollectionUtils.isEmpty(Arrays.asList(webPageUrlList))).throwMessage("目标网址不能为空");
        ThrowExceptionFunctionUtil.isTureThrow(StringUtils.isBlank(downloadPath)).throwMessage("目标下载路径不能为空");

    }




    /**
     * 网址解析通配符工具类，解析 webPageUrlList
     */
    @Data
    public static class WebPageUrlParseProperties {
        /**
         * 原 url 网址
         */
        private String sourceUrl;

        /**
         * 通配符字符串，eg: [1-100] todo 多个通配符匹配？ xx/yy[1-100]/zz[2-20] 。。。改List？
         */
        private String wildcardStr;

        /**
         * 通配符解析出来的范围，暂时只支持数字解析，eg: [1-100], 则 lower = 1, upper = 100，不存在通配符，则此值为 null
         */
        private DataRange<Integer> dataRange;

        /**
         * 将 url 解析成 WebPageUrlParseProperties 对象
         *
         * @param url 网址url
         * @return WebPageUrlParseProperties
         */
        public static WebPageUrlParseProperties parseWebPageUrl(String url) {
            WebPageUrlParseProperties result = new WebPageUrlParseProperties();
            result.setSourceUrl(url);

            // 解析通配符 最先一个 [ 开始 最后一个 ] 结束包含的字符串
            String wildcardStr = StringConstantUtil.substringByPreSuf(url, StringConstantUtil.LEFT_MIDDLE_BRACKET, StringConstantUtil.RIGHT_MIDDLE_BRACKET);
            if (StringUtils.isBlank(wildcardStr)) {
                return result;
            }
            result.setWildcardStr(wildcardStr);

            // 解析通配符
            result.setDataRange(WebPageUrlParseProperties.convertDataRange(result));

            return result;
        }


        public DataRange<Integer> convertDataRange() {
            return convertDataRange(this);
        }

        /**
         * 解析当前通配符值wildcardStr，转换为 dataRange 数据范围
         *
         * @param urlProperties WebPageUrlParseProperties
         */
        public static DataRange<Integer> convertDataRange(WebPageUrlParseProperties urlProperties) {
            String wildcardStr = urlProperties.getWildcardStr();
            if (StringUtils.isBlank(wildcardStr)) {
                return null;
            }

            // 解析通配符（去掉首尾字符[] 去掉空字符，按 - 分隔数组 ）
            String[] splitArr = wildcardStr.substring(1, wildcardStr.length() - 1).replace(" ", "").split(StringConstantUtil.SUBTRACT);

            // 暂时只支持一个 eg: [1-100]，不支持[1-100-1000]等
            ThrowExceptionFunctionUtil.isTureThrow(splitArr.length > 2).throwMessage("通配符值不合法: " + wildcardStr);

            int firstInt = IntegerConstantUtil.convertStrToIntThrow(splitArr[0], "通配符值不合法: " + wildcardStr);
            if (splitArr.length == 1) {
                return DataRange.valueOf(firstInt, firstInt);
            } else {
                int secondInt = IntegerConstantUtil.convertStrToIntThrow(splitArr[1], "通配符值不合法: " + wildcardStr);
                return DataRange.valueOf(firstInt, secondInt);
            }
        }

    }

}
