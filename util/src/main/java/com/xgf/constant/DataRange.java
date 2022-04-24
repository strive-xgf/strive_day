package com.xgf.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author xgf
 * @create 2022-04-24 22:38
 * @description 数据范围通用
 **/

@Data
@NoArgsConstructor
@ApiModel(value = "数据范围通用类", description = "范围属性可用")
public class DataRange<T extends Comparable<T>> implements Serializable {

    private static final long serialVersionUID = -5852562252800005966L;

    @ApiModelProperty("范围下界，开始区间")
    private T lower;

    @ApiModelProperty("范围上界，结束区间")
    private T upper;

    @ApiModelProperty("包含下界，闭区间，默认包含true")
    private boolean includeLower = true;

    @ApiModelProperty("包含上界，闭区间，默认包含true")
    private boolean includeUpper = true;

    public DataRange(T lower, T upper) {
        Objects.requireNonNull(lower);
        Objects.requireNonNull(upper);

        this.lower = lower;
        this.upper = upper;
        compareConvert();
    }

    public DataRange(T lower, T upper, boolean includeLower, boolean includeUpper) {
        Objects.requireNonNull(lower);
        Objects.requireNonNull(upper);

        this.lower = lower;
        this.includeLower = includeLower;
        this.upper = upper;
        this.includeUpper = includeUpper;
        compareConvert();
    }

    public static <T extends Comparable<T>> DataRange<T> valueOf(T lower, T upper) {
        return new DataRange<T>(lower, upper);
    }

    public static <T extends Comparable<T>> DataRange<T> valueOf(T lower, T upper, boolean includeLower, boolean includeUpper) {
        return new DataRange<>(lower, upper, includeLower, includeUpper);
    }

    /**
     * 判断入参 value 是否在区间范围内
     * @param value 入参值
     * @return true 在区间范围内
     */
    public boolean inRange(T value) {
        Objects.requireNonNull(value);

        // 根据 includeLower 和 includeUpper 区分开闭区间
        return (includeLower ? value.compareTo(lower) >= 0 : value.compareTo(lower) > 0)
                && (includeUpper ? value.compareTo(upper) <= 0 : value.compareTo(upper) < 0);
    }

    /**
     * 比较且转换大小位置（上下界值）
     */
    private void compareConvert() {
        if (lower.compareTo(upper) > 0) {
            T temp = lower;
            lower = upper;
            upper = temp;
        }
    }

    /**
     * 打印区间范围参数
     */
    public String printRange() {
        return (includeLower ? StringConstantUtil.LEFT_MIDDLE_BRACKET : StringConstantUtil.LEFT_SMALL_BRACKET)
                + lower
                + StringConstantUtil.COMMA + StringConstantUtil.BLANK
                + upper
                + (includeUpper ? StringConstantUtil.RIGHT_MIDDLE_BRACKET : StringConstantUtil.RIGHT_SMALL_BRACKET);

    }


}
