package com.rushuni.sh.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String gotoIndex() {
        return "index";
    }

    @GetMapping("/setmeal.html")
    public String gotosetmeal() {
        return "setmeal";
    }

    @GetMapping("/setmeal_detail.html")
    public String gotosetmealDetail() {
        return "setmeal_detail";
    }

//    @GetMapping("/setmeal.html")
//    public String gotoMsetmeal() {
//        return "setmeal";
//    }

    @GetMapping("/orderInfo")
    public String gotoOrderInfo() {
        return "orderInfo";
    }

    @GetMapping("/orderNotice")
    public String gotoOrderNotice() {
        return "orderNotice";
    }

    @GetMapping("/orderSuccess.html")
    public String gotoOrderSuccess(){
        return "orderSuccess";
    }

    @GetMapping("/login")
    public String gotoLogin(){
        return "login.html";
    }

    @GetMapping("/member.html")
    public String gotoMember(){
        return "member.html";
    }
}

