package com.egojit.easyweb.upms.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.BaseWebController;
import com.egojit.easyweb.common.base.Page;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upm.service.SysMenuService;
import com.egojit.easyweb.upms.common.utils.UserUtils;
import com.egojit.easyweb.upms.model.SysMenu;
import com.egojit.easyweb.upms.model.SysUser;
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
 * 备注：MenuController 菜单
 * 作者：egojit
 * 日期：2017/12/8
 */
@Controller
@RequestMapping("/admin/menu")
@Api(value = "菜单管理", description = "菜单管理")
public class MenuController  extends BaseWebController{
    @Autowired
    private SysMenuService service;

    @RequestMapping("/index")
    @ApiOperation(value = "菜单管理首页")
    public String index(){
        return "/menu/index";
    }

    @ResponseBody
    @PostMapping("/delete")
    @ApiOperation(value = "菜单管理-删除接口")
    public BaseResult delete(String ids){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"删除成功");
        List<String> idList= JSON.parseArray(ids,String.class);
        int count= service.deleteByIds(idList);
        _log.info("删除了："+count+"数据");
        return result;
    }

    @RequestMapping("/edit")
    @ApiOperation(value = "菜单管理-编辑界面")
    public String add(){
        return "/menu/edit";
    }

    @ApiOperation(value = "菜单管理-编辑接口")
    @PostMapping("/edit")
    @ResponseBody
    public BaseResult edit(SysMenu model){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        SysUser curentUser= UserUtils.getUser();
        if(StringUtils.isEmpty(model.getId())){
            model.setCreateBy(curentUser.getId());
            model.setUpdateBy(curentUser.getId());
            model.setIsShow("1");
            service.insert(model);
        }else {
            model.setUpdateBy(curentUser.getId());
            service.updateByPrimaryKeySelective(model);
        }
        return result;
    }

    @ApiOperation(value = "菜单管理-详情接口")
    @PostMapping("/detail")
    @ResponseBody
    public BaseResult detail(String id){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        SysMenu model=  service.selectByPrimaryKey(id);
        result.setData(model);
        return result;
    }


    /**
     * 获取所有机构列表
     * @param model
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public BaseResult list(HttpServletRequest request,
                           HttpServletResponse response, SysMenu model) {
        List<SysMenu> list=index(request,response,model).getRows();
        return new BaseResult(BaseResultCode.SUCCESS, list);
    }


    /**
     * 获取所有菜单列表
     * @param model
     * @return
     */
    @PostMapping("/index")
    @ResponseBody
    public Page<SysMenu> index(HttpServletRequest request,
                               HttpServletResponse response, SysMenu model) {
        Page<SysMenu> pg = new Page<SysMenu>(request, response,-1);

        Example example = new Example(SysMenu.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(model.getName())) {
            criteria.andLike("name", "%" + model.getName() + "%");
        }
        if (StringUtils.isEmpty(model.getParentId())) {
            criteria.andEqualTo("parentId","0");//获取公司
        }else {
            criteria.andEqualTo("parentId",model.getParentId());
        }
        pg = service.selectPageByExample(example, pg);
        return pg;
    }


    /**
     * 根据父资源获取直系资源
     * @return
     */
    @ApiOperation(value = "菜单管理-根据父资源获取直系资源")
    @PostMapping("/tree")
    @ResponseBody
    public JSONArray tree(SysMenu model) {
        return service.getMenus(model,false);
    }


    /**
     * 根据父资源获取下属所有资源
     * @return
     */
    @ApiOperation(value = "菜单管理-根据父资源获取下属所有资源")
    @PostMapping("/alltree")
    @ResponseBody
    public JSONArray allTree(SysMenu model) {
        return service.getMenus(model,true);
    }






}
