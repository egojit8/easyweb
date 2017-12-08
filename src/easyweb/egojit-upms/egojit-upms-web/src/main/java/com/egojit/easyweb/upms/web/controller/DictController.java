package com.egojit.easyweb.upms.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.BaseWebController;
import com.egojit.easyweb.common.base.Page;
import com.egojit.easyweb.common.utils.MD5Util;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upm.service.SysDictService;
import com.egojit.easyweb.upm.service.SysUserService;
import com.egojit.easyweb.upms.common.utils.UserUtils;
import com.egojit.easyweb.upms.model.SysDict;
import com.egojit.easyweb.upms.model.SysUser;
import com.fasterxml.jackson.databind.ser.Serializers;
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
import java.util.ArrayList;
import java.util.List;
/**
 * 备注：DictController 字典管理
 * 作者：egojit
 * 日期：2017/12/04
 */
@Controller
@RequestMapping("/admin/dict")
@Api(value = "字典管理", description = "字典管理")
public class DictController extends BaseWebController {

    @Autowired
    private SysDictService service;

    @RequestMapping("/index")
    @ApiOperation(value = "字典管理首页")
    public String index(){
        return "/dict/index";
    }

    @ResponseBody
    @PostMapping("/delete")
    @ApiOperation(value = "字典管理-删除接口")
    public BaseResult delete(String ids){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"删除成功");
        List<String> idList= JSON.parseArray(ids,String.class);
        int count= service.deleteByIds(idList);
        _log.info("删除了："+count+"数据");
        return result;
    }

    @RequestMapping("/edit")
    @ApiOperation(value = "字典管理-编辑界面")
    public String add(){
        return "/dict/edit";
    }

    @ApiOperation(value = "字典管理-编辑接口")
    @PostMapping("/edit")
    @ResponseBody
    public BaseResult edit(SysDict model){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        SysUser curentUser= UserUtils.getUser();
        service.update(model,curentUser);
        return result;
    }

    @ApiOperation(value = "字典管理-详情接口")
    @PostMapping("/detail")
    @ResponseBody
    public BaseResult detail(String id){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        SysDict model=  service.selectByPrimaryKey(id);
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
                           HttpServletResponse response, SysDict model) {
        List<SysDict> list=index(request,response,model).getRows();
        return new BaseResult(BaseResultCode.SUCCESS, list);
    }





    /**
     * 获取所有字典列表
     * @param model
     * @return
     */
    @PostMapping("/index")
    @ResponseBody
    public Page<SysDict> index(HttpServletRequest request,
                                 HttpServletResponse response, SysDict model) {
        Page<SysDict> pg = new Page<SysDict>(request, response,-1);

        Example example = new Example(SysDict.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(model.getLabel())) {
            criteria.andLike("label", "%" + model.getLabel() + "%");
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
     * 获取所有机构列表
     * @return
     */
    @ApiOperation(value = "字典管理-树层级结构接口")
    @PostMapping("/tree")
    @ResponseBody
    public JSONArray tree(SysDict model) {
        JSONArray list=new JSONArray();
        List<SysDict> midList = service.getDicByParentId(model.getId());
        if(midList!=null){
            for (SysDict item:midList) {
                JSONObject obj=new JSONObject();
                obj.put("name",item.getLabel());
                obj.put("id",item.getValue());
                obj.put("pId",item.getParentId());
                obj.put("isParent",""+isHaveChild(item.getId()));
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 获取所有机构列表
     * @return
     */
    @ApiOperation(value = "字典管理-树层级结构接口")
    @PostMapping("/get_by_parentid")
    @ResponseBody
    public BaseResult getByParentId(SysDict model) {
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        result.setData(tree(model));
        return result;
    }


    /**
     * 判断是否有子字典
     * @param id
     * @return
     */
    public boolean isHaveChild(String id){
        Example example = new Example(SysDict.class);
        example.createCriteria().andEqualTo("parentId",id);
        int count= service.selectCountByExample(example);
        return  count>0?true:false;
    }


}
