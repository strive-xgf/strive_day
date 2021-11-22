package com.xgf.designpattern.create.prototype;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xgf
 * @create 2021-11-22 14:36
 * @description 手机品牌
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneBrand {
    private String brandUuid;
    private String brandName;

}
