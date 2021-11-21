package com.xgf.designpattern.create.builder;

import com.xgf.exception.CustomExceptionEnum;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xgf
 * @create 2021-11-21 21:42
 * @description 菜单类 Meal 类，包含多个 Item 条目对象
 **/

@Data
public class Meal {

    private List<Item> items = new ArrayList<>();

    /**
     * 点餐 - 增加食品
     * @param item 食品条目
     */
    public void addItem(Item item){
        items.add(item);
    }

    /**
     * 获取花费总价
     * @return 总价
     */
    public double getCost(){
        double cost = 0;
        for (Item item : items) {
            cost += item.price();
        }
        return cost;
    }

    /**
     * 展示菜单信息
     */
    public String showItems(){

        if(CollectionUtils.isEmpty(items)){
            throw CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION.generateException("，您还没有选择对应的商品哟");
        }

        StringBuilder sb = new StringBuilder("=== 您选择的菜单内容如下：===").append("\n");
        items.stream().filter(Objects::nonNull).forEach(item -> {
            sb.append("名称 : ").append(item.name())
                    .append(", 就餐方式 : ").append(item.dining())
                    .append(", 单价 :").append(item.price()).append(item.unit())
                    .append("\n");
        });
        return sb.toString();
    }

}
