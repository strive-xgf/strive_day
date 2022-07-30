package com.xgf.java8;



import java.util.function.Supplier;

/**
 * @author xgf
 * @create 2022-07-26 00:12
 * @description Boolean函数式工具（满足条件执行
 **/

public class BooleanFunctionUtil {

    /**
     * Boolean 供给型函数式接口：满足条件，执行函数（无参）返回结果
     **/
    @FunctionalInterface
    public interface BooleanSupplierFunction<T> {

        /**
         * 执行方法接口（Supplier方法无入参，有返回结果）
         *
         * @param supplier Supplier 接口
         * @return T
         **/
        T get(Supplier<T> supplier);
    }

    /**
     * 为 true 执行，并返回结果
     *
     * @param flag Boolean 值
     * @return true：执行并返回
     */
    public static <T> BooleanSupplierFunction<T> trueSupplier(Boolean flag) {
        return supplier -> {
            if (flag != null && flag) {
                return supplier.get();
            }
            return null;
        };
    }

    public static <T> BooleanSupplierFunction<T> falseSupplier(Boolean flag) {
        return supplier -> {
            if (flag != null && !flag) {
                return supplier.get();
            }
            return null;
        };
    }

    public static <T> BooleanSupplierFunction<T> nullSupplier(Boolean flag) {
        return supplier -> {
            if (flag == null) {
                return supplier.get();
            }
            return null;
        };
    }

    /**
     * 为 true 执行方法，否则返回默认值 t
     * @param flag Boolean 值
     * @param t 默认值
     * @param <T> 泛型 T
     * @return true: 执行方法，返回结果，false: 返回 t
     */
    public static <T> BooleanSupplierFunction<T> trueSupplierDefault(Boolean flag, T t) {
        return supplier -> {
            if (flag != null && flag) {
                return supplier.get();
            }
            return t;
        };
    }

    public static <T> BooleanSupplierFunction<T> falseSupplierDefault(Boolean flag, T t) {
        return supplier -> {
            if (flag != null && !flag) {
                return supplier.get();
            }
            return t;
        };
    }

    public static <T> BooleanSupplierFunction<T> nullSupplierDefault(Boolean flag, T t) {
        return supplier -> {
            if (flag == null) {
                return supplier.get();
            }
            return t;
        };
    }









    /**
     * Boolean Runnable 无参无返回值，判断Boolean值执行结果
     **/
    @FunctionalInterface
    public interface BooleanRunnableFunction {

        /**
         * 执行方法接口（Runnable方法无入参，有返回结果）
         *
         * @param runnable Runnable 接口
         **/
        void run(Runnable runnable);
    }

    /**
     * 为 true 执行
     *
     * @param flag Boolean 值
     */
    public static  BooleanRunnableFunction trueRunnable(Boolean flag) {
        return (runnable) -> {
            if (flag != null && flag) {
                runnable.run();
            }
        };
    }

    public static  BooleanRunnableFunction falseRunnable(Boolean flag) {
        return runnable -> {
            if (flag != null && !flag) {
                runnable.run();
            }
        };
    }

    public static  BooleanRunnableFunction nullRunnable(Boolean flag) {
        return runnable -> {
            if (flag == null) {
                runnable.run();
            }
        };
    }

}
