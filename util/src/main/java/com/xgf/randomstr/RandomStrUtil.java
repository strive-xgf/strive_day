package com.xgf.randomstr;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.*;

/**
 * @author xgf
 * @create 2021-05-28 15:22
 * @description 创建随机字符串工具类
 */
public class RandomStrUtil {

    /**
     * 默认长度
     */
    private static final Integer DEFAULT_LENGTH = 8;

    /**
     * 默认规则集合（UUID规则）
     */
    private static final List<Integer> DEFAULT_RULE_LIST = new ArrayList<Integer>(){{add(8); add(4); add(4); add(4); add(12); }};

    /**
     * 默认间隔字符串 ： -
     */
    private static final String DEFAULT_INTERVAL_STR = "-";

    /**
     * 批量创建随机uuid
     * @param n 数目
     * @return uuid集合
     */
    public static List<String> batchCreateUuid(Integer n){
        if(n <= 0){
            System.err.println("create number Illegal, param : {" + n + "}，need number greater than zero");
            return Collections.emptyList();
        }

        List<String> uuidList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            uuidList.add(UUID.randomUUID().toString());
        }
        return uuidList;
    }

    /**
     * 创建取随机字符串，由数字、大小写字母组成（无间隔）
     * @param length 字符串长度
     * @return String类型随机字符串
     */
    public static String createRandomStr(Integer length){
        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 随机判断判断该字符是数字还是字母，按比例操作，默认数字字母概率1:1
            String choice = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(choice)) {
                // 随机判断是大写字母还是小写字母
                int start = random.nextInt(2) % 2 == 0 ? 65 : 97;
                sb.append((char) (start + random.nextInt(26)));
            } else {
                sb.append(random.nextInt(10));
            }
        }
        return sb.toString();
    }

    /**
     * 获取默认间隔长度的随机字符串，由数字、大小写字母组成，且按规则添加指定字符
     * @param length 字符串长度
     * @param str 添加的指定格式字符串
     * @param flag 长度是否为length，如果为ture：生成长度 = length，为false，生成长度 = length + (int)length/interval*str.length()
     * @return String类型随机字符串
     */
    public static String createRandomStr(Integer length, String str, boolean flag){
        return createRandomStr(length, str, DEFAULT_LENGTH, flag);
    }

    /**
     * 获取随机字符串，由数字、大小写字母组成，且每几个字符之后添加指定字符
     * @param length 字符串长度
     * @param str 添加的指定格式字符串
     * @param interval 每几位添加
     * @param flag 长度是否为length，如果为ture：生成长度 = length，为false，生成长度 = length + (int)length/interval
     * @return String类型随机字符串
     */
    public static String createRandomStr(Integer length, String str, Integer interval, Boolean flag) {
        if (length <= 0) {
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 1; i <= length; i++) {
            // 随机判断判断该字符是数字还是字母
            String choice = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(choice)) {
                // 随机判断是大写字母还是小写字母
                int start = random.nextInt(2) % 2 == 0 ? 65 : 97;
                sb.append((char) (start + random.nextInt(26)));
            } else {
                sb.append(random.nextInt(10));
            }

            if (i % interval == 0 && length != i) {
                // 开启间隔算长度
                if (flag) {
                    if ((length - i) < str.length()) {
                        // 只追加一部分
                        sb.append(str.substring(0, (length - i)));
                    } else {
                        // 追加全部间隔符
                        sb.append(str);
                    }
                    // 间隔符算长度（<=0 都满足条件）
                    length = length - str.length();
                } else {
                    // 追加全部间隔符
                    sb.append(str);
                }
            }

        }
        return sb.toString();
    }

    /**
     * 获取随机字符串，由数字、大小写字母组成，且每几个字符之后添加指定字符
     * @param length 字符串长度
     * @param str 添加的指定格式字符串
     * @param intervalRuleList 间隔添加自定义数组规则，按照定义数组来添加
     * @param flag 长度是否等于length，如果为ture：生成长度 = length，为false，生成长度 = length + (int)length/interval
     * @return String类型随机字符串
     */
    public static String createRandomStr(Integer length, String str, List<Integer> intervalRuleList, Boolean flag){
        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return "";
        }

        // 复制集合（集合传引用）
        List<Integer> ruleList = new ArrayList<>(intervalRuleList.size());
        ruleList.addAll(intervalRuleList);

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 1, k = 1; i <= length; i++, k++) {
            // 随机判断判断该字符是数字还是字母
            String choice = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(choice)) {
                // 随机判断是大写字母还是小写字母
                int start = random.nextInt(2) % 2 == 0 ? 65 : 97;
                sb.append((char) (start + random.nextInt(26)));
            } else {
                sb.append(random.nextInt(10));
            }

            if (CollectionUtils.isNotEmpty(ruleList)
                    && k % ruleList.get(0) == 0
                    && length != i) {
                // 重置k遍历
                k = 0;
                // 移除生效的规则条件
                ruleList.remove(ruleList.get(0));
                // 开启间隔算长度
                if (flag) {
                    if((length - i) < str.length()){
                        // 只追加一部分
                        sb.append(str.substring(0, (length - i)));
                    }else {
                        // 追加全部间隔符
                        sb.append(str);
                    }
                    // 间隔符算长度（<=0 都满足条件）
                    length = length - str.length();
                }else {
                    // 追加全部间隔符
                    sb.append(str);
                }
            }

        }
        return sb.toString();
    }

    /**
     * 创建指定长度，带随机字符串前缀的
     * @param length 字符串长度
     * @param prefix 前缀
     * @param str 间隔字符串
     * @param intervalList 间隔规则数组
     * @param flag 长度是否等于length，如果为ture：生成长度 = length，为false，生成长度 = length + (int)length/interval
     * @return String类型随机字符串
     */
    public static String createRandomStrWithPrefix(String prefix, Integer length, String str, List<Integer> intervalList, Boolean flag){
        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return "";
        }

        if(length <= prefix.length()){
            return prefix.substring(0, length);
        }
        return prefix + createRandomStr(length - prefix.length(), str, intervalList, flag);
    }


    public static String createRandomStrWithPrefix(String prefix, Integer length, String str, Integer interval, Boolean flag){
        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return "";
        }

        if(length <= prefix.length()){
            return prefix.substring(0, length);
        }
        return prefix + createRandomStr(length - prefix.length(), str, interval, flag);
    }

    /**
     * 带前缀，指定长度，长度不算间隔符
     * @param length 字符串长度
     * @param prefix 前缀
     * @return String类型随机字符串
     */
    public static String createRandomStrWithPrefix(String prefix, Integer length){
        return createRandomStrWithPrefix(prefix, length, DEFAULT_INTERVAL_STR, DEFAULT_LENGTH, false);
    }

    public static String createRandomStrWithPrefix(String prefix, Integer length, Boolean flag){
        return createRandomStrWithPrefix(prefix, length, DEFAULT_INTERVAL_STR, DEFAULT_LENGTH, flag);
    }

    public static String createRandomStrWithPrefix(String prefix, Integer length, String str){
        return createRandomStrWithPrefix(prefix, length, str, DEFAULT_LENGTH, false);
    }

    public static String createRandomStrWithPrefix(String prefix, Integer length, String str, Integer interval){
        return createRandomStrWithPrefix(prefix, length, str, interval, false);
    }


    /**
     * 创建随机字符串数组
     * @param prefix 前缀
     * @param n 大小数量
     * @param length 字符串长度
     * @param str 每隔多少字符插入字符串
     * @param interval 间隔长度
     * @param flag 长度是否等于length，如果为ture：生成长度 = length，为false，生成长度 = length + (int)length/interval
     * @return String类型随机字符串列表
     */
    public static List<String> batchCreateRandomStrWithPrefix(String prefix, Integer n, Integer length, String str, Integer interval, Boolean flag){
        if(n <= 0){
            System.err.println("create number Illegal, param : {" + n + "}，need number greater than zero");
            return new ArrayList<>();
        }

        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return new ArrayList<>(Collections.nCopies(n, ""));
        }

        if(length <= prefix.length()){
            // 全部返回子前缀, n个
            return new ArrayList<String>(Collections.nCopies(n, prefix.substring(0, length)));
        }
        List<String> strList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            strList.add(prefix + createRandomStr(length - prefix.length(), str, interval, flag));
        }
        return strList;
    }

    /**
     * 批量创建指定长度，带随机字符串前缀的
     * @param length 字符串长度
     * @param prefix 前缀
     * @param flag 长度是否等于length，如果为ture：生成长度 = length，为false，生成长度 = length + (int)length/interval
     * @param n 创建个数
     * @param str 间隔符
     * @param intervalRuleList 间隔规则数组
     * @return String类型随机字符串列表
     */
    public static List<String> batchCreateRandomStrWithPrefix(String prefix, Integer n, Integer length, String str, List<Integer> intervalRuleList, Boolean flag){
        if(n <= 0){
            System.err.println("create number Illegal, param : {" + n + "}，need number greater than zero");
            return new ArrayList<>();
        }

        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return new ArrayList<>(Collections.nCopies(n, ""));
        }

        if(length <= prefix.length()){
            // 全部返回子前缀, n个
            return new ArrayList<String>(Collections.nCopies(n, prefix.substring(0, length)));
        }
        List<String> strList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            strList.add(prefix + createRandomStr(length - prefix.length(), str, intervalRuleList, flag));
        }
        return strList;
    }

    public static List<String> batchCreateRandomStrWithPrefix(String prefix, Integer n, Integer length){
        return batchCreateRandomStrWithPrefix(prefix, n, length, DEFAULT_INTERVAL_STR, DEFAULT_RULE_LIST, false);
    }

    public static List<String> batchCreateRandomStrWithPrefix(String prefix, Integer n, Integer length, Boolean flag){
        return batchCreateRandomStrWithPrefix(prefix, n, length, DEFAULT_INTERVAL_STR, DEFAULT_RULE_LIST, flag);
    }

    public static List<String> batchCreateRandomStrWithPrefix(String prefix, Integer n, Integer length, String str){
        return batchCreateRandomStrWithPrefix(prefix, n, length, str, DEFAULT_RULE_LIST, false);
    }

    public static List<String> batchCreateRandomStrWithPrefix(String prefix, Integer n, Integer length, String str, List<Integer> intervalRuleList){
        return batchCreateRandomStrWithPrefix(prefix, n, length, str, intervalRuleList, false);
    }


    /**
     * 创建指定长度，带后缀的字符串
     * @param length 字符串长度
     * @param suffix 后缀
     * @param str 间隔字符串
     * @param intervalList 间隔规则数组
     * @param flag 长度是否等于length，如果为ture：生成长度 = length，为false，生成长度 = length + (int)length/interval
     * @return String类型随机字符串
     * */
    public static String createRandomStrWithSuffix(String suffix, Integer length, String str, List<Integer> intervalList, Boolean flag){
        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return "";
        }

        if(length <= suffix.length()){
            return suffix.substring(0, length);
        }
        return createRandomStr(length - suffix.length(), str, intervalList, flag) + suffix;
    }

    public static String createRandomStrWithSuffix(String suffix, Integer length, String str, Integer interval, Boolean flag){
        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return "";
        }
        if(length <= suffix.length()){
            return suffix.substring(0, length);
        }
        return createRandomStr(length - suffix.length(), str, interval, flag) + suffix;
    }

    public static String createRandomStrWithSuffix(String suffix, Integer length){
        return createRandomStrWithSuffix(suffix, length, DEFAULT_INTERVAL_STR, DEFAULT_LENGTH, false);
    }

    public static String createRandomStrWithSuffix(String suffix, Integer length, Boolean flag){
        return createRandomStrWithSuffix(suffix, length, DEFAULT_INTERVAL_STR, DEFAULT_LENGTH, flag);
    }

    public static String createRandomStrWithSuffix(String suffix, Integer length, String str){
        return createRandomStrWithSuffix(suffix, length, str, DEFAULT_LENGTH, false);
    }

    public static String createRandomStrWithSuffix(String suffix, Integer length, String str, Integer interval){
        return createRandomStrWithSuffix(suffix, length, str, interval, false);
    }

    /**
     * 批量创建带后缀，随机字符串数组
     * @param suffix 后缀
     * @param n 大小数量
     * @param length 字符串长度
     * @param str 每隔多少字符插入字符串
     * @param interval 间隔长度
     * @param flag 长度是否等于length，如果为ture：生成长度 = length，为false，生成长度 = length + (int)length/interval
     * @return String类型随机字符串列表
     */
    public static List<String> batchCreateRandomStrWithSuffix(String suffix, Integer n, Integer length, String str, Integer interval, Boolean flag){
        if(n <= 0){
            System.err.println("create number Illegal, param : {" + n + "}，need number greater than zero");
            return new ArrayList<>();
        }

        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return new ArrayList<>(Collections.nCopies(n, ""));
        }

        if(length <= suffix.length()){
            // 全部返回子前缀, n个
            return new ArrayList<String>(Collections.nCopies(n, suffix.substring(0, length)));
        }
        List<String> strList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            strList.add(createRandomStr(length - suffix.length(), str, interval, flag) + suffix);
        }
        return strList;
    }

    /**
     * 批量创建指定长度，带随机字符串前缀的
     * @param length 字符串长度
     * @param suffix 后缀
     * @param flag 长度是否等于length，如果为ture：生成长度 = length，为false，生成长度 = length + (int)length/interval
     * @param n 创建个数
     * @param str 间隔符
     * @param intervalRuleList 间隔规则数组
     * @return String类型随机字符串列表
     */
    public static List<String> batchCreateRandomStrWithSuffix(String suffix, Integer n, Integer length, String str, List<Integer> intervalRuleList, Boolean flag){
        if(n <= 0){
            System.err.println("create number Illegal, param : {" + n + "}，need number greater than zero");
            return new ArrayList<>();
        }

        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return new ArrayList<>(Collections.nCopies(n, ""));
        }

        if(length <= suffix.length()){
            // 全部返回子前缀, n个
            return new ArrayList<String>(Collections.nCopies(n, suffix.substring(0, length)));
        }
        List<String> strList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            strList.add(createRandomStr(length - suffix.length(), str, intervalRuleList, flag) + suffix);
        }
        return strList;
    }

    public static List<String> batchCreateRandomStrWithSuffix(String suffix, Integer n, Integer length){
        return batchCreateRandomStrWithSuffix(suffix, n, length, DEFAULT_INTERVAL_STR, DEFAULT_RULE_LIST, false);
    }

    public static List<String> batchCreateRandomStrWithSuffix(String suffix, Integer n, Integer length, Boolean flag){
        return batchCreateRandomStrWithSuffix(suffix, n, length, DEFAULT_INTERVAL_STR, DEFAULT_RULE_LIST, flag);
    }

    public static List<String> batchCreateRandomStrWithSuffix(String suffix, Integer n, Integer length, String str){
        return batchCreateRandomStrWithSuffix(suffix, n, length, str, DEFAULT_RULE_LIST, false);
    }

    public static List<String> batchCreateRandomStrWithSuffix(String suffix, Integer n, Integer length, String str, List<Integer> intervalRuleList){
        return batchCreateRandomStrWithSuffix(suffix, n, length, str, intervalRuleList, false);
    }


    /**
     * 创建指定长度，带前后缀的字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @param length 字符串长度
     * @param str 间隔字符串
     * @param intervalList 间隔规则数组
     * @param flag 长度是否等于length，如果为ture：生成长度 = length，为false，生成长度 = length + (int)length/interval
     * @return String类型随机字符串
     */
    public static String createRandomStrWithPrefixAndSuffix(String prefix, String suffix, Integer length, String str, List<Integer> intervalList, Boolean flag){
        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return "";
        }

        if(length <= prefix.length()){
            return prefix.substring(0, length);
        }

        if(length <= (prefix.length() + suffix.length())){
            return prefix + suffix.substring(0, length - prefix.length());
        }
        return prefix + createRandomStr(length - prefix.length() - suffix.length(), str, intervalList, flag) + suffix;
    }

    public static String createRandomStrWithPrefixAndSuffix(String prefix, String suffix, Integer length, String str, Integer interva, Boolean flag){
        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return "";
        }

        if(length <= prefix.length()){
            return prefix.substring(0, length);
        }

        if(length <= (prefix.length() + suffix.length())){
            return prefix + suffix.substring(0, length - prefix.length());
        }
        return prefix + createRandomStr(length - prefix.length() - suffix.length(), str, interva, flag) + suffix;
    }

    public static String createRandomStrWithPrefixAndSuffix(String suffix, Integer length, String str, List<Integer> intervalList, Boolean flag){
        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return "";
        }

        if(length <= suffix.length()){
            return suffix.substring(0, length);
        }
        return createRandomStr(length - suffix.length(), str, intervalList, flag) + suffix;
    }

    public static String createRandomStrWithPrefixAndSuffix(String suffix, Integer length, String str, Integer interval, Boolean flag){
        if(length <= 0){
            System.err.println("create number Illegal, param : {" + length + "}，need number greater than zero");
            return "";
        }
        if(length <= suffix.length()){
            return suffix.substring(0, length);
        }
        return createRandomStr(length - suffix.length(), str, interval, flag) + suffix;
    }

    public static String createRandomStrWithPrefixAndSuffix(String suffix, Integer length){
        return createRandomStrWithSuffix(suffix, length, DEFAULT_INTERVAL_STR, DEFAULT_LENGTH, false);
    }

    public static String createRandomStrWithPrefixAndSuffix(String suffix, Integer length, Boolean flag){
        return createRandomStrWithSuffix(suffix, length, DEFAULT_INTERVAL_STR, DEFAULT_LENGTH, flag);
    }

    public static String createRandomStrWithPrefixAndSuffix(String suffix, Integer length, String str){
        return createRandomStrWithSuffix(suffix, length, str, DEFAULT_LENGTH, false);
    }

    public static String createRandomStrWithPrefixAndSuffix(String suffix, Integer length, String str, Integer interval){
        return createRandomStrWithSuffix(suffix, length, str, interval, false);
    }


    /**
     * 创建指定大小和长度的随机字符串列表
     * @param n 大小
     * @param length 长度
     * @return String类型随机字符串列表
     */
    public static List<String> batchCreateRandomList(Integer n, Integer length){
        if(n <= 0){
            return Collections.emptyList();
        }

        if(length <= 0){
            return new ArrayList<>(n);
        }

        List<String> randomList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            randomList.add(createRandomStr(length));
        }
        return randomList;
    }

    /**
     * 创建随机字符串数组
     * @param n 大小数量
     * @param length 字符串长度
     * @param str 每隔多少字符插入字符串
     * @param interval 间隔长度
     * @param flag 长度是否等于length，如果为ture：生成长度 = length，为false，生成长度 = length + (int)length/interval
     * @return String类型随机字符串列表
     */
    public static List<String> batchCreateRandomList(Integer n, Integer length, String str, Integer interval, Boolean flag){
        if(n <= 0){
            return Collections.emptyList();
        }

        if(length <= 0){
            return new ArrayList<>(n);
        }

        List<String> randomList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            randomList.add(createRandomStr(length, str, interval, flag));
        }
        return randomList;
    }


    /**
     * 创建随机字符串数组
     * @param n 大小数量
     * @param length 字符串长度
     * @param str 每隔多少字符插入字符串
     * @param intervalRuleList 自定义间隔数组规则，按照数组规则来实现间隔
     * @param flag 长度是否等于length，如果为ture：生成长度 = length，为false，生成长度 = length + (int)length/interval*str.length()
     * @return String类型随机字符串列表
     */
    public static List<String> batchCreateRandomList(Integer n, Integer length, String str, List<Integer> intervalRuleList, Boolean flag){
        if(n <= 0){
            return Collections.emptyList();
        }

        if(length <= 0){
            return new ArrayList<>(n);
        }

        List<String> randomList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            randomList.add(createRandomStr(length, str, intervalRuleList, flag));
        }
        return randomList;
    }




    @Test
    public void test(){
//        System.out.println(batchCreateUuid(10));
//        System.out.println(batchCreateUuid(10, ""));
//        System.out.println(batchCreateUuid(-2));
//        System.out.println(batchCreateUuid(-2, ""));
//        System.out.println(createRandomStr(101, "-", true));
//        System.out.println(createRandomStr(101, "-", false));
//        System.out.println(batchCreateRandomList(10, 66));
//        System.out.println(batchCreateRandomList(8, 36, "-", 6, true));
//
//        System.out.println(batchCreateUuid(20, "-"));
//        System.out.println();
//        System.out.println(batchCreateUuid(20, ""));
//        System.out.println();
//        System.out.println(batchCreateRandomList(10, 10));
//
//
//
//
//
//        System.out.println(batchCreateRandomList(8, 30, "--", 6, true));
//        System.out.println(batchCreateRandomList(8, 32, "--", 6, false));
//
//
//
        List<Integer> ruleList = new ArrayList<>();
        ruleList.add(8);
        ruleList.add(4);
        ruleList.add(4);
        ruleList.add(4);
        ruleList.add(4);
        ruleList.add(18);
        ruleList.add(12);
////        System.out.println(batchCreateRandomList(1000, 100, "-", ruleList, true));
////
////
////        // 前缀
////        System.out.println(createRandomStrWithPrefix(28 , "strive_day=", false).length() + "  >>>  " + createRandomStrWithPrefix(28, "strive_day=", false));
//
//
//        List<String> stringList = batchCreateRandomStrWithPrefix("strive_day = ", 1000, 200, "-", ruleList, true);
//        stringList.forEach(p -> {
//            System.out.println(p.length() + "\t" + p);
//            List<String> splitList = Arrays.asList(p.split("-"));
//            splitList.forEach(s -> System.out.print(s.length() + "  " + s + "\t"));
//            System.out.println();
//        });

//        String suffixStr = createRandomStrWithSuffix("study_strive_day_day_to_go", 100, "-", 10, true);
//        System.out.println(suffixStr.length() + "\t" + suffixStr);
//
//        List<String> stringList = batchCreateRandomStrWithSuffix("study_strive_day_day_to_go", -2, 88, "-", ruleList, true);
//        stringList.forEach(p -> {
//            System.out.println(p.length() + "\t" + p);
//            List<String> splitList = Arrays.asList(p.split("-"));
//            splitList.forEach(s -> System.out.print(s.length() + "  " + s + "\t"));
//            System.out.println();
//        });

//        String randomStrWithPrefixAndSuffix = createRandomStrWithPrefixAndSuffix("strive_xgf_", "_study_strive_day_day_to_go", 88, "-=-", ruleList, true);
//        System.out.println(randomStrWithPrefixAndSuffix.length() + "\t" + randomStrWithPrefixAndSuffix);


    }



}
