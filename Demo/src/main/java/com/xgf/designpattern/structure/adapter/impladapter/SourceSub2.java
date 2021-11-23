package com.xgf.designpattern.structure.adapter.impladapter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xgf
 * @create 2021-11-23 00:27
 * @description
 **/

@Slf4j
public class SourceSub2 extends AbstractAdapter {
    @Override
    public void editExcelFile() {
        log.info("====== SourceSub2 editExcelFile");
    }

    @Override
    public void editWordFile() {
        log.info("====== SourceSub2 editWordFile");
    }
}
