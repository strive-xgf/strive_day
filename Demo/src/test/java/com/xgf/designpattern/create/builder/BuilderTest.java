package com.xgf.designpattern.create.builder;

import com.xgf.DemoApplication;
import com.xgf.designpattern.create.builder.product.dineway.Dining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xgf
 * @create 2021-11-20 23:48
 * @description
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class BuilderTest {

    @Resource(name = "dineInRoom")
    private Dining dineInRoom;

    @Resource(name = "wrapper")
    private Dining wrapper;

    @Resource(name = "bottle")
    private Dining bottle;

    @Resource(name = "chickenBurger")
    private Item chickenBurger;

    @Resource(name = "beefBurger")
    private Item beefBurger;

    @Resource(name = "pepsi")
    private Item pepsi;

    @Resource(name = "coke")
    private Item coke;

    @Resource(name = "mealBuilder")
    private MealBuilder mealBuilder;


    @Test
    public void test(){
        // Builder 组合菜单
        System.out.println(mealBuilder.SpecialMeal().showItems());
        System.out.println("==========");
        Meal meal = mealBuilder.generateMeal(beefBurger, chickenBurger, coke, pepsi);
        System.out.println(meal.showItems());
        System.out.println("总价 = " + meal.getCost());
    }

//    @Test
//    public void test(){
//        // Builder 组合菜单
//        Meal meal = mealBuilder.SpecialMeal();
//        // todo 没有共用一个对象，所以没有改变就餐方式
////        beefBurger.dining(PackingEnum.WRAPPER.getCode());
//        System.out.println(meal.showItems());
//
//        System.out.println("==========");
//
//
//        ChickenBurger chickenBurger = new ChickenBurger();
////        chickenBurger.dining(DineInEnum.DINEINROOM.getCode());
//        BeefBurger beefBurger = new BeefBurger();
////        chickenBurger.dining(PackingEnum.WRAPPER.getCode());
//        Pepsi pepsi = new Pepsi();
////        pepsi.dining(PackingEnum.BOTTLE.getCode());
//
//        Meal myMeal = mealBuilder.generateMeal(chickenBurger, beefBurger, pepsi);
//        System.out.println(myMeal.showItems());
//        System.out.println("总价 =" + myMeal.getCost());
//
//    }


}
