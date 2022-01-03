package com.xgf.designpattern.structure.decorator;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xgf
 * @create 2022-01-03 19:22
 * @description 装饰器
 **/

@Data
@Component("foodDecorator")
public class FoodDecorator extends Decorator {

    @Override
    public void eat() {
        // 附加功能
        System.out.println("====== decorator start eat");
        super.eat();
        System.out.println("====== decorator eat beef");
        System.out.println("====== decorator eat mutton");
        System.out.println("====== decorator drink");
        System.out.println("====== decorator end eat");

    }
}
