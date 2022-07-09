package com.xgf.retry.guava;

import com.github.rholder.retry.*;
import com.xgf.common.LogUtil;
import com.xgf.constant.StringConstantUtil;
import com.xgf.retry.guava.config.GuavaRetryProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author xgf
 * @create 2022-07-03 21:39
 * @description Guava Retry 切面
 **/

@Slf4j
@Aspect
@Component
@ConditionalOnProperty(prefix = "custom.config.guava.retry", name = "enabled", havingValue = "true")
public class GuavaRetryAspect {

    @Autowired
    protected GuavaRetryProperties guavaRetryProperties;

    /**
     * 基于 execution 读取配置 api
     */
    @Pointcut("execution(public * com.xgf.mvc..*.*(..))")
    public void pointCut() {
    }

    /**
     * 基于注解
     */
    @Pointcut("@annotation(com.xgf.retry.guava.GuavaRetryAnnotation)")
    public void pointCutAnnotation() {
    }

    @Pointcut("pointCut() || pointCutAnnotation()")
    public void allPointCut() {
    }


    @Around(value = "allPointCut()")
    public Object executeAround(ProceedingJoinPoint joinPoint) throws Throwable {

        Method method = getExecuteMethod(joinPoint);
        if (method == null) {
            LogUtil.infoParam("method is null");
            return null;
        }

        // 获取方法上注解信息
        GuavaRetryAnnotation annotation = method.getDeclaredAnnotation(GuavaRetryAnnotation.class);
        // 拼接uri路径
        final String uri = String.join(StringConstantUtil.DOT, method.getDeclaringClass().getName(), method.getName());

        LogUtil.infoParam(">>>>>> guava retry executeAround url", uri);

        // 没有注解，读取配置，配置拦截重试接口为空，或者当前uri接口不是拦截接口，直接执行结束
        if (annotation == null && (CollectionUtils.isEmpty(guavaRetryProperties.getApis()) || !guavaRetryProperties.getApis().contains(uri))) {
            return joinPoint.proceed();
        }

        // 有注解 重试时间/次数 配置不需要重试，直接执行结束
        if (annotation != null && annotation.duration() <= 0 && annotation.attemptNumber() <= 1) {
            return joinPoint.proceed();
        }

        // 执行回调 call
        return getRetryBuilder(annotation).call(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new Exception(throwable);
            }
        });


    }


    /**
     * 组装 Retryer 信息
     *
     * @param annotation 自定义注解 GuavaRetry
     * @return Retryer
     * @throws Throwable 异常
     */
    private Retryer<Object> getRetryBuilder(GuavaRetryAnnotation annotation) throws Throwable {

        if (annotation != null) {
            return getRetryBuilderByAnnotation(annotation);
        }

        // 不使用注解，读配置
        return getRetryBuilderByConfig();
    }

    /**
     * 通过配置值组装 Retryer
     *
     * @return Retryer
     * @throws NoSuchMethodException 异常
     */
    private Retryer<Object> getRetryBuilderByConfig() throws Throwable {

        RetryerBuilder<Object> builder = RetryerBuilder.newBuilder();
        // 指定异常继续重试
        builder.retryIfExceptionOfType(Exception.class);
        // 设置停止策略【StopAfterAttemptStrategy】（重试n次停止）【防止出现一直重试】
        builder.withStopStrategy(StopStrategies.stopAfterAttempt(guavaRetryProperties.getAttemptNumber()));
        // 等待策略（固定时长等待策略，每多长时间等待）
        builder.withWaitStrategy(WaitStrategies.fixedWait(guavaRetryProperties.getWaitStrategySleepTime(), TimeUnit.MILLISECONDS));
        // 任务执行时长限制，固定时长限制策略【单次任务执行超时，则终止执行当前重试任务】
        builder.withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(guavaRetryProperties.getLimitDuration(), TimeUnit.MILLISECONDS));
        // 自定义阻塞策略：自旋锁实现
//        builder.withBlockStrategy(new GuavaBlockStrategy());
        // 注册监听（可多个）call执行重试时，都会回调监听，处理特殊逻辑
        builder.withRetryListener(guavaRetryProperties.getRetryListenerClass().getDeclaredConstructor().newInstance());
        return builder.build();
    }

    /**
     * 通过注解值组装 Retryer
     *
     * @return Retryer
     * @throws NoSuchMethodException 异常
     */
    private Retryer<Object> getRetryBuilderByAnnotation(GuavaRetryAnnotation annotation) throws Throwable {

        RetryerBuilder<Object> builder = RetryerBuilder.newBuilder();

        if (annotation.attemptNumber() > 0) {
            // 重试次数（指定重试次数）
            builder.withStopStrategy(StopStrategies.stopAfterAttempt(annotation.attemptNumber()));
        } else if (annotation.duration() > 0) {
            // 退出策略（指定时长退出）
            builder.withStopStrategy(StopStrategies.stopAfterDelay(annotation.duration(), TimeUnit.MILLISECONDS));
        }

        // 重试间间隔等待策略（固定时长等待策略）
        if (annotation.waitStrategySleepTime() > 0) {
            builder.withWaitStrategy(WaitStrategies.fixedWait(annotation.waitStrategySleepTime(), TimeUnit.MILLISECONDS));
        }

        if (annotation.exceptionClass().length > 0) {
            // 停止重试策略（满足指定异常重试）
            for (Class retryThrowable : annotation.exceptionClass()) {
                if (retryThrowable != null && Throwable.class.isAssignableFrom(retryThrowable)) {
                    builder.retryIfExceptionOfType(retryThrowable);
                }
            }
        }

        if (annotation.limitDuration() > 0) {
            // 任务执行时长限制，固定时长限制策略 10s【单次任务执行超时，则终止执行当前重试任务】
            builder.withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(annotation.limitDuration(), TimeUnit.SECONDS));
        }

        // 注册监听（可多个）call执行重试时，都会回调监听，处理特殊逻辑
        builder.withRetryListener(annotation.retryListenerClass().getDeclaredConstructor().newInstance());
        return builder.build();
    }


    /**
     * 获取执行方法
     *
     * @param joinPoint ProceedingJoinPoint
     * @return Method
     */
    private Method getExecuteMethod(ProceedingJoinPoint joinPoint) {

        // 当前只处理方法重试
        if (!(joinPoint.getSignature() instanceof MethodSignature)) {
            LogUtil.infoParam("param no MethodSignature");
            return null;
        }

        return ((MethodSignature) joinPoint.getSignature()).getMethod();
    }

}
