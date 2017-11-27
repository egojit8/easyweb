package com.egojit.easyweb.log.web.controller;

import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.BaseWebController;
import com.egojit.easyweb.common.base.Page;
import com.egojit.easyweb.log.model.SysLog;
import com.egojit.easyweb.log.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by egojit on 2017/10/29.
 */
@Controller
@RequestMapping("/admin/log")
@Api(value = "用户管理", description = "用户管理")
public class LogController extends BaseWebController {

    @Autowired
    private SysLogService sysLogService;

    @RequestMapping("/index")
    @ApiOperation(value = "日志管理首页")
    public String index(){
        return "/log/index";
    }

    @ResponseBody
    @PostMapping("/index")
    @ApiOperation(value = "日志管理接口")
    public Page<SysLog> index(HttpServletRequest request, HttpServletResponse response){
        SysLog user=new SysLog();
        Page<SysLog> pg=new Page<SysLog>(request,response);
        pg= sysLogService.selectPage(user,pg);
        return pg;
    }
}
