package com.xgf.webfacade.facade.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author strive_day
 * @create 2023-04-08 18:53
 * @description 页面跳转 controller
 */

@Controller
@RequestMapping(path = "/page",method = RequestMethod.GET)
public class ToPageController {

    /**
     * 跳转到主页（Thymeleaf开箱即用），跳转 /templates/下路径
     * @return 主页路径
     */
    @RequestMapping(path="/toMain",method = RequestMethod.GET)
    public String toMain(){
        return "/pages/home/main";
    }

    @RequestMapping(path="/toFooter",method = RequestMethod.GET)
    public String toFooter(){
        return "/pages/home/footer";
    }

    /**
     * 欢迎时钟页面
     */
    @GetMapping(path = "/toWelcome")
    public String toWelcome() {
        // 通过重定向，跳转到static目录下的时钟欢迎页面
        return "redirect:../welcome.html";
    }



}
