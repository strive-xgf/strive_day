package com.xgf.annotation.aspcet;

import com.xgf.annotation.aspcet.common.MethodParamParseUtil;
import com.xgf.check.CheckCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author xgf
 * @create 2022-04-17 18:35
 * @description 方法参数切面
 **/

@Slf4j
@Aspect
@Component("methodParamAspect")
public class MethodParamAspect extends MethodParamParseUtil {

    @Pointcut("@annotation(com.xgf.annotation.MethodParamAnnotation)")
    public void methodParamPoint() {
    }

    @Before(value = "methodParamPoint()")
    public void executeBefore(JoinPoint joinPoint) {
//        log.info("====== MethodParamAspect before, check param");
    }

    @After(value = "methodParamPoint()")
    public void executeAfter(JoinPoint joinPoint) {

        // 解析数据得到对应参数值【进行强转具体值，然后处理对应逻辑】
        List<?> resultList = Optional.ofNullable(parseParamValueList(joinPoint)).orElseGet(ArrayList::new)
                .stream().filter(Objects::nonNull).collect(Collectors.toList());

        if (CheckCommonUtil.checkAllFieldsIsNull(resultList)) {
            log.info("MethodParamAspect executeAfter resultList is empty");
            return;
        }

        // todo 处理特殊逻辑 【推荐以消息形式发送，逻辑处理由消息接收处处理】 【待改消息】
        // tag = methodName【joinPoint.getSignature().toLongString()】， value等于解析值parseParamValueList(joinPoint)

    }



}
