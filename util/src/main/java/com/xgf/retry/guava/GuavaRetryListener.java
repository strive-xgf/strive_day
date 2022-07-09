package com.xgf.retry.guava;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import com.xgf.common.LogUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * @author xgf
 * @create 2022-07-03 19:45
 * @description 实现 Guava RetryListener 监听器（call执行回调，处理特殊逻辑，比如：消息提醒，日志记录等等）
 **/

@Slf4j
public class GuavaRetryListener implements RetryListener {

    @Override
    public <V> void onRetry(Attempt<V> attempt) {
        LogUtil.info(">>>>>> guava retry  attemptNumber = {}, delaySinceFirstAttempt = {}", attempt.getAttemptNumber(), attempt.getDelaySinceFirstAttempt());

        if (attempt.hasException()){
            LogUtil.warn("====== guava retry exist exception = {}", attempt.getExceptionCause().toString());
        }

        if (attempt.hasResult()) {
            LogUtil.info("====== guava retry has result = {}", attempt.getResult());
        }

    }

}
