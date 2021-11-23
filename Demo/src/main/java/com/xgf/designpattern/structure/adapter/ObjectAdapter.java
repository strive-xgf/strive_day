package com.xgf.designpattern.structure.adapter;

import com.xgf.designpattern.structure.adapter.obj.Source;
import com.xgf.designpattern.structure.adapter.obj.TargetAble;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xgf
 * @create 2021-11-23 00:11
 * @description 对象适配器 持有Source类的实例，并实现Targetable接口，解决兼容性问题
 **/

@Slf4j
@Data
public class ObjectAdapter implements TargetAble {

    private Source source;

    public ObjectAdapter(Source source) {
        this.source = source;
    }

    @Override
    public void editTextFile() {
        // 调用Source类方法
        source.editTextFile();
    }

    @Override
    public void editExcelFile() {
        log.info("====== ObjectAdapter editExcelFile");
    }

    @Override
    public void editWordFile() {
        log.info("====== ObjectAdapter editWordFile");
    }
}
