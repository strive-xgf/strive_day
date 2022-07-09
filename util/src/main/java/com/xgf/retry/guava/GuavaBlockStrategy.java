package com.xgf.retry.guava;

import com.github.rholder.retry.BlockStrategy;
import com.xgf.common.LogUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xgf
 * @create 2022-07-07 16:07
 * @description BlockStrategies 阻塞策略，自旋锁的实现, 不响应线程中断
 **/

@Slf4j
public class GuavaBlockStrategy implements BlockStrategy {

    @Override
    public void block(long sleepTime) throws InterruptedException {

        long start = System.currentTimeMillis();
        long end = start;

        while (end - start <= sleepTime) {
            end = System.currentTimeMillis();
        }

        LogUtil.info("block end", start, end, sleepTime);
    }
}