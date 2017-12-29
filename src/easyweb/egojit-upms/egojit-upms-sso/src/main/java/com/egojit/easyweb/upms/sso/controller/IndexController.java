package com.egojit.easyweb.upms.sso.controller;

import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.BaseWebController;
import com.egojit.easyweb.upm.service.SysUserService;
import com.egojit.easyweb.upms.model.SysMenu;
import com.egojit.easyweb.upms.sso.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by egojit on 2017/10/26.
 */
@Controller
@RequestMapping("/admin")
@Api(value = "首页", description = "首页管理")
public class IndexController extends BaseWebController {

    @Autowired
    SysUserService userService;

    @RequestMapping("/index")
    @ApiOperation(value = "首页界面")
    public String index() {
        return "index";
    }

    @RequestMapping("/logout")
    @ApiOperation(value = "退出登录")
    public String logout() {
        UserUtils.clearCache();
        UserUtils.getSubject().logout();
        return "redirect:/admin/index";
    }

    @ResponseBody
    @RequestMapping("/menus")
    @ApiOperation(value = "菜单接口")
    public Object menus() {
        List<SysMenu> menuList = UserUtils.getMenuList();
        return new BaseResult(BaseResultCode.SUCCESS, menuList);
    }

    @RequestMapping("/default")
    @ApiOperation(value = "主页")
    public String main() {
        return "default";
    }
}
