package com.xgf.page.service;

import com.xgf.constant.page.CommonPage;
import com.xgf.page.bean.req.ColorCompareReqDTO;
import com.xgf.page.bean.resp.ColorCompareRespDTO;

import java.util.List;

/**
 * @author strive_day
 * @create 2023-05-28 20:34
 * @description 颜色对照表service接口
 */

public interface IColorCompareTableService {

    /**
     * 颜色对照表搜索接口
     *
     * @param reqDTO ColorCompareReqDTO
     * @param page   分页参数
     * @return List<ColorCompareRespDTO>
     */
    List<ColorCompareRespDTO> search(ColorCompareReqDTO reqDTO, CommonPage page);

}
