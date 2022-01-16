package com.xgf.util;

import com.alibaba.fastjson.JSON;
import com.xgf.DemoApplication;
import com.xgf.designpattern.structure.filter.Person;
import com.xgf.exception.CustomException;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.task.TaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

/**
 * @author xgf
 * @create 2021-12-23 15:37
 * @description 异步工具测试
 **/


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class TaskUtilTest {

    @Test
    public void executeAsyncTest() {
        try {
            // 捕获不到异常
//            test2();
            // 能捕获到异常
//            test3();
            // 能捕获到异常
//            test4();
            // 能捕获到异常
            test5();
        } catch (CustomException e) {
            log.info("+++ ====== executeAsyncTest catch exception, code = 【{}】, message = 【{}】 ", e.getErrorCode(), e.getMessage(), e);
        }

        System.out.println("====== 执行结束继续执行 >>>>>> ");
    }

    private String throwException(Boolean flag){
        if(BooleanUtils.isTrue(flag)){
            throw CustomExceptionEnum.ENUM_TYPE_ILLEGAL_EXCEPTION.generateException();
        }
        return "hello";
    }



    @Test
    public void test1(){
        long startTime = System.currentTimeMillis();

        boolean flag = true;
        CompletableFuture<Void> wait1 = CompletableFuture.runAsync(() -> wait1(flag));
        CompletableFuture<Void> wait2 = CompletableFuture.runAsync(() -> wait2(flag));
        CompletableFuture<Void> wait3 = CompletableFuture.runAsync(() -> wait3(flag));
        CompletableFuture<Void> wait4 = CompletableFuture.runAsync(() -> wait4(flag));
        CompletableFuture<Person> wait5 = CompletableFuture.supplyAsync(() -> wait5(flag));
        CompletableFuture.allOf(wait1, wait2, wait3, wait4, wait5).join();

        log.info("====== test1 total cost = {} ms", System.currentTimeMillis() - startTime);

    }

    @Test
    public void test2() {
        long startTime = System.currentTimeMillis();

        boolean flag = true;
        CompletableFuture<Void> wait1 = TaskUtil.runAsync(() -> wait1(flag));
        CompletableFuture<Void> wait2 = TaskUtil.runAsync(() -> wait2(flag));
        CompletableFuture<Void> wait3 = TaskUtil.runAsync(() -> wait3(flag));
        CompletableFuture<Void> wait4 = TaskUtil.runAsync(() -> wait4(flag));
        CompletableFuture<Person> wait5 = TaskUtil.supplyAsync(() -> wait5(flag));

        try {
            CompletableFuture.allOf(wait1, wait2, wait3, wait4, wait5).join();
            Person person = wait5.get();
            log.info("====== test2 person = {}", JSON.toJSONString(person));

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }finally {
            log.info("====== test2 total cost = {} ms", System.currentTimeMillis() - startTime);
        }


    }

    /**
     * 自定义异常捕获处理
     */
    @Test
    public void test3() {
        long startTime = System.currentTimeMillis();

        boolean flag = true;
        CompletableFuture<Void> wait1 = TaskUtil.runAsync(() -> wait1(flag));
        CompletableFuture<Void> wait2 = TaskUtil.runAsync(() -> wait2(flag));
        CompletableFuture<Void> wait3 = TaskUtil.runAsync(() -> wait3(flag));
        CompletableFuture<Void> wait4 = TaskUtil.runAsync(() -> wait4(flag));
        CompletableFuture<Person> wait5 = TaskUtil.supplyAsync(() -> wait5(flag));


        try {
            CompletableFuture.allOf(wait1, wait2, wait3, wait4, wait5).join();
            Person person = wait5.get();
            log.info("====== test2 person = {}", JSON.toJSONString(person));
            // 抛出的自定义异常通过 CompletionException 捕获
        } catch (InterruptedException | ExecutionException | CompletionException e) {
            // 捕获并转换异常 ***
            log.warn("====== test2 exception message = {} ", e.getMessage());
            if(e.getCause() instanceof RuntimeException){
                throw (RuntimeException)e.getCause();
            }
            throw new RuntimeException(e);
        } finally {
            log.info("====== test2 total cost = {} ms", System.currentTimeMillis() - startTime);
        }

    }


    @Test
    public void test4() {
        long startTime = System.currentTimeMillis();

        boolean flag = true;
        CompletableFuture<Void> wait1 = TaskUtil.runAsync(() -> wait1(flag));
        CompletableFuture<Void> wait2 = TaskUtil.runAsync(() -> wait2(flag));
        CompletableFuture<Void> wait3 = TaskUtil.runAsync(() -> wait3(flag));
        CompletableFuture<Void> wait4 = TaskUtil.runAsync(() -> wait4(flag));
        CompletableFuture<Person> wait5 = TaskUtil.supplyAsync(() -> wait5(flag));

        try {
            TaskUtil.waitAllThrow(wait1, wait2, wait3, wait4, wait5);
        } finally {
            log.info("====== test4 total cost = {} ms", System.currentTimeMillis() - startTime);
        }

    }

    @Test
    public void test5() {
        long startTime = System.currentTimeMillis();

        boolean flag = true;
        CompletableFuture<Void> wait1 = TaskUtil.runAsync(() -> wait1(flag));
        CompletableFuture<Void> wait2 = TaskUtil.runAsync(() -> wait2(flag));
        CompletableFuture<Void> wait3 = TaskUtil.runAsync(() -> wait3(flag));
        CompletableFuture<Void> wait4 = TaskUtil.runAsync(() -> wait4(flag));
        CompletableFuture<Person> wait5 = TaskUtil.supplyAsync(() -> wait5(flag));

        try {
            TaskUtil.waitFirstException(wait3, wait2, wait4, wait5, wait1);
//            TaskUtil.waitAnyException(wait2, wait3, wait4, wait5, wait1);
        } finally {
            log.info("====== test5 total cost = {} ms", System.currentTimeMillis() - startTime);
        }

    }





    private String wait1(boolean flag){
        try {
            log.info("====== wait1 start");
            Thread.sleep(500);
            log.info("====== wait1 end");
            if (flag) {
                throw new CustomException("wait1", "wait111");
            }
            return "wait1";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "wait1 exception";
        }
    }

    private String wait2(boolean flag){
        try {
            log.info("====== wait2 start");
            Thread.sleep(600);
            log.info("====== wait2 end");
            if (flag) {
                throw new CustomException("wait2", "wait222");
            }
            return "wait2";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "wait2 exception";
        }
    }

    private String wait3(boolean flag){
        try {
            log.info("====== wait3 start");
            Thread.sleep(600);
            log.info("====== wait3 end");
            if (flag) {
                throw new CustomException("wait3", "wait3333");
            }
            return "wait3";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "wait3 exception";
        }
    }

    private String wait4(boolean flag){
        try {
            log.info("====== wait4 start");
            Thread.sleep(700);
            log.info("====== wait4 end");
            if (flag) {
                throw new CustomException("wait4", "wai444");
            }
            return "wait4";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "wait4 exception";
        }
    }


    private Person wait5(boolean flag){
        try {
            log.info("====== wait5 start");
            Thread.sleep(700);
            log.info("====== wait5 end");
            if (flag) {
                throw new CustomException("wait5", "wait555");
            }
            Person person1 = new Person();
            person1.setName("wait555");
            return person1;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Person person2 = new Person();
            person2.setName("wait5 exception");
            return person2;
        }
    }

}
