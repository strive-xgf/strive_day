package com.xgf.java8;

import com.xgf.bean.Hobby;
import com.xgf.bean.User;
import com.xgf.common.JsonUtil;
import com.xgf.common.LogUtil;
import org.junit.Test;

import java.util.Date;

/**
 * @author xgf
 * @create 2022-07-29 18:26
 * @description
 **/

public class BooleanFunctionUtilTest {

    /**
     * mock Supplier 供给型方法（无参数，有返回结果）
     * @return
     */
    private User mockSupplierMethod() {
        return User.builder().userUuid("userUuid").hobby(Hobby.builder().hobbyType("type").createdTime(new Date()).build()).build();
    }

    /**
     * mock Runnable（无参数，无返回结果）
     */
    private void mockRunnableMethod() {
        System.out.println(">>>>>> execute mockRunnableMethod");
    }


    @Test
    public void testSupplier() {
        System.out.println("trueSupplier > " + BooleanFunctionUtil.trueSupplier(true).get(() -> 1));
        System.out.println("trueSupplier > " + BooleanFunctionUtil.trueSupplier(true).get(this::mockSupplierMethod));
        System.out.println("falseSupplier > " + BooleanFunctionUtil.falseSupplier(false).get(() -> 1));
        System.out.println("nullSupplier > " + BooleanFunctionUtil.nullSupplier(null).get(() -> 1));
        System.out.println("trueSupplierDefault true > " + JsonUtil.toJsonString(
                BooleanFunctionUtil.trueSupplierDefault(true, User.builder().userUuid("uuid1").build()).get(this::mockSupplierMethod)));
        System.out.println("trueSupplierDefault null > " + JsonUtil.toJsonString(
                BooleanFunctionUtil.trueSupplierDefault(null, User.builder().userUuid("uuid2").build()).get(this::mockSupplierMethod)));
        System.out.println("falseSupplierDefault false > " + JsonUtil.toJsonString(
                BooleanFunctionUtil.falseSupplierDefault(false, User.builder().userUuid("uuid1").build()).get(this::mockSupplierMethod)));
        System.out.println("falseSupplierDefault null > " + JsonUtil.toJsonString(
                BooleanFunctionUtil.nullSupplierDefault(null, User.builder().userUuid("uuid1").build()).get(this::mockSupplierMethod)));

    }

    @Test
    public void testRunnable() {
        System.out.println("\n====== trueRunnable ======");
        System.out.println("trueRunnable true > ");
        BooleanFunctionUtil.trueRunnable(true).run(() -> System.out.println("123"));
        System.out.println("trueRunnable true > ");
        BooleanFunctionUtil.trueRunnable(true).run(this::mockRunnableMethod);
        System.out.println("trueRunnable true > ");
        BooleanFunctionUtil.trueRunnable(true).run(() -> LogUtil.info("get redis lock, key = {}, requestId = {}, expireTime = {}", "lockKey", "requestId", "expireTime"));
        System.out.println("trueRunnable false > ");
        BooleanFunctionUtil.trueRunnable(false).run(this::mockRunnableMethod);
        System.out.println("trueRunnable null > ");
        BooleanFunctionUtil.trueRunnable(null).run(this::mockRunnableMethod);

        System.out.println("\n====== falseRunnable ======");
        System.out.println("trueRunnable false > ");
        BooleanFunctionUtil.falseRunnable(false).run(this::mockRunnableMethod);
        System.out.println("trueRunnable true > ");
        BooleanFunctionUtil.falseRunnable(true).run(this::mockRunnableMethod);
        System.out.println("trueRunnable null > ");
        BooleanFunctionUtil.falseRunnable(null).run(this::mockRunnableMethod);


        System.out.println("\n====== nullRunnable ======");
        System.out.println("trueRunnable null > ");
        BooleanFunctionUtil.nullRunnable(null).run(this::mockRunnableMethod);
        System.out.println("trueRunnable false > ");
        BooleanFunctionUtil.nullRunnable(false).run(this::mockRunnableMethod);
        System.out.println("trueRunnable true > ");
        BooleanFunctionUtil.nullRunnable(true).run(this::mockRunnableMethod);
    }

}
