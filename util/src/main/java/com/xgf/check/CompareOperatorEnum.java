package com.xgf.check;

import com.xgf.constant.EnumBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xgf
 * @create 2022-05-08 00:55
 * @description 比较操作符枚举
 **/

@Slf4j
@Getter
@AllArgsConstructor
public enum CompareOperatorEnum implements EnumBase {
    /**
     * 等于
     */
    EQ("eq", " = "),

    /**
     * 不等于
     */
    NEQ("neq", " != "),

    /**
     * 大于
     */
    GT("gt", " > "),

    /**
     * 大于等于
     */
    GTE("gte", " >= "),

    /**
     * 小于
     */
    LT("lt", " < "),

    /**
     * 小于等于
     */
    LTE("lte", " <= "),
    ;

    private String code;
    private String value;

    /**
     * 比较结果，与当前枚举进行对比，
     *
     * @param compareValue compare() 方法比较结果作为入参
     * @param operatorEnum 比较枚举值
     * @return 相等，则返回true
     */
    public static boolean compareOperator(int compareValue, CompareOperatorEnum operatorEnum) {
        if (CompareOperatorEnum.EQ.equals(operatorEnum)) {
            return compareValue == 0;

        } else if (CompareOperatorEnum.NEQ.equals(operatorEnum)) {
            return compareValue != 0;

        } else if (CompareOperatorEnum.GT.equals(operatorEnum)) {
            return compareValue > 0;

        } else if (CompareOperatorEnum.GTE.equals(operatorEnum)) {
            return compareValue >= 0;

        } else if (CompareOperatorEnum.LT.equals(operatorEnum)) {
            return compareValue < 0;

        } else if (CompareOperatorEnum.LTE.equals(operatorEnum)) {
            return compareValue <= 0;

        }

        log.warn("====== CompareOperatorEnum exist illegal value = " + operatorEnum.name());
        return false;

    }
}
