package com.xgf.convert.validate;

import com.xgf.annotation.validate.CheckNumberValueAnnotation;
import com.xgf.check.validate.CommonCheckNumberData;

/**
 * @author xgf
 * @create 2022-05-06 23:11
 * @description 通用校验数据转换
 **/

public class CommonCheckDataConvert {

    public static CommonCheckNumberData convert(CheckNumberValueAnnotation checkDataValueAnnotation) {

        CommonCheckNumberData result = new CommonCheckNumberData();
        result.setMax(checkDataValueAnnotation.max());
        result.setMin(checkDataValueAnnotation.min());
        result.setFieldDescription(checkDataValueAnnotation.fieldDescription());
        result.setScale(checkDataValueAnnotation.scale());
        result.setDealSpecialValueFlag(checkDataValueAnnotation.dealSpecialValueFlag());

        return result;
    }

}
