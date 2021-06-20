package com.xgf.util;

import com.xgf.springbean.BeanCopyUtil;
import lombok.Data;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author strive_day
 * @create 2021-06-20 20:04
 * @description 测试工具类 BeanCopyUtil，复制对象/集合
 */


public class BeanCopyUtilTest {

    /**
     * 测试对象属性的赋值
     */
    @Test
    public void testCopyProperties_Object() {
//        User5Property user5 = CreateRadomObject.createObject(new User5Property());
        User4Property user4 = CreateRadomObject.createObject(new User4Property());
//        User2Property user2 = CreateRadomObject.createObject(new User2Property());
        User5Property user5 = new User5Property();
//        User4Property user4 = new User4Property();
        User2Property user2 = new User2Property();

//        this.copyProperties(user5, user4);
//        this.copyProperties(user5, user2);


        System.out.println("user5 = " + user5);
        System.out.println("user4 = " + user4);
        System.out.println("user2 = " + user2);

        BeanCopyUtil.convertObject2Object(user4, user5);
        BeanCopyUtil.convertObject2Object(user4, user2);

        System.out.println("user5 = " + user5);
        System.out.println("user4 = " + user4);
        System.out.println("user2 = " + user2);
    }

    @Test
    public void testCopyProperties_List() {
//        List<User5Property> user5List = CreateRadomObject.createObjectList(new User5Property(), 3);
        List<User4Property> user4List = CreateRadomObject.createObjectList(new User4Property(), 3);
//        List<User2Property> user2List = CreateRadomObject.createObjectList(new User2Property(), 3);
        List<User5Property> user5List = new ArrayList<>();
//        List<User4Property> user4List = new ArrayList<>();
        List<User2Property> user2List = new ArrayList<>();


        System.out.println("user5List = " + user5List);
        System.out.println("user4List = " + user4List);
        System.out.println("user2List = " + user2List);

        user5List = BeanCopyUtil.convertList2List(user4List, User5Property.class);
        user2List = BeanCopyUtil.convertList2List(user4List, User2Property.class);

//        user4List = this.convertList2List(user5List, User4Property.class);
//        user2List = this.convertList2List(user5List, User2Property.class);

        System.out.println("user5List = " + user5List);
        System.out.println("user4List = " + user4List);
        System.out.println("user2List = " + user2List);
    }
    
}



/**
 * 创建随机对象类
 */
class CreateRadomObject {

    /**
     * 保留两位小数
     */
    public static final DecimalFormat DECIMAL_FORMAT_2 = new DecimalFormat("#.00");
    public static final String ROUND_2 = "#.00";

    /**
     * double类型保留小数  --- 用于显示
     *
     * @param d double类型数据
     * @return 返回保留小数后的结果
     */
    public static String toFixed_X(double d) {
        return toFixed_X(d, ROUND_2);
//        return DECIMAL_FORMAT_2.format(d);
    }

    /**
     * double类型保留小数
     *
     * @param d double类型数据
     * @param format 保留格式
     * @return 返回保留后的结果
     */
    public static String toFixed_X(double d, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(d);
    }

    /**
     * 批量随机创建对象列表： User5Property/User4Property/User2Property
     *
     * @param n 创建数目
     * @return 返回创建列表
     */
    public static <T> List<T> createObjectList(T t, Integer n) {

        List<T> list = null;
        if (t instanceof User5Property) {
            list = (List<T>) new ArrayList<User5Property>(n);
        } else if (t instanceof User4Property) {
            list = (List<T>) new ArrayList<User4Property>(n);
        } else if (t instanceof User2Property) {
            list = (List<T>) new ArrayList<User2Property>(n);
        } else {
            System.out.println(" 当前类不支持");
            return new ArrayList<>();
        }

        for (int i = 1; i <= n; i++) {
            if (t instanceof User5Property) {
                User5Property user = new User5Property();
                user.setId(i);
                user.setUuid(UUID.randomUUID().toString().replace("-", ""));
                user.setAge(i * 10);
                user.setName("【user - " + i + "】");
                user.setPinMoney((double) Math.round(new Random().nextDouble() * 10000));
                list.add((T) user);
            } else if (t instanceof User4Property) {
                User4Property user = new User4Property();
                user.setUuid(UUID.randomUUID().toString().replace("-", ""));
                user.setAge(i * 10);
                user.setName("【user - " + i + "】");
                user.setPinMoney((double) Math.round(new Random().nextDouble() * 10000));
                list.add((T) user);
            } else if (t instanceof User2Property) {
                User2Property user = new User2Property();
                user.setUuid(UUID.randomUUID().toString().replace("-", ""));
                user.setName("【user - " + i + "】");
                list.add((T) user);
            }
        }
        return list;
    }


    /**
     * 随机创建对象：User5Property/User4Property/User2Property
     *
     * @return 返回创建对象
     */
    public static <T> T createObject(T t) {
        if (t instanceof User5Property) {
            User5Property user = new User5Property();
            user.setId(new Random().nextInt(100));
            user.setUuid(UUID.randomUUID().toString().replace("-", ""));
            user.setAge(new Random().nextInt(100));
            user.setName("【user - " + user.getId() + "】");
            user.setPinMoney((double) Math.round(new Random().nextDouble() * 10000));
            return (T) user;
        } else if (t instanceof User4Property) {
            User4Property user = new User4Property();
            user.setUuid(UUID.randomUUID().toString().replace("-", ""));
            user.setAge(new Random().nextInt(100));
            user.setPinMoney((double) Math.round(new Random().nextDouble() * 10000));
            user.setName("【user - " + new Random().nextInt(100) + "】");
            return (T) user;
        } else if (t instanceof User2Property) {
            User2Property user = new User2Property();
            user.setUuid(UUID.randomUUID().toString().replace("-", ""));
            user.setName("【user - " + new Random().nextInt(100) + "】");
            return (T) user;
        }
        return null;
    }
}


/**
 * 5个属性的User
 */
@Data
class User5Property {
    private Integer id;
    private String uuid;
    private String name;
    private Integer age;
    private Double pinMoney;
}

/**
 * 4个属性的user
 */
@Data
class User4Property {
    private String uuid;
    private String name;
    private Integer age;
    private Double pinMoney;
    // 多出的属性不会影响复制
//    private short k;
}

/**
 * 2个属性的user
 */
@Data
class User2Property {
    private String uuid;
    private String name;
}