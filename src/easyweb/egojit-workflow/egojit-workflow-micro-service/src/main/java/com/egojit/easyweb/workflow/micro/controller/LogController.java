package com.egojit.easyweb.workflow.micro.controller;

import com.egojit.easyweb.common.base.BaseWebController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by egojit on 2017/10/29.
 */
@Controller
@RequestMapping("/test/workflow")
@Api(value = "用户管理", description = "用户管理")
public class LogController extends BaseWebController {

    @RequestMapping("/index")
    @ApiOperation(value = "日志管理首页")
    public String index() {
        return "/log/index";
    }

    @ResponseBody
    @PostMapping("/test")
    @ApiOperation(value = "日志管理接口")
    public Object index(HttpServletRequest request, HttpServletResponse response) {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        Group group = identityService.newGroup("SP2Admin");
        group.setName("管理员组");
        group.setType("manager");
        identityService.saveGroup(group);
        return group;
    }
}
