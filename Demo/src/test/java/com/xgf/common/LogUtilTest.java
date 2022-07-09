package com.xgf.common;

import com.xgf.DemoApplication;
import com.xgf.bean.User;
import com.xgf.exception.CustomExceptionEnum;
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
    public void testInfoParam() {
        try {
            throw CustomExceptionEnum.CUSTOM_PARAM_VALIDATOR_NULL_EXCEPTION.generateException();
        } catch (Exception e) {
            LogUtil.errorParam("1", "2", e, User.builder().userUuid("uuid").age(22).build(), Arrays.asList("S1", "S2"));
        }
    }

    @Test
    public void testDeBugParam() {
        LogUtil.infoParam("debug");
    }

    @Test
    public void testInfo() {
        try {
            throw CustomExceptionEnum.CUSTOM_PARAM_VALIDATOR_NULL_EXCEPTION.generateException();
        }catch (Exception e) {
            LogUtil.info("info execute method = {} param is null, time = {}",
                    "info testInfo", System.currentTimeMillis(), User.builder().userUuid("uuid").age(22).build(), Arrays.asList("S1", "S2"));
            LogUtil.info("execute method = {} param is null, time = {}, user = {}, stringList = {}",
                    "testInfo", System.currentTimeMillis(), User.builder().userUuid("uuid").age(22).build(), Arrays.asList("S1", "S2"), e);
            LogUtil.warn("warn execute method = {} param is null, time = {}, user = {}, stringList = {}",
                    "testInfo", System.currentTimeMillis(), User.builder().userUuid("uuid").age(22).build(), Arrays.asList("S1", "S2"), e);
            LogUtil.error("error execute method = {} param is null, time = {}, user = {}, stringList = {}, e = {}",
                    "testInfo", System.currentTimeMillis(), User.builder().userUuid("uuid").age(22).build(), e);
        }

    }

}
