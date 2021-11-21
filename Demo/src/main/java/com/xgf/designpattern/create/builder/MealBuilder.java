package com.xgf.designpattern.create.builder;

import com.xgf.designpattern.create.builder.product.BeefBurger;
import com.xgf.designpattern.create.builder.product.Pepsi;
import com.xgf.spring.SpringUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author xgf
 * @create 2021-11-21 22:18
 * @description 菜单 Builder 负责创建 Meal 对象
 *      建造者模式：基本部件不会变，而其组合经常变化的时候，组装成一个结果给外部直接调用（建造者模式更加关注与零件装配的顺序）
 **/

@Component("mealBuilder")
public class MealBuilder {

    /**
     * 特价套餐，组合Item生成Builder 【Builder模式 - 基本部件不会变，而其组合经常变化的】
     * @return 套餐
     */
    public Meal SpecialMeal(){
        Meal meal = new Meal();

//        Burger beefBurger = new BeefBurger();
//        beefBurger.dining(DineInEnum.DINEINROOM.getCode());
//        meal.addItem(beefBurger);
//
//        ColdDrink pepsi = new Pepsi();
//        pepsi.dining(PackingEnum.BOTTLE.getCode());
//        meal.addItem(pepsi);

        // todo strive_day 因为每个Item都有一个就餐类型属性，不能再共用一个对象了
        Item beefBurger = SpringUtil.getBean(BeefBurger.class);
        meal.addItem(beefBurger);

        Item pepsi = SpringUtil.getBean(Pepsi.class);
        meal.addItem(pepsi);

        return meal;
    }

    /**
     * 生成菜单
     * @param itemList 视频条目
     * @return 菜单
     */
    public Meal generateMeal (List<Item> itemList){
        Meal meal = new Meal();
        itemList.forEach(meal::addItem);
        return meal;
    }

    /**
     * 生成菜单
     * @param items 食品条目
     * @return 菜单
     */
    public Meal generateMeal (Item ...items){
        Meal meal = new Meal();
        Arrays.stream(items).forEach(meal::addItem);
        return meal;
    }

}
