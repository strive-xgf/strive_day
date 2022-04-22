package com.xgf.util.annotation;

import com.alibaba.fastjson.JSON;
import com.xgf.annotation.MethodParamAnnotation;
import com.xgf.bean.Hobby;
import com.xgf.bean.User;
import com.xgf.bean.WorkInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2022-04-18 01:45
 * @description 测试 com.xgf.annotation.MethodParamAnnotation 注解方法【注入形式 aop 才生效】
 **/

@Slf4j
@Service("methodParamAnnotationService")
public class MethodParamAnnotationService {

//    @MethodParamAnnotation(methodParam = "user")
//    @MethodParamAnnotation(methodParam = "#{user.hobby}")
//    @MethodParamAnnotation(methodParam = "#{user.hobby}", paramType = Hobby.class)
//    @MethodParamAnnotation(methodParam = "#{user.stringList}", paramType = String.class)
//    @MethodParamAnnotation(methodParam = "#{user.workInfoList}", paramType = WorkInfo.class)
    /**
     * 下面两个区别：
     * 1. 不传 paramType 类型，默认按String 转换，转换结果：【["{\"workContent\":\"workContent1\",\"workUuid\":\"workUuid1\"}","{\"workContent\":\"workContent2\",\"workUuid\":\"workUuid2\"}"]】
     * 2. 传  paramType 类型，按 paramType 转换，转换结果：【[{"workContent":"workContent1","workUuid":"workUuid1"},{"workContent":"workContent2","workUuid":"workUuid2"}]】
     * 第一种是String类型，需要接收端按JSON.Parse再解析成对应的对象，第二种直接解析成对应对象了
     * 但是第二种不满足的会解析成空对象，可以在消息推送端，拦截{@link com.xgf.annotation.aspcet.MethodParamAspect#executeAfter}
     * 两种区别可参考 {@link com.xgf.util.annotation.MethodParamAnnotationService#testParamTypeDiff()} 方法
     */
//    @MethodParamAnnotation(methodParam = "#{user.workInfoList}")
    @MethodParamAnnotation(methodParam = "#{user.workInfoList}", paramType = WorkInfo.class)
//    @MethodParamAnnotation(methodParam = "#{user.workInfoList}", paramType = User.class)
    public void saveOneUser(User user) {
        System.out.println(">>>>>> MethodParamAnnotationTest saveOneUser execute");
    }

//    @Test
    public void testParamTypeDiff() {
        User user = User.builder()
                .userUuid("userUuid")
                .passWord("123")
                .age(18)
                .hobby(Hobby.builder().hobbyType("hobbyType").hobbyCount(12).createdTime(new Date()).hobbyUuid("hobbyUuid").build())
                .createdTime(new Date())
                .bigDecimal(new BigDecimal("9999"))
                .stringList(Arrays.asList("1", "2", "3", "4", "5", "6"))
                .workInfoList(Arrays.asList(WorkInfo.builder().workUuid("workUuid1").workContent("workContent1").build(),
                        WorkInfo.builder().workUuid("workUuid2").workContent("workContent2").build()))
                .build();

        String value = JSON.toJSONString(user.getWorkInfoList());

        List<?> paramValueList;
        // 第一种（只是将String(JSON字符串），按Json对象转换成数组 {} 一个对象对应一个 String）
        paramValueList = JSON.parseArray(value, String.class);
        log.info("====== paramType default String, paramValueList = 【{}】", JSON.toJSONString(paramValueList));

        // 第二种 （将String(JSON字符串） 按具体目标对象格式转换）
        paramValueList = JSON.parseArray(value, WorkInfo.class);
        log.info("====== paramType = WorkInfo.class, paramValueList = 【{}】", JSON.toJSONString(paramValueList));


        // 消息接收端
        List<WorkInfo> resultList;

        // 第一种处理
        resultList = paramValueList.stream().map(p -> (WorkInfo)p).collect(Collectors.toList());
        log.info("====== paramType default String, resultList = 【{}】", JSON.toJSONString(resultList));

        // 强转类型不匹配会报错类转换异常（ClassCastException）
        List<User> collect = paramValueList.stream().map(p -> (User) p).collect(Collectors.toList());


        // 第二种处理
        resultList = paramValueList.stream().map(p -> (WorkInfo)p).collect(Collectors.toList());
        log.info("====== paramType = WorkInfo.class, resultList = 【{}】", JSON.toJSONString(resultList));

    }

//    @MethodParamAnnotation(methodParam = "#{userList}")
    @MethodParamAnnotation(methodParam = "#{userList}", paramType = User.class)
    public void batchSaveUser(List<User> userList) {
        System.out.println(">>>>>> MethodParamAnnotationTest batchSave execute");
    }

    // todo  待做：支持多参数转换 eg: @MethodParamAnnotation(methodParam = "#{user.hobby.hobbyType, user.hobby.workInfoList}")
}
