package com.xgf.retry;

import com.xgf.DemoApplication;
import com.xgf.mvc.controller.GuavaRetryDemoController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xgf
 * @create 2022-07-03 21:44
 * @description
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class GuavaRetryTest {

    @Autowired
    private GuavaRetryDemoController guavaRetryDemoController;

    /**
     * 基于配置
     */
    @Test
    public void testGuavaRetryBaseConfig() {
        guavaRetryDemoController.testGuavaRetryBaseConfig();
    }

    @Test
    public void testGuavaRetryBaseAnnotation() {
        guavaRetryDemoController.testGuavaRetryBaseAnnotation();
    }

}
