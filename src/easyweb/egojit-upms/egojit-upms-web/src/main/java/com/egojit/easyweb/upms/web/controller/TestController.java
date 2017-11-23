package com.egojit.easyweb.upms.web.controller;

import com.egojit.easyweb.upm.service.UserService;
import com.egojit.easyweb.upms.dao.mapper.UserMapper;
import com.egojit.easyweb.upms.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by egojit on 2017/11/18.
 */

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserService userService;
    @ResponseBody
    @RequestMapping("/test")
    public Object test(){
       List<User> list= userService.selectByRowBounds(null,new RowBounds(1,3));
        return list;//list;
    }
}
