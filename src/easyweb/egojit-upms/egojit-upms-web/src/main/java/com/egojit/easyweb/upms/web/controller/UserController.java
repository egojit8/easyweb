package com.egojit.easyweb.upms.web.controller;

import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.BaseWebController;
import com.egojit.easyweb.common.base.Page;
import com.egojit.easyweb.upm.service.SysUserService;
import com.egojit.easyweb.upms.model.SysUser;
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
@RequestMapping("/admin/user")
@Api(value = "用户管理", description = "用户管理")
public class UserController extends BaseWebController {

    @Autowired
    private SysUserService userService;

    @RequestMapping("/index")
    @ApiOperation(value = "用户管理首页")
    public String index(){
        return "/user/index";
    }

    @ResponseBody
    @PostMapping("/index")
    @ApiOperation(value = "用户管理接口")
    public Page<SysUser> index(HttpServletRequest request, HttpServletResponse response){
        SysUser user=new SysUser();
        Page<SysUser> pg=new Page<SysUser>(request,response);
        pg= userService.selectPage(user,pg);
        return pg;
    }

    @RequestMapping("/add")
    @ApiOperation(value = "用户添加界面")
    public String add(){
        return "/user/add";
    }
}
