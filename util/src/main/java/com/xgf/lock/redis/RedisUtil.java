package com.xgf.lock.redis;

import com.xgf.common.LogUtil;
import com.xgf.java8.BooleanFunctionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * @author xgf
 * @create 2022-07-25 00:15
 * @description Redis工具类（分布式锁）
 **/

@Component
public class RedisUtil {

    /**
     * 是否需要答应加锁日志，默认false，不写日志
     */
    private static Boolean LOCK_LOG_FLAG;

    /**
     * 给静态变量 LOCK_LOG_FLAG 读取赋值，默认为
     */
    @Value("${custom.config.log.lockLogFlag:false}")
    public void setStaticLockLogFlag(Boolean lockLogFlag) {
        RedisUtil.LOCK_LOG_FLAG = lockLogFlag;
    }

    /**
     * 加锁成功
     */
    private static final String LOCK_SUCCESS = "OK";

    /**
     * 获取锁方式：SET_IF_NOT_EXIST，当key不存在时，进行set操作，若key存在，不做任何操作
     */
    private static final String SET_IF_NOT_EXIST = "NX";

    /**
     * 设置锁过期时间，SET_WITH_EXPIRE_TIME
     */
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 释放锁成功
     */
    private static final Long RELEASE_LOCK_SUCCESS = 1L;

    /**
     * 尝试获取分布式锁，不存在则加锁，存在则返回
     *
     * @param jedis Redis客户端
     * @param lockKey 锁标识
     * @param requestId 请求（客户端）标识（加锁解锁是同一客户端）
     * @param expireTime 超期时间
     * @return true: 获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, Long expireTime) {
        // set方法 加锁（共有五个形参） public String set(String key, String value, String nxxx, String expx, int time)
        // 参1：key，使用key当锁（唯一）
        // 参2：value，这里传的是requestId，为了满足可靠性（加锁和解锁必须是同一客户端），通过value知道这个锁是什么请求加的，作为解锁的依据requestId可以使用UUID.randomUUID().toString()方法生成
        // 参3：nxxx，设置为SET_IF_NOT_EXIST，当key不存在时，进行set操作，若key存在，不做任何操作
        // 参4：expx，传SET_WITH_EXPIRE_TIME，意思是我们要给这个key加一个过期的设置，具体时间由第五个参数决定。
        // 参5：time，与第四个参数相呼应，key的过期时间

        // 执行 set() 方法就只会导致两种结果：
        // 1. 当前没有锁（key不存在），那么就进行加锁操作，并对锁设置个有效期，同时value表示加锁的客户端。
        // 2. 已有锁存在，不做任何操作
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            BooleanFunctionUtil.trueRunnable(LOCK_LOG_FLAG).run(() ->
                    LogUtil.info("get redis lock, key = {}, requestId = {}, expireTime = {}", lockKey, requestId, expireTime));
            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param jedis Redis客户端
     * @param lockKey 锁标识
     * @param requestId 请求（客户端）标识
     * @return true: 释放锁成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {

        // 需要执行的脚本 (lua脚本代码）
        // 首先获取锁对应的value值，检查是否与requestId相等，如果相等则删除锁（解锁）
        // 用Lua语言实现：确保上述操作是原子性的
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        // eval(String) 函数可计算某个字符串，并执行其中的的 JavaScript 代码
        // 该方法只接受原始字符串作为参数，如果 string 参数不是原始字符串，那么该方法将不作任何改变地返回。因此请不要为 eval() 函数传递 String 对象来作为参数。
        // 如果参数中没有合法的表达式和语句，则抛出 SyntaxError 异常。
        // 如果非法调用 eval()，则抛出 EvalError 异常。
        // 如果传递给 eval() 的 Javascript 代码生成了一个异常，eval() 将把该异常传递给调用者。

        // 将Lua代码传到jedis.eval()方法里，并使参数KEYS[1]赋值为lockKey，ARGV[1]赋值为requestId。eval()方法是将Lua代码交给Redis服务端执行。
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_LOCK_SUCCESS.equals(result)) {
            BooleanFunctionUtil.trueRunnable(LOCK_LOG_FLAG).run(() ->
                    LogUtil.info("release redis lock, key = {}, requestId = {}", lockKey, requestId));
            return true;
        }
        return false;
    }

    /**
     * 获取锁失败重试机制机制
     *
     * @param jedis Redis客户端
     * @param lockKey 锁标识
     * @param requestId 请求（客户端）标识
     * @param retryCount 重试次数
     * @param sleepTime 重试时间间隔（单位：毫秒）
     * @param expireTime 重新获取锁设置锁的过期时间（单位：毫秒）
     *
     * @return true: 重试获取到锁
     */
    public static Boolean retryGetDistributedLock(Jedis jedis, String lockKey, String requestId, Integer retryCount, Long sleepTime, Long expireTime) {
        try {
            for (int i = 0; i < retryCount; i++) {
                int count = i;
                BooleanFunctionUtil.trueRunnable(LOCK_LOG_FLAG).run(() ->
                        LogUtil.info("retry lock count = {}, retryCount = {}, key = {}, requestId = {}, time = {}", count, retryCount, lockKey, requestId,
                                System.currentTimeMillis()));
                boolean lockFlag = tryGetDistributedLock(jedis, lockKey, requestId, expireTime);
                if (lockFlag) {
                    //加锁成功返回
                    return true;
                }
                Thread.sleep(sleepTime);
            }
        } catch (Exception e) {
            BooleanFunctionUtil.trueRunnable(LOCK_LOG_FLAG).run(() ->LogUtil.warn("retry get lock exception message = {}", e));
        }
        return false;
    }


}
