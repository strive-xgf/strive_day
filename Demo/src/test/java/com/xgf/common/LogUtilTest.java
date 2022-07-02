package com.xgf.common;

import com.xgf.DemoApplication;
import com.xgf.bean.User;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.spring.SpringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * @author xgf
 * @create 2022-07-01 11:27
 * @description
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class LogUtilTest {

    @Test
    public void testInfo() {
        try {
            throw CustomExceptionEnum.CUSTOM_PARAM_VALIDATOR_NULL_EXCEPTION.generateException();
        } catch (Exception e) {
            LogUtil.error("1", "2", e, User.builder().userUuid("uuid").age(22).build(), Arrays.asList("S1", "S2"));
        }
    }

    @Test
    public void testDeBug() {
        LogUtil.info("debug");
    }

}
