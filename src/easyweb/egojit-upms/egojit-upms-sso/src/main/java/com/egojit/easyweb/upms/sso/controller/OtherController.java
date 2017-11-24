package com.egojit.easyweb.upms.sso.controller;

import com.egojit.easyweb.common.base.BaseWebController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by egojit on 2017/11/24.
 */
@Controller
public class OtherController extends BaseWebController{

    /**
     * 错误界面
     * @return
     */
    @GetMapping("/500")
    public String error(){
        return "500";
    }
    /**
     * 404界面
     * @return
     */
    @GetMapping("/404")
    public String notfind(){
        return "404";
    }
}
