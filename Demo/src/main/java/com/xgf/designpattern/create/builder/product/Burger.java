package com.xgf.designpattern.create.builder.product;

import com.xgf.designpattern.create.builder.product.dineway.Dining;
import com.xgf.designpattern.create.builder.Item;

import javax.annotation.Resource;

/**
 * @author xgf
 * @create 2021-11-20 22:22
 * @description 汉堡类
 **/

public abstract class Burger implements Item {

    @Resource(name = "dineInRoom")
    private Dining dineInRoom;

    @Resource(name = "wrapper")
    private Dining wrapper;

//    /**
//     * 就餐方式
//     */
//    private Dining dining;

//    @Override
//    public Dining dining(String way){
//        if(StringUtils.isBlank(way)){
//            throw CustomExceptionEnum.PARAM_VALUE_CAN_NOT_NULL_EXCEPTION.generateException();
//        }
//
//        if(way.equals(DineInEnum.DINEINROOM.getCode())){
//            dining = dineInRoom;
////            dining = new DineInRoom();
//        }else if(way.equals(PackingEnum.WRAPPER.getCode())){
//            dining =  wrapper;
////            dining = new Wrapper();
//        }else {
//            // 其它使用情况不允许
//            throw CustomExceptionEnum.PARAM_TYPE_ILLEGAL_EXCEPTION.generateException("param = " + way);
//        }
//        return dining;
//    }

    @Override
    public Dining dining() {
//        if(Objects.isNull(dining)){
//            throw CustomExceptionEnum.PARAM_TYPE_ILLEGAL_EXCEPTION.generateException("，请选择就餐方式");
//        }
//        return this.dining.dineWay();
        return wrapper;
    }
}
