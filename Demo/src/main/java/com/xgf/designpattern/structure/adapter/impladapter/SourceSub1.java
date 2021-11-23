package com.xgf.designpattern.structure.adapter.impladapter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xgf
 * @create 2021-11-23 00:26
 * @description
 **/

@Slf4j
public class SourceSub1 extends AbstractAdapter {
    @Override
    public void editTextFile() {
        log.info("====== SourceSub1 editTextFile");
    }
}
