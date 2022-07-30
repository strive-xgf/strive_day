package com.xgf.java8;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author xgf
 * @create 2022-07-26 14:20
 * @description
 **/

public class BranchHandleUtilTest {

    @Test
    public void testIsTrueOrFalse() {
        List<Boolean> booleanList = Arrays.asList(Boolean.TRUE, Boolean.FALSE, null);
        booleanList.forEach(p -> {
            System.out.print(p + " : ");
            BranchHandleUtil.isTrueOrFalse(p).handle(
                    () -> System.out.println("为 true 执行"),
                    () -> System.out.println(" 为 false / null 执行")
            );
        });
    }

}
