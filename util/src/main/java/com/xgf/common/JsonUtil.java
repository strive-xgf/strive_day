package com.xgf.common;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-05-14 14:42
 * @description json工具类
 * 现在封装的是 alibaba 的 fastjson
 **/


public class JsonUtil {

    private static final Logger log= LoggerFactory.getLogger(JsonUtil.class);

    public static String toJsonString(Object obj){
        return JSON.toJSONString(obj);
    }

    public static String toJsonString(Object object, SerializeFilter filter, SerializerFeature... features){
        return JSON.toJSONString(object, filter, features);
    }

    /**
     * json字符串 转 指定类型T对象
     *
     * @param json json 字符串
     * @param clz 转换对象
     * @param <T> 对象泛型类型
     * @return T 的实例对象，转换异常返回null
     */
    public static <T> T toObject(String json, Class<T> clz) {
        try {
            return JSON.parseObject(json, clz);
        }catch (JSONException e){
            log.warn("====== JsonUtil toObject can't parse {} to {}, message = {}", json, clz, e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * json字符串 转 数组T
     *
     * @param json json 字符串
     * @param clz 转换对象
     * @param <T> 对象泛型类型
     * @return T 的实例数组，转换异常返回空数组
     */
    public static <T> List<T> toArray(String json, Class<T> clz) {
        try {
            return JSON.parseArray(json, clz);
        } catch (Exception e) {
            log.warn("====== JsonUtil toArray can't parse {} to {}, message = {}", json, clz, e.getLocalizedMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * json字符串 转 数组T 【对象/数组的json字符串兼容转为数组】
     *
     * @param json json 字符串
     * @param clz 转换对象
     * @param <T> 对象泛型类型
     * @return T 的实例数组，转换异常返回空数组
     */
    public static <T> List<T> toArrayIncludeObj(String json, Class<T> clz) {
        List<T> resultList = new ArrayList<>();
        // 防止JSON转换报错
        try {
            // 将JSON格式字符串转换为js对象（属性名没有双引号） 如果 json 数据非标注，则报错
            Object parse = JSON.parse(json);
            if(Objects.isNull(parse)){
                log.warn("====== JsonUtil toArrayIncludeObj parse result is null, json = {}", json);
                return resultList;
            }else if(parse instanceof JSONObject){
                resultList.add(JsonUtil.toObject(json, clz));
            }else if(parse instanceof JSONArray){
                resultList = JsonUtil.toArray(json, clz);
            }else{
                log.warn("====== JsonUtil toArrayIncludeObj json data format error, json = {}", json);
            }
            return resultList;
        } catch (Exception e) {
            log.error("====== JsonUtil toArrayIncludeObj exception, json ==> {}， exception message ==> {}", json, e.getMessage(), e);
            return resultList;
        }
    }

    public static <T> Map<String, Object> toMap(String json) {
        try {
            return JSON.parseObject(json, Map.class);
        } catch (Exception e) {
            log.error("====== JsonUtil toMap exception, json ==> {}， exception message ==> {}", json, e.getMessage(), e);
        }
        return null;
    }


    /**
     * json字符串 转 指定类型T对象
     * @param json json 字符串
     * @param typeReferenceValue TypeReference值
     * @return T 实例对象，转换异常返回null
     */
    @Deprecated
    public static <T> T toObject(String json, TypeReference<T> typeReferenceValue){
        try {
            return JSON.parseObject(json, typeReferenceValue);
        }catch(Exception e) {
            log.warn("====== JsonUtil toObject can't parse {} to {}, message = {}", json, typeReferenceValue.getType(), e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * json字符串 转 List<Map<String, Object>> 数据
     *
     * @param json json 字符串
     * @return List<Map<String, Object>> 数据
     */
    @Deprecated
    public static List<Map<String, Object>> toListStr2ObjMap(String json) {
        return toListStr2ObjMap(json, Object.class);
    }

    /**
     * json字符串 转 List<Map<String, T>> 数据（指定数据类型）
     *
     * @param json json 字符串
     * @return List<Map<String, T>> 数据
     */
    @Deprecated
    public static <V> List<Map<String, V>> toListStr2ObjMap(String json, Class<V> vKlz) {
        return toListMap(json, String.class, vKlz);
    }

    /**
     * json 字符串 转换为指定数据类型 List<Map<K, V>>
     * @param json json 字符串
     * @param kClz Map 的 key 泛型
     * @param vClz Map 的 value 泛型
     * @param <K> 泛型
     * @param <V> 泛型
     * @return List<Map<K, V>>
     */
    // todo 此方法有问题，实现不了转换，不要用，可以看test 方法 {@link com.xgf.util.common.JsonUtilTest.testToListMap()}
    @Deprecated
    public static <K, V> List<Map<K, V>> toListMap(String json, Class<K> kClz, Class<V> vClz) {
        try {
            return JSON.parseObject(json, new TypeReference<List<Map<K, V>>>() {});
        } catch (Exception e) {
            log.warn("====== JsonUtil toListStr2ObjMap can't parse {} to <{}, {}>, message = {}", json, kClz, vClz, e.getLocalizedMessage(), e);
            return null;
        }
    }

    // todo 问题同上
    @Deprecated
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClz, Class<V> vClz) {
        try {
            return JSON.parseObject(json, new TypeReference<Map<K, V>>() {});
        } catch (Exception e) {
            log.warn("====== JsonUtil toListStr2ObjMap can't parse {} to <{}, {}>, message = {}", json, kClz, vClz, e.getLocalizedMessage(), e);
            return null;
        }
    }

}