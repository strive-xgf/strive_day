package com.xgf.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xgf
 * @create 2022-04-24 23:03
 * @description key : value 数据
 **/

@Data
@NoArgsConstructor
@ApiModel(value = "key : value 数据",description = "类似 Map.Entry 存储结构")
public class DataEntry<T,V> implements Serializable {

    private static final long serialVersionUID = -6878606628792525366L;

    @ApiModelProperty("key")
    private T key;

    @ApiModelProperty("value")
    private V value;

    public DataEntry(T key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <T, V> DataEntry<T, V> valueOf(T key, V value) {
        return new DataEntry<>(key, value);
    }




}
