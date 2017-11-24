package com.egojit.easyweb.upms.web.controller;

import com.egojit.easyweb.upm.service.SysUserService;
import com.egojit.easyweb.upms.model.SysUser;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by egojit on 2017/11/18.
 */

@Controller
@RequestMapping("/admin")
public class TestController {

    @Autowired
    SysUserService userService;
    @ResponseBody
    @RequestMapping("/test")
    public Object test(){
       List<SysUser> list= userService.selectByRowBounds(null,new RowBounds(1,3));
        return list;//list;
    }
}
