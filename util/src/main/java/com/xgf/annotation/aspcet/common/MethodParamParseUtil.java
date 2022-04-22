package com.xgf.annotation.aspcet.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xgf.annotation.MethodParamAnnotation;
import com.xgf.constant.StringConstantUtil;
import com.xgf.exception.CustomExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author xgf
 * @create 2022-04-21 01:36
 * @description 方法参数解析工具类
 **/

@Slf4j
@Component("methodParamParseUtil")
public class MethodParamParseUtil {

    public List<?> parseParamValueList(JoinPoint joinPoint) {
        log.info("====== MethodParamParseUtil parseParamValueList start, method = 【{}】", joinPoint.getSignature().toLongString());

        // 参数按paramType解析之后的数据
        List<?> paramValueList = new ArrayList<>();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取方法上的注解信息
        MethodParamAnnotation annotation = signature.getMethod().getAnnotation(MethodParamAnnotation.class);

        String annotationValue = getAnnotationValue(joinPoint, annotation.methodParam());
        log.info("====== MethodParamParseUtil parseParamValueList annotationValue = 【{}】", annotationValue);

        if (StringUtils.isBlank(annotationValue)) {
            log.info("====== MethodParamParseUtil parseParamValueList annotationValue is blank");
            return new ArrayList<>();
        }


        // 按 paramType 将数据解析为对象形式（按JSON对象解析，防止解析报错）
        try {
            Object parse = JSON.parse(annotationValue);
            if(Objects.isNull(parse)){
                log.warn("====== MethodParamParseUtil value parse is null");
                return paramValueList;
            }else if(parse instanceof JSONObject){
                // 将json数据转换成对象
                paramValueList.add(JSON.parseObject(annotationValue, (Type) annotation.paramType()));
            }else if(parse instanceof JSONArray){
                // 将json数据转换为数组
                paramValueList = JSON.parseArray(annotationValue, annotation.paramType());
            }else{
                throw CustomExceptionEnum.DATA_PARSE_EXCEPTION.generateException();
            }
        } catch (Exception e) {
            log.error("====== MethodParamParseUtil parseParamValueList json data exception, annotationValue = 【{}】, exception message = 【{}】", annotationValue, e.getMessage(), e);
            throw e;
        }

        log.info("====== MethodParamParseUtil parseParamValueList result size =【{}】, value = 【{}】", paramValueList.size(), JSON.toJSONString(paramValueList));
        return paramValueList;
    }

    /**
     * 获取注解中传递的参数值 （动态参数取值）
     *
     * @param joinPoint JoinPoint
     * @param paramName 参数名称
     * @return 参数名称对应参数值
     */
    private String getAnnotationValue(JoinPoint joinPoint, String paramName) {

        // 非动态参数: #{paramName} ，按参数值返回
        if (!paramName.matches("^#\\{\\D*\\}")) {
            return paramName;
        }

        // 获取方法中所有的参数
        Map<String, Object> paramName4ValueMap = getParamName4ValueMap(joinPoint);

        // 解析动态参数: #{paramName}  >>> 参数名去除 #{}
        paramName = paramName.replace(StringConstantUtil.POUND + StringConstantUtil.LEFT_BIG_BRACKET, StringConstantUtil.EMPTY)
                .replace(StringConstantUtil.RIGHT_BIG_BRACKET, StringConstantUtil.EMPTY);

        // 单个动态参数返回 (eg : 参数名）
        if (!paramName.contains(StringConstantUtil.DOT)) {
            return JSON.toJSONString(paramName4ValueMap.get(paramName));
        }

        // 复杂参数类型解析（eg：对象.参数名.参数名. ... .参数名），按点 . 分割 【paramName.split("\\.");】
        String[] split = paramName.split(StringConstantUtil.DOWN_DIAGONAL + StringConstantUtil.DOT);
        // 获取方法中对象的内容（首位是方法形参名【入参名称】，第二位开始是方法入参内容对应的具体字段）
        Object paramValue = paramName4ValueMap.get(split[0]);
        // 获取参数值，从第二位开始遍历字段【首位是参数对象名称】
        for (int i = 1; i < split.length; i++) {
            if (Objects.isNull(paramValue)) {
                // 参数为 null 的情况，打印日志 【注意】外层要处理为空的情况，此处可以终止循环，因为null循环结果还是null
                log.warn("====== MethodParamParseUtil param = {} value is null", paramName);
//                throw CustomExceptionEnum.PARAM_TYPE_ILLEGAL_EXCEPTION.generateException(paramName + " value is null");
            }
            paramValue = getFieldValue(paramValue, split[i]);
        }

        return JSON.toJSONString(paramValue);
    }

    /**
     * 反射获取 Object 对象指定字段名称的值
     *
     * @param object 对象
     * @param fieldName 字段名
     * @return 字段对应值
     */
    private static Object getFieldValue(Object object, String fieldName) {
        // 兼容参数值为 null 的情况（eg: User: hobby = null, age = 18, 传值 #{user.hobby}
        if (Objects.isNull(object)) {
            return null;
        }

        // 获取所有的属性数组
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(fieldName)) {
                try {
                    return field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取方法的参数名和对应的值
     *
     * @param joinPoint 链接点
     * @return map集合，key : 方法形参名， value : 方法形参值
     *  eg:
     *  User : 属性, hobby, age, uuid
     *  方法 : public void test(User user)
     *  user对象 : hobby = "hobby", age = 18, uuid = "uuid"
     *  调用: test(user)，添加注解 #{user}
     *  该方法返回值：{"user":{"hobby":"hobby", "age":18, "uuid":"uuid"}}
     */
    private Map<String, Object> getParamName4ValueMap(JoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>(8);
        // 获取入参对象数组（入参值）【参数量返回数组】
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取入参名称（形参名）
        String[] paramNames = signature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            params.put(paramNames[i], args[i]);
        }
        return params;
    }

}
