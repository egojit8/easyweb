package com.egojit.easyweb.upms.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.BaseWebController;
import com.egojit.easyweb.common.base.Page;
import com.egojit.easyweb.common.models.User;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.common.utils.UserUtil;
import com.egojit.easyweb.upm.service.SysOfficeService;
import com.egojit.easyweb.upm.service.SysRoleService;
import com.egojit.easyweb.upm.service.SysUserService;
import com.egojit.easyweb.upms.model.SysOffice;
import com.egojit.easyweb.upms.model.SysRole;
import com.egojit.easyweb.upms.model.SysUser;
import com.egojit.easyweb.upms.sso.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * Description：系统安全认证实现类
 * Auther：高露
 * Q Q:408365330
 * Company: 鼎斗信息技术有限公司
 * Time:2018-4-25
 */
@Controller
@RequestMapping("/admin/role")
@Api(value = "角色管理", description = "角色管理")
public class RoleController extends BaseWebController {

    @Autowired
    private SysRoleService service;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysOfficeService officeService;
    @RequestMapping("/index")
    @ApiOperation(value = "角色管理首页")
    public String index(){
        User curentUser= UserUtils.getUser();
        if(curentUser.isAdmin()){
            return "/role/index";
        }else{
            return "/role/indexCompany";
        }

    }

    /**
     * 获取所有角色列表
     * @param model
     * @return
     */
    @PostMapping("/index")
    @ResponseBody
    public Page<SysRole> index(HttpServletRequest request,
                               HttpServletResponse response, SysRole model) {
        Page<SysRole> pg = new Page<SysRole>(request, response,-1);

        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(model.getName())) {
            criteria.andLike("name", "%" + model.getName() + "%");
        }
        User curentUser= UserUtils.getUser();
        if(curentUser.isAdmin()){
            if (!StringUtils.isEmpty(model.getOfficeId())) {
                criteria.andEqualTo("officeId", model.getOfficeId());
            }
        }else{
            criteria.andEqualTo("officeId",curentUser.getCompany().getId());
        }
        pg = service.selectPageByExample(example, pg);
        return pg;
    }

    @ResponseBody
    @PostMapping("/delete")
    @ApiOperation(value = "角色管理-删除接口")
    public BaseResult delete(String ids){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"删除成功");
        List<String> idList= JSON.parseArray(ids,String.class);
        int count= service.deleteByIds(idList);
        _log.info("删除了："+count+"数据");
        return result;
    }

    @RequestMapping("/edit")
    @ApiOperation(value = "角色管理-编辑界面")
    public String add(){
        return "/role/edit";
    }

    @ApiOperation(value = "角色管理-编辑接口")
    @PostMapping("/edit")
    @ResponseBody
    public BaseResult edit(SysRole model){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        User curentUser= UserUtils.getUser();
        if(StringUtils.isEmpty(model.getId())){
            model.setCreateBy(curentUser.getId());
            model.setUpdateBy(curentUser.getId());
            service.insert(model);
        }else {
            model.setUpdateBy(curentUser.getId());
            service.updateByPrimaryKeySelective(model);
        }
        return result;
    }

    @ApiOperation(value = "角色管理-详情接口")
    @PostMapping("/detail")
    @ResponseBody
    public BaseResult detail(String id){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        SysRole model=  service.selectByPrimaryKey(id);
        JSONObject object=(JSONObject) JSON.toJSON(model);
        List<SysOffice> list= officeService.getAllParents(new SysOffice(model.getOfficeId()));
        String name=getOfficeNames(list,new SysOffice(model.getOfficeId()));
        if(StringUtils.isNotEmpty(name)){
            name=name.substring(1,name.length());
        }
        object.put("officeName",name);
        result.setData(object);
        return result;
    }


    /**
     * 获取所属机构的名称，拼接好的
     * @param list
     * @param office
     * @return
     */
    private String getOfficeNames(List<SysOffice> list,SysOffice office)
    {
        String names="";
        if(list!=null) {
            for (SysOffice o : list) {
                if (o.getId().equals(office.getId())) {
                    names = getOfficeNames(list, new SysOffice(o.getParentId())) + "," + o.getName();
                }
            }
        }
        return names;
    }


    @RequestMapping("/power")
    @ApiOperation(value = "角色管理-权限设置界面")
    public String power(){
        return "/role/power";
    }

    @ApiOperation(value = "角色管理-权限设置接口")
    @PostMapping("/power")
    @ResponseBody
    public BaseResult power(String roleId,String menusIds){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        service.setPower(roleId,menusIds);
        return result;
    }
    @RequestMapping("/selectRole")
    @ApiOperation(value = "角色选择")
    public String selectRole(){
        return "/role/selectRole";
    }

    /**
     * 角色选择
     * @param model
     * @return
     */
    @PostMapping("/selectRole")
    @ResponseBody
    @ApiOperation(value = "角色选择-接口")
    public Page<SysRole> selectRole(HttpServletRequest request,
                               HttpServletResponse response,String userId, SysRole model) {
        Page<SysRole> pg = new Page<SysRole>(request, response,-1);

        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(model.getName())) {
            criteria.andLike("name", "%" + model.getName() + "%");
        }
        if(StringUtils.isNotEmpty(userId)){//查询当前用户可以设置的角色，只能是他所长公司的角色
            SysUser user= sysUserService.selectByPrimaryKey(userId);
            if(user!=null){
                model.setOfficeId(user.getCompanyId());
            }
        }
        if (!StringUtils.isEmpty(model.getOfficeId())) {
            criteria.andEqualTo("officeId", model.getOfficeId());
        }
        pg = service.selectPageByExample(example, pg);

        SysUser user=new SysUser();
        user.setId(userId);
        List<SysRole> roles= service.getRoleByUser(user);
        return pg;
    }


    /**
     * 获取当前用户的角色
     * @return
     */
    @PostMapping("/getRoleByUser")
    @ResponseBody
    @ApiOperation(value = "获取当前用户的角色")
    public BaseResult getRoleByUser(HttpServletRequest request,
                                    HttpServletResponse response,String userId) {
        BaseResult result=new BaseResult(BaseResultCode.EXCEPTION,"未知的服务器异常！");
        SysUser user=new SysUser();
        user.setId(userId);
        List<SysRole> roles= service.getRoleByUser(user);
        result=new BaseResult(BaseResultCode.SUCCESS,roles);
        return result;
    }


    /**
     * 角色选择
     * @return
     */
    @PostMapping("/setrole")
    @ResponseBody
    @ApiOperation(value = "设置角色-接口")
    public BaseResult setRole(HttpServletRequest request,
                                    HttpServletResponse response,String userId, String roleIds) {
        BaseResult result=new BaseResult(BaseResultCode.EXCEPTION,"设置失败！");

        if(StringUtils.isNotEmpty(roleIds)){
           List<String> roles=JSON.parseArray(roleIds,String.class);
            boolean isOk= service.setUserRoles(userId,roles);
            if(isOk)
                result=new BaseResult(BaseResultCode.SUCCESS,"角色设置成功！");
        }else{
            result=new BaseResult(BaseResultCode.EXCEPTION,"请选择角色！");
        }
        return result;
    }

}
