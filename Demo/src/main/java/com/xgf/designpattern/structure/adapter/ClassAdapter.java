package com.xgf.designpattern.structure.adapter;

import com.xgf.designpattern.structure.adapter.obj.Source;
import com.xgf.designpattern.structure.adapter.obj.TargetAble;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xgf
 * @create 2021-11-22 23:43
 * @description 类适配器，继承Source类并实现Targetable接口
 **/

@Slf4j
public class ClassAdapter extends Source implements TargetAble {

    @Override
    public void editExcelFile() {
        log.info("====== ClassAdapter editExcelFile");
    }

    @Override
    public void editWordFile() {
        log.info("====== ClassAdapter editWordFile");
    }
}
