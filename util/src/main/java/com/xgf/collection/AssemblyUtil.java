package com.xgf.collection;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2022-01-17 10:07
 * @description 通用 集合 工具处理
 **/

public class AssemblyUtil {

    /**
     * 根据条件给集合去重
     * @param distinctConditionList 条件List
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByCondition(Function<? super T, ?>... distinctConditionList) {
        return distinctByCondition(Arrays.stream(distinctConditionList).distinct().collect(Collectors.toList()));
    }

    /**
     * 根据条件给集合去重
     * @param distinctConditionList 条件List
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByCondition(List<Function<? super T, ?>> distinctConditionList) {
        Map<List<?>, Boolean> result = new ConcurrentHashMap<>();
        return p -> result.putIfAbsent(
                distinctConditionList.stream().map(e -> e.apply(p)).collect(Collectors.toList()),
                Boolean.TRUE) == null;
    }




}
