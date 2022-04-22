package com.xgf.check;

import com.xgf.exception.CustomExceptionEnum;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xgf
 * @create 2022-04-22 01:20
 * @description 通用校验接口
 **/


public class CheckCommonUtil {

    /**
     * 校验对象属性值是否都为null
     * @param obj 对象
     * @return true: 都为null
     */
    public static boolean checkAllFieldsIsNull(Object obj) {
        if (Objects.isNull(obj)) {
            return true;
        }

        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
//                if (Objects.nonNull(field.get(obj)) && StringUtils.isNotBlank(field.get(obj).toString())) {
                if (Objects.nonNull(field.get(obj))) {
                    return false;
                }
            }
        } catch (Exception e) {
            throw CustomExceptionEnum.REFLECT_EXCEPTION.generateException(e.toString());
        }

        return true;
    }

    /**
     * 校验集合中所有对象属性是否都为null
     * @param objList 参数list
     * @return true: 集合对象属性都为null
     */
    public static boolean checkAllFieldsIsNull(List<?> objList) {
        AtomicBoolean flag = new AtomicBoolean(true);
        if (CollectionUtils.isEmpty(objList)) {
            return flag.get();
        }

        objList.stream().filter(Objects::nonNull).forEach(obj -> {
            if (! checkAllFieldsIsNull(obj)) {
                flag.set(false);
                return;
            }
        });
        return flag.get();
    }

    public static boolean checkAllFieldsIsNull(Object... paras) {
        return checkAllFieldsIsNull(Arrays.asList(paras));
    }





}
