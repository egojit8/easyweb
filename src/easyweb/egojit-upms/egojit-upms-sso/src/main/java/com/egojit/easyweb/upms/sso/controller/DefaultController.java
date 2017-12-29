package com.egojit.easyweb.upms.sso.controller;

import com.egojit.easyweb.common.base.BaseWebController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController extends BaseWebController {

    @GetMapping("/")
    public String index(){
       return "redirect:/admin/index";
    }
}
