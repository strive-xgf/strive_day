package com.xgf.designpattern.structure.proxy.dynamicproxy.cglib.custom;

import org.springframework.stereotype.Component;

/**
 * @author xgf
 * @create 2022-04-17 11:39
 * @description 模拟 service 实现
 **/

@Component("caseMethodService")
public class CaseMethodService {

    public void saveOneObject() {
        System.out.println(">>>>>> CaseMethodService saveOneObject");
    }

    public void batchSaveObject() {
        System.out.println(">>>>>> CaseMethodService batchSaveObject");
    }

    public void deleteOneObject() {
        System.out.println(">>>>>> CaseMethodService deleteOneObject");
    }

    public int saveOneObjectReturnResult() {
        System.out.println(">>>>>> CaseMethodService saveOneObject");
        return 1;
    }

}
