package com.xgf.designpattern.structure.adapter.obj;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xgf
 * @create 2021-11-22 23:41
 * @description
 **/

@Slf4j
public class TargetAbleImpl implements TargetAble {
    @Override
    public void editTextFile() {
        log.info("====== TargetAbleImpl editTextFile");
    }

    @Override
    public void editExcelFile() {
        log.info("====== TargetAbleImpl editExcelFile");
    }

    @Override
    public void editWordFile() {
        log.info("====== TargetAbleImpl editWordFile");
    }
}
