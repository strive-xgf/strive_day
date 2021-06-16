package com.xgf.function;

import com.xgf.randomstr.RandomStrUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author strive_day
 * @create 2021-06-17 0:29
 * @description 测试util服务的 RandomStrUtil 工具类， 随机创建指定格式字符串
 */
public class RandomStrUtilTest {

    @Test
    public void testCreatedRandomStr(){
        // 规则
        List<Integer> ruleList = new ArrayList<>();
        ruleList.add(8);
        ruleList.add(4);
        ruleList.add(4);
        ruleList.add(4);
        ruleList.add(4);
        ruleList.add(18);
        ruleList.add(12);
        List<String> stringList = RandomStrUtil.batchCreateRandomStrWithPrefix("strive_day = ", 1000, 88, "-", ruleList, true);

        stringList.forEach(p -> {
            System.out.println(p.length() + "\t" + p);
            List<String> splitList = Arrays.asList(p.split("-"));
            splitList.forEach(s -> System.out.print(s.length() + "  " + s + "\t"));
            System.out.println();
        });

    }


}
