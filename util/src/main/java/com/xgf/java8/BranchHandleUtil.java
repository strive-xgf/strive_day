package com.xgf.java8;


/**
 * @author xgf
 * @create 2022-07-26 14:05
 * @description 分支处理函数式工具
 **/

public class BranchHandleUtil {

    /**
     * 函数式接口：if else 分支处理，无返回值
     **/
    @FunctionalInterface
    public interface BranchHandle {

        /**
         * 处理分支操作，true 执行 trueHandle，false 执行 falseHandle
         *
         * @param trueHandle 为 true 时要进行的操作
         * @param falseHandle 为 false/null 时要进行的操作
         **/
        void handle(Runnable trueHandle, Runnable falseHandle);
    }

    /**
     * 参数为true或false时，分别进行不同的操作
     *
     * @param flag Boolean 值
     * @return BranchHandle
     **/
    public static BranchHandle isTrueOrFalse(Boolean flag){
        return (trueHandle, falseHandle) -> {
            if (flag != null && flag){
                trueHandle.run();
            } else {
                falseHandle.run();
            }
        };
    }

}
