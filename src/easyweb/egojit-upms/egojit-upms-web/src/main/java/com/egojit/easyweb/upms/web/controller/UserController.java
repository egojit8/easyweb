package com.egojit.easyweb.upms.web.controller;

import com.alibaba.fastjson.JSON;
import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.BaseWebController;
import com.egojit.easyweb.common.base.Page;
import com.egojit.easyweb.common.models.User;
import com.egojit.easyweb.common.utils.MD5Util;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upm.service.SysUserService;
import com.egojit.easyweb.upms.model.SysUser;
import com.egojit.easyweb.upms.model.SysUserExt;
import com.egojit.easyweb.upms.sso.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

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
    @ApiOperation(value = "用户管理-首页")
    @RequiresPermissions("upms:user:read")
    public String index(){
        return "/upms/user/index";
    }

    @ResponseBody
    @PostMapping("/index")
    @ApiOperation(value = "用户管理-首页接口")
    @RequiresPermissions("upms:user:read")
    public Page<SysUser> index(HttpServletRequest request,
                                  HttpServletResponse response, SysUser user){
        Page<SysUser> pg=new Page<SysUser>(request,response);
        Example example=new Example(SysUser.class);
        Example.Criteria criteria= example.createCriteria();
        User loginUser=UserUtils.getUser();
        if(!loginUser.isAdmin()){//超级管理员无需
            criteria.andEqualTo("companyId", loginUser.getCompany().getId());
        }
        if(!StringUtils.isEmpty(user.getLoginName())) {
            criteria.andLike("loginName", "%"+user.getLoginName()+"%");
        }
        if(!StringUtils.isEmpty(user.getName())) {
            criteria.andLike("name", "%"+user.getName()+"%");
        }
        pg= userService.selectPageByExample(example,pg);
        return pg;
    }

    @ResponseBody
    @PostMapping("/delete")
    @ApiOperation(value = "用户管理接口")
    @RequiresPermissions("upms:user:delete")
    public BaseResult delete(String ids){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"删除成功");
        List<String> idList= JSON.parseArray(ids,String.class);
        int count= userService.deleteByIds(idList);
        _log.info("删除了："+count+"数据");
        return result;
    }

    @RequestMapping("/edit")
    @ApiOperation(value = "用户添加界面")
    @RequiresPermissions("upms:user:edit")
    public String add(){
        return "/upms/user/edit";
    }

    @ApiOperation(value = "用户添加界面")
    @PostMapping("/edit")
    @ResponseBody
    @RequiresPermissions("upms:user:edit")
    public BaseResult edit(SysUser user){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        User curentUser= UserUtils.getUser();
        if(StringUtils.isEmpty(user.getId())){
            user.setCreateBy(curentUser.getId());
            user.setUpdateBy(curentUser.getId());
            String ecPwd= MD5Util.shiroPwd(user.getPassword(),user.getLoginName());
            user.setPassword(ecPwd);
            userService.insert(user);
        }else {
            user.setUpdateBy(curentUser.getId());
            userService.updateByPrimaryKeySelective(user);
        }
        return result;
    }
    @RequestMapping("/detail")
    @ApiOperation(value = "用户详情-界面")
    @RequiresPermissions("upms:user:read")
    public String detail(){
        return "/upms/user/detail";
    }

    @ApiOperation(value = "用户详情")
    @PostMapping("/detail")
    @ResponseBody
    @RequiresPermissions("upms:user:read")
    public BaseResult detail(String id){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        SysUser model=  userService.selectByPrimaryKey(id);
        result.setData(model);
        return result;
    }

    @RequestMapping("/power")
    @ApiOperation(value = "用户管理-权限设置界面")
    @RequiresPermissions("upms:user:power")
    public String power(){
        return "/upms/user/power";
    }

    @ApiOperation(value = "用户管理-权限设置接口")
    @PostMapping("/power")
    @ResponseBody
    @RequiresPermissions("upms:user:power")
    public BaseResult power(String roleId,String menusIds){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
//        service.setPower(roleId,menusIds);
        return result;
    }

    @RequestMapping("/changePwd")
    @ApiOperation(value = "用户管理-密码设置")
    @RequiresPermissions("upms:user:changePwd")
    public String changePwd(){
        return "/upms/user/changePwd";
    }

    @ApiOperation(value = "用户管理-密码设置")
    @PostMapping("/changePwd")
    @ResponseBody
    @RequiresPermissions("upms:user:changePwd")
    public BaseResult changePwd(String userIds,String password){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        SysUser user=new SysUser();
        if(StringUtils.isEmpty(password)){
            result=new BaseResult(BaseResultCode.ARGUMENT,"密码不能为空！");
        }
        if(StringUtils.isNotEmpty(userIds)){
            String[] ids= userIds.split(",");
            boolean isOk= userService.changePwds(ids,password);
            if(!isOk){
                result=new BaseResult(BaseResultCode.EXCEPTION,"修改失败！");
            }
        }else{
            result=new BaseResult(BaseResultCode.EXCEPTION,"请选择用户！");
        }
        return result;
    }

}
