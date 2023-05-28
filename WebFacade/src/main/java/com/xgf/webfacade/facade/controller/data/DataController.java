package com.xgf.webfacade.facade.controller.data;

import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author strive_day
 * @create 2023-05-28 22:22
 * @description 数据页面展示 controller
 */

@Controller
@RequestMapping(path = "/data",method = RequestMethod.GET)
public class DataController {


    @RequestMapping(path="/colorCompareTable",method = RequestMethod.GET)
    public String toColorCompareTable(){
        return "/pages/data/colorCompareTable";
    }



}
