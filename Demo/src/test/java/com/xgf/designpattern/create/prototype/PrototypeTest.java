package com.xgf.designpattern.create.prototype;

import com.xgf.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xgf
 * @create 2021-11-22 14:47
 * @description
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class PrototypeTest {

    @Test
    public void test(){

        // 加载对象原型
        PhoneCache.loadCache();

        Phone phonePrototypeA1 = PhoneCache.getPhonePrototype(1);
        Phone phonePrototypeA2 = PhoneCache.getPhonePrototype(1);
        Phone phonePrototypeA3 = PhoneCache.getPhonePrototype(1);
        System.out.println("phonePrototypeA1 = " + phonePrototypeA1 + "\t\t" + phonePrototypeA1.description());
        System.out.println("phonePrototypeA2 = " + phonePrototypeA2 + "\t\t" + phonePrototypeA2.description());
        System.out.println("phonePrototypeA3 = " + phonePrototypeA3 + "\t\t" + phonePrototypeA3.description());

        Phone phonePrototypeB1 = PhoneCache.getPhonePrototype(2);
        Phone phonePrototypeB2 = PhoneCache.getPhonePrototype(2);
        Phone phonePrototypeB3 = PhoneCache.getPhonePrototype(2);

        System.out.println("phonePrototypeB1 = " + phonePrototypeB1 + "\t\t" + phonePrototypeB1.description());
        System.out.println("phonePrototypeB2 = " + phonePrototypeB2 + "\t\t" + phonePrototypeB2.description());
        System.out.println("phonePrototypeB3 = " + phonePrototypeB3 + "\t\t" + phonePrototypeB3.description());

    }
}
