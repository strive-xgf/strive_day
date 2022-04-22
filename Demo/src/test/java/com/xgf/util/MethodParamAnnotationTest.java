package com.xgf.util;

import com.xgf.DemoApplication;
import com.xgf.bean.Hobby;
import com.xgf.bean.User;
import com.xgf.bean.WorkInfo;
import com.xgf.designpattern.structure.proxy.dynamicproxy.cglib.custom.CaseMethodService;
import com.xgf.util.annotation.MethodParamAnnotationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * @author xgf
 * @create 2022-04-17 23:11
 * @description
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class MethodParamAnnotationTest {

    @Autowired
    private CaseMethodService caseMethodService;

    @Autowired
    private MethodParamAnnotationService methodParamAnnotationService;

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

    @Test
    public void testSaveOneUser() {

//        methodParamAnnotationService.saveOneUser(User.builder()
//                .userUuid("userUuid222")
//                .phoneNum(",")
//                .passWord("222")
//                .age(18).build());

        methodParamAnnotationService.saveOneUser(user);
    }

    @Test
    public void testBatchSaveUser() {
        methodParamAnnotationService.batchSaveUser(Arrays.asList(user, User.builder()
                .userUuid("userUuid222")
                .passWord(",")
                .passWord("222")
                .age(18).build()));
    }

}
