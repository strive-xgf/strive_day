package com.xgf.lock.redis;

import com.xgf.DemoApplication;
import com.xgf.lock.redis.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xgf
 * @create 2022-07-28 19:10
 * @description
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class RedisUtilTest {

    /**
     * 模拟一堆请求端口对同一个数据加锁访问
     */
    @Test
    public void test1() throws InterruptedException {
        Jedis jedis = new Jedis("localhost");

        // redis key 所有用户争这个key
        String redisKey = "redisKey_AAA";

        // 模拟多个客户端
        List<String> requestIdList = new ArrayList<>();
        // 成功获取并完成的客户端Id
        List<String> successIdList = new ArrayList<>(16);

        for (int i = 0; i < 10; i++) {
            requestIdList.add("ClientRequestId" + i + i + i);
        }

        int count = 1;
        while (requestIdList.size() != 0) {
            for (int i = 0; i < requestIdList.size(); i++) {
                // 当前请求客户端id
                String requestId = requestIdList.get(i);
                System.out.println(">>>>>> execute count : " + count ++);
                boolean b = RedisUtil.tryGetDistributedLock(jedis, redisKey, requestId, 5000L);
                // 获取到锁，处理业务逻辑
                if (b) {
                    System.out.println(">>>>>> requestId = " + requestId + ", deal custom logic");
                    // 处理成功
                    successIdList.add(requestId);
                    if (i % 2 == 0) {
                        // 对 2 取模来释放锁，一般处理完成之后直接释放锁
                        boolean releaseLockFlag = RedisUtil.releaseDistributedLock(jedis, redisKey, requestId);
                        System.out.println(">>>>>> requestId = " + requestId + ", release lock flag = " + releaseLockFlag);
                    }
                } else {
                    System.out.println("<<<<<< requestId = " + requestId + ", get lock fail, key = " + redisKey);
                    // 等待三秒
                    Thread.sleep(2000);
                }
            }
            // 剩余的客户端id
            System.out.println("requestIdList = " + requestIdList);
            // 成功获取到锁的客户端id
            System.out.println("successIdList = " + successIdList);
            System.out.println("\n\n");
            //将已经处理完的清除
            for (String s : successIdList) {
                requestIdList.remove(s);
            }
            // 清空
            successIdList.clear();
            // 休眠 2 秒，模拟 2 秒重试
            Thread.sleep(2000);

        }

    }


    /**
     * 模拟多个请求用户对多个数据加锁访问
     */
    @Test
    public void test2() throws InterruptedException {
        Jedis jedis = new Jedis("localhost");

        // 所有数据集合
        List<String> allDataList = new ArrayList<>(16);
        // 满足条件的（已处理的）集合
        List<String> successList = new ArrayList<>(16);
        // 模拟多个客户端
        List<String> requestIdList = Arrays.asList("Client_AAA", "Client_BBB", "Client_CCC");


        // 数据初始化
        for (int i = 0; i < 10; i++) {
//            allDataList.add(UUID.randomUUID().toString());
            allDataList.add("" + i + i + i);
        }

        // 模拟执行次数
        int count = 1;
        while (allDataList.size() != 0) {

            // 对 allDataList 里面所有数据做获取锁处理，并对数据为偶数的才释放锁
            for (int i = 0; i < allDataList.size(); i++) {
                System.out.println(">>>>>> execute count : " + count ++ + ", date = " + allDataList.get(i));
                // 锁key（数据id）
                String redisKey = "redisKey_" + i;
                for (String requestId : requestIdList) {
                    boolean getLockFlag = RedisUtil.tryGetDistributedLock(jedis, redisKey, requestId, 5000L);
                    // 获取到锁，处理业务逻辑
                    if (getLockFlag) {
                        System.out.println(">>>>>> requestId = " + requestId + ", deal custom logic");
                        // 处理成功的数据
                        successList.add(allDataList.get(i));
                        if (i % 2 == 0) {
                            // 对 2 取模来释放锁，一般处理完成之后直接释放锁
                            boolean releaseLockFlag = RedisUtil.releaseDistributedLock(jedis, redisKey, requestId);
                            System.out.println(">>>>>> requestId = " + requestId + ", release lock flag = " + releaseLockFlag);
                        }
                    } else {
                        System.out.println(">>>>>> requestId = " + requestId + ", get lock fail, key = " + redisKey);
                    }
                }
            }

            // ------ 循环一次数据处理后，处理情况
            System.out.println("allDataList = " + allDataList);
            System.out.println("successList = " + successList);
            //将已经处理完的清除
            for (String s : successList) {
                allDataList.remove(s);
            }
            // 清空成功的集合
            successList.clear();
            // 休眠 2 秒，模拟 2 秒重试
            Thread.sleep(2000);
        }

    }
}
