package com.xgf.check.validate;

import com.alibaba.fastjson.JSON;
import com.xgf.check.CompareOperatorEnum;
import com.xgf.constant.CommonNullConstantUtil;
import com.xgf.constant.DataEntry;
import com.xgf.constant.StringConstantUtil;
import com.xgf.exception.CustomExceptionEnum;
import com.xgf.reflect.CommonReflectUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @author xgf
 * @create 2022-05-08 18:21
 * @description 通用数据比较校验
 * 校验传值方式:
 * 1. valueEntry + operatorEnum + [dealSpecialValueFlag] （这种方式直接比较值）
 * 2. checkObject + columnEntry + operatorEnum + [dealSpecialValueFlag] （这种方式是反射对象 checkObject 中的列值实现）
 * 【注意】：存在空值不校验（校验通过）
 **/

@Data
@Slf4j
public class CommonCheckCompareData {

    /**
     * 校验对象（使用 columnEntry 时必传）
     */
    private Object checkObject;

    /**
     * <校验列, 条件列> （对象参数列名）
     * 优先级高于 valueEntry
     * 存在会通过反射获取对象参数对应的值，转换到 valueEntry ，进行校验（valueEntry被覆盖）
     * 依赖 checkObject （反射 checkObject 对象中的字段值）
     */
    private DataEntry<String, String> columnEntry;

    /**
     * <校验值, 条件值> （对象参数值）
     * 实际校验值，只有当 columnEntry 为空的时候，此值才有效（此值不依赖 checkObject）
     */
    private DataEntry<Object, Object> valueEntry;

    /**
     * 比较运算符（必传）
     */
    private CompareOperatorEnum operatorEnum;

    /**
     * 特殊值（Null值）是否处理不校验, true 则不校验（校验通过）
     * {@link CommonNullConstantUtil#checkSpecialValue(java.lang.Object)}
     */
    private Boolean dealSpecialValueFlag;



    /**
     * 校验是否满足，不满足抛出异常
     */
    public void checkData() {
        checkData(this);
    }

    /**
     * 校验是否满足，不满足抛出异常
     * @param param 参数
     */
    public static void checkData(CommonCheckCompareData param) {
        if (param.columnEntry == null && param.valueEntry == null) {
            throw CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION.generateCustomMessageException("参与比较字段不存在");
        }

        if (param.operatorEnum == null) {
            throw CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION.generateCustomMessageException("比较运算符不能为空");
        }

        if (param.columnEntry != null && param.getCheckObject() == null) {
            throw CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION.generateCustomMessageException("column值存在，校验对象不能为空");
        }

        // 不为空，则 columnEntry 反射取值 替换 valueEntry
        if (param.columnEntry != null) {
            // 反射获取字段列的属性值
            Map<String, Object> fieldName2ValueMap = CommonReflectUtil.getFieldName2ValueMap(param.getCheckObject(), Arrays.asList(param.columnEntry.getKey(), param.columnEntry.getValue()));
            // 未找到 columnEntry 对应的2个字段值，抛出异常
            if (MapUtils.isEmpty(fieldName2ValueMap) || fieldName2ValueMap.size() != 2) {
                throw CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION.generateCustomMessageException("参与比较字段不存在");
            }

            // 找到，替换 valueEntry 进行值校验
            param.valueEntry = DataEntry.valueOf(fieldName2ValueMap.get(param.columnEntry.getKey()), fieldName2ValueMap.get(param.columnEntry.getValue()));
        }

        // 存在空值不校验
        if (param.valueEntry == null || param.valueEntry.getKey() == null || param.valueEntry.getValue() == null) {
            log.info("======= CommonCheckCompareData exist null, not check, valueEntry = " + JSON.toJSONString(param.valueEntry));
            return;
        }

        // 存在特殊值不校验
        if (BooleanUtils.isTrue(param.dealSpecialValueFlag)) {
            if (CommonNullConstantUtil.checkSpecialValue(param.valueEntry.getKey()) || CommonNullConstantUtil.checkSpecialValue(param.valueEntry.getValue())) {
                log.info("======= CommonCheckCompareData exist specialValue, not check, valueEntry = " + JSON.toJSONString(param.valueEntry));
                return;
            }
        }

        Object o1 = param.valueEntry.getKey();
        Object o2 = param.valueEntry.getValue();

        boolean compareFlag = false;

        if(o1 instanceof Integer && o2 instanceof Integer){
            compareFlag = CompareOperatorEnum.compareOperator(((Integer) o1).compareTo((Integer)o2), param.operatorEnum);

        }else if(o1 instanceof Long && o2 instanceof Long){
            compareFlag = CompareOperatorEnum.compareOperator(((Long) o1).compareTo((Long)o2), param.operatorEnum);

        }else if(o1 instanceof Float && o2 instanceof Float){
            compareFlag = CompareOperatorEnum.compareOperator(((Float) o1).compareTo((Float)o2), param.operatorEnum);

        }else if(o1 instanceof Double && o2 instanceof Double){
            compareFlag = CompareOperatorEnum.compareOperator(((Double) o1).compareTo((Double)o2), param.operatorEnum);

        }else if(o1 instanceof BigDecimal && o2 instanceof BigDecimal){
            compareFlag = CompareOperatorEnum.compareOperator(((BigDecimal) o1).compareTo((BigDecimal)o2), param.operatorEnum);

        }else if(o1 instanceof Date && o2 instanceof Date){
            compareFlag = CompareOperatorEnum.compareOperator(((Date) o1).compareTo((Date)o2), param.operatorEnum);

        }else if(o1 instanceof String && o2 instanceof String){
            compareFlag = CompareOperatorEnum.compareOperator(((String) o1).compareTo((String)o2), param.operatorEnum);

        } else if (StringConstantUtil.checkStrIsFloatNumber(String.valueOf(o1)) && StringConstantUtil.checkStrIsFloatNumber(String.valueOf(o2))) {
            // 兼容两种数值类型（正则表达式匹配浮点数），数据类型不一致（eg: Integer, Double）导致的比较抛出异常
            // 这里数值类型全部转换为 BigDecimal 进行比较 todo: 是否直接String比较
            compareFlag = CompareOperatorEnum.compareOperator(new BigDecimal(String.valueOf(o1)).compareTo(new BigDecimal(String.valueOf(o2))), param.operatorEnum);

        } else if(o1 instanceof Comparable && o2 instanceof Comparable){
            try {
                compareFlag = CompareOperatorEnum.compareOperator(((Comparable) o1).compareTo(o2), param.operatorEnum);
            } catch (ClassCastException e) {
                // 类转换异常，抛出原因
                throw CustomExceptionEnum.DATA_CONVERT_EXCEPTION.generateCustomMessageException("校验值的类型不一致: " + o1.getClass().getName() + "  <>  " + o2.getClass().getName());
            }

        }else {
            throw CustomExceptionEnum.PARAM_TYPE_ILLEGAL_EXCEPTION.generateCustomMessageException("CommonCheckCompareData 校验字段类型必须实现Comparable接口");
        }
        
        // 根据运算符比较结果
        if (! compareFlag) {
            throw CustomExceptionEnum.CUSTOM_CHECK_DATA_ILLEGAL_EXCEPTION.generateCustomMessageException("不满足条件: "
                    + param.valueEntry.getKey() + param.getOperatorEnum().getValue() + param.valueEntry.getValue());
            
        }

    }





}
