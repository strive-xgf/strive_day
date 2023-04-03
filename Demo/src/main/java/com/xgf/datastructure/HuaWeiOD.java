package com.xgf.datastructure;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author strive_day
 * @create 2023-04-02 18:15
 * @description
 */
public class HuaWeiOD {

    /**
     * 文件扫描成本 = 文件大小（第三行输入） N
     * 缓存报告需要 M 金币，后序使用不需要扫描成本
     */
    @Test
    public void test1() {
        Scanner scan = new Scanner(System.in);

        while (scan.hasNextInt()) {
            // 缓存消耗金币数
            int buffCount = scan.nextInt();
            // 吃掉回车
            scan.nextLine();
            // 文件标识序列
            String[] fileList = scan.nextLine().split(" ");
            // 文件大小序列
            String[] fileSizeList = scan.nextLine().split(" ");

            // 文件和遍历大小map
            HashMap<String, Integer> file2SizeMap = new HashMap<>();
            // 文件和遍历全部成本map
            HashMap<String, Integer> file2AllMap = new HashMap<>();


            // 遍历求出每种文件全部读取成本
            for (int i = 0; i < fileList.length; i++) {
                int fileSize = Integer.parseInt(fileSizeList[i]);
                file2SizeMap.put(fileList[i], fileSize);
                file2AllMap.put(fileList[i], file2AllMap.getOrDefault(fileList[i], 0) + fileSize);
            }

            // 最小消耗金币数
            int minCount = 0;

            for (Map.Entry<String, Integer> entry : file2AllMap.entrySet()) {
                // 遍历 + 缓存消耗
                int buffSize = file2SizeMap.get(entry.getKey()) + buffCount;
                // 比较遍历和缓存，去成本小的
                minCount += entry.getValue() > buffSize ? buffSize : entry.getValue();
            }

            System.out.println(minCount);

        }

    }


    /**
     * 密码匹配
     * 输入：密码，排序后忽略大小写，然后字母全匹配
     * 第二行输入：找出所有的字母，忽略大小写后排序
     * 输出：返回匹配的字符串序号（从1开始）
     */
    @Test
    public void test2() {

        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {

            // 密码key
            String key = scan.nextLine();
            // 匹配箱子box（空格分隔）
            String[] boxList = scan.nextLine().split(" ");
            // 获取匹配下标
            int legalIndex = getLegalIndex(key, Arrays.asList(boxList));
            // 打印
            System.out.println(legalIndex);

        }

    }

    /**
     * 匹配密码，获取合法密码下标
     *
     * @param key     密码key
     * @param boxList 箱子字符串集合
     * @return 找到返回对应的数组下标（从1开始），-1：未找到
     */
    public static int getLegalIndex(String key, List<String> boxList) {
        // 将字符只取小写字母，且排序
        String realKey = getAllLowerLetter(key);
        // 转换
        List<String> realLetterList = boxList.stream().map(p -> getAllLowerLetter(p)).collect(Collectors.toList());

        for (int i = 0; i < realLetterList.size(); i++) {
            if (realKey.equals(realLetterList.get(i))) {
                return i + 1;
            }
        }
        // 未找到返回-1
        return -1;

    }

    /**
     * 获取字符串中的所有字母，排序小写输出
     *
     * @param str 字符串入参
     * @return 取字符串中的所有字母，小写然后排序后输出
     */
    public static String getAllLowerLetter(String str) {
        if (str == null) {
            return null;
        }

        // 遍历只取字母，且小写
        String lowerStr = str.toLowerCase();

        // 所有字母数组
        List<Character> letterList = new ArrayList();
        // 只取字母
        for (Character curr : lowerStr.toCharArray()) {
            if (Character.isLetter(curr)) {
                letterList.add(curr);
            }
        }

        // 字符串自然排序
        letterList.sort(Character::compareTo);

        // 转换为字符串
        StringBuilder sb = new StringBuilder();
        letterList.forEach(sb::append);

        return sb.toString();
    }


    /**
     * 【字母组合】
     * <p>
     * <p>
     * 屏蔽字符：屏蔽字符，只是不能全部同时出现（拆分可以出现），取子集不能出现
     * <p>
     * 要求：给定一个数字字符串和屏蔽字符串，输出所有可能的字符组合
     * 要求：组合要按照数字的顺序组合字母字符串
     */
    @Test
    public void test3() {
        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
            // 数字字符串
            String numStr = scan.nextLine();
            // 屏蔽字符串
            String excludeStr = scan.nextLine();
            // 获取所有数字字符串的组合结果
            List<String> allZuHeByNumStrList = getAllZuHeByNumStr(numStr);
            // 过滤屏蔽字符串，并输出结果
            allZuHeByNumStrList.stream().filter(p -> !filterString(p, excludeStr)).forEach(p -> System.out.print(p + ","));
            // 最后回车
            System.out.println();
        }

    }

    /**
     * 过滤字符串，只要字符串中同时存在屏蔽字符串中的所有字符，就过滤
     *
     * @param str        判断是否需要过滤的字符串
     * @param excludeStr 屏蔽字符串
     * @return true: 需要过滤
     */
    public static boolean filterString(String str, String excludeStr) {
        if (excludeStr == null || excludeStr.length() < 1) {
            return false;
        }

        for (Character curr : excludeStr.toCharArray()) {
            if (!str.contains(String.valueOf(curr))) {
                return false;
            }
        }

        return true;
    }


    /**
     * 根据数字字符串，获取对应字母的所有有序组合
     *
     * @param numStr 数字字符串
     * @return 所有有序的组合
     */
    public static List<String> getAllZuHeByNumStr(String numStr) {

        if (numStr == null || numStr.length() < 1) {
            return new ArrayList<>();
        }

        // 获取默认数字和字母组合Map
        Map<String, List<String>> defaultInt2StrListMap = convertDefaultInt2StringMap(getDefaultInt2StringMap());

        // 两两组合结果（初始值为第一个字符的所有组合），每次拿到组合后的数据，再去和下一个组合进行合并
        List<String> resultList = defaultInt2StrListMap.get(String.valueOf(numStr.charAt(0)));

        // 组合所有可能
        for (int i = 1; i < numStr.length(); i++) {
            // 两两组合，组合结果再和下一个结果两两组合
            resultList = getZuHeByList(resultList, defaultInt2StrListMap.get(String.valueOf(numStr.charAt(i))), 0);
        }

        return resultList;
    }


    /**
     * 递归实现两两排序组合, 两个list有序，按照前面的组合后面的list
     *
     * @param strList1  字符串List集合1（排在前）
     * @param strList2  字符串List集合2
     * @param str1Index 从0开始，匹配strList1下标
     */
    public static List<String> getZuHeByList(List<String> strList1, List<String> strList2, Integer str1Index) {
        List<String> resultList = new ArrayList<>();
        // 超出则不继续遍历
        if (strList1.size() == str1Index) {
            return resultList;
        }

        // 组合前缀
        String prefixS1 = strList1.get(str1Index);
        // 去前一个数组对应的下标值，然后排序后面所有数组
        strList2.forEach(p -> resultList.add(prefixS1 + p));

        // 递归执行下一组合，并将结果放入集合中
        resultList.addAll(getZuHeByList(strList1, strList2, str1Index + 1));

        return resultList;
    }


    /**
     * 将默认的getDefaultInt2StringMap，转换成需要的数据格式 Map<String, List<String>>
     */
    public static Map<String, List<String>> convertDefaultInt2StringMap(Map<String, String> paramMap) {
        HashMap<String, List<String>> int2strListMap = new HashMap<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            int2strListMap.put(entry.getKey(), Arrays.asList(entry.getValue().split(",")));
        }
        return int2strListMap;
    }

    /**
     * 默认数字和字母的组合
     *
     * @return 根据条件，默认值Map集合
     */
    public static Map<String, String> getDefaultInt2StringMap() {
        HashMap<String, String> int2StrMap = new HashMap<>();

        // 赋值默认值
        int2StrMap.put("0", "a,b,c");
        int2StrMap.put("1", "d,e,f");
        int2StrMap.put("2", "g,h,i");
        int2StrMap.put("3", "j,k,l");
        int2StrMap.put("4", "m,n,o");
        int2StrMap.put("5", "p,q,r");
        int2StrMap.put("6", "s,t");
        int2StrMap.put("7", "u,v");
        int2StrMap.put("8", "w,x");
        int2StrMap.put("9", "y,z");

        return int2StrMap;
    }


    @Test
    public void demo() {
        System.out.println(getAllLowerLetter("s,asdfjkj91273jhsd"));
        System.out.println(getAllLowerLetter("a-983asdjk1=-aa"));
        System.out.println(getAllLowerLetter("KKKa-983asdjk1=-aa"));
    }


}
