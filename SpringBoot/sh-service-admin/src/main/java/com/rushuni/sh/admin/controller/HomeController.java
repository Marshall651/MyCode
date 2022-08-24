package com.rushuni.sh.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Marshall
 * @date 2022/7/22
 */
@Controller
public class HomeController {
    @GetMapping("/")
    public String gotoIndex() {
        return "main";
    }

    @GetMapping("checkitem")
    public String gotoCheckItem() {
        return "checkitem";
    }

    @GetMapping("checkgroup")
    public String gotoCheckgroup() {
        return "checkgroup";
    }

    @GetMapping("setmeal")
    public String gotoSetmeal() {
        return "setmeal";
    }

    @GetMapping("ordersetting")
    public String gotoordersetting() {
        return "ordersetting";
    }
}
