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
import com.egojit.easyweb.upm.service.SysOfficeService;
import com.egojit.easyweb.upms.common.utils.UserUtils;
import com.egojit.easyweb.upms.model.SysOffice;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 备注：CompanyController 公司
 * 作者：egojit
 * 日期：2017/11/28
 */
@Controller
@RequestMapping("/admin/office")
@Api(value = "机构管理", description = "机构管理")
public class OfficeController extends BaseWebController{

    @Autowired
    private SysOfficeService service;

    @RequestMapping("/index")
    @ApiOperation(value = "机构管理首页")
    public String index(){
        return "/office/index";
    }

    @ResponseBody
    @PostMapping("/delete")
    @ApiOperation(value = "机构管理-删除接口")
    public BaseResult delete(String ids){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"删除成功");
        List<String> idList= JSON.parseArray(ids,String.class);
        int count= service.deleteByIds(idList);
        _log.info("删除了："+count+"数据");
        return result;
    }

    @RequestMapping("/edit")
    @ApiOperation(value = "机构管理-编辑界面")
    public String add(){
        return "/office/edit";
    }

    @ApiOperation(value = "机构管理-编辑接口")
    @PostMapping("/edit")
    @ResponseBody
    public BaseResult edit(SysOffice model){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        SysUser curentUser= UserUtils.getUser();
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

    @ApiOperation(value = "机构管理-详情接口")
    @PostMapping("/detail")
    @ResponseBody
    public BaseResult detail(String id){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        SysOffice model=  service.selectByPrimaryKey(id);
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
                            HttpServletResponse response, SysOffice model) {
        List<SysOffice> list=index(request,response,model).getRows();
        return new BaseResult(BaseResultCode.SUCCESS, list);
    }

    /**
     * 获取当前用户机构信息
     * @return
     */
    @PostMapping("/getofficebyuser")
    @ResponseBody
    public BaseResult getCurentOffices() {
        SysUser user= UserUtils.getUser();
        Example example=new Example(SysOffice.class);
        List<SysOffice> offices=new ArrayList<SysOffice>();
        if(user.isAdmin()){
            offices=service.selectAll();
        }else{
            String companyId=user.getOfficeId();
            SysOffice currentOffice= service.selectByPrimaryKey(companyId);
            if(currentOffice!=null){
               String parentIds= currentOffice.getParentIds();
                Example.Criteria criteria= example.createCriteria();
                String inCondition=parentIds.substring(0,parentIds.length()-1);
                criteria.andCondition(" id in ("+inCondition+")");
                offices=service.selectByExample(example);
            }
        }
        return new BaseResult(BaseResultCode.SUCCESS, offices);
    }



    /**
     * 获取所有机构列表
     * @param model
     * @return
     */
    @PostMapping("/index")
    @ResponseBody
    public Page<SysOffice> index(HttpServletRequest request,
                           HttpServletResponse response, SysOffice model) {
        Page<SysOffice> pg = new Page<SysOffice>(request, response,-1);

        Example example = new Example(SysOffice.class);
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
     * 获取所有机构列表
     * @return
     */
    @ApiOperation(value = "机构管理-树层级结构接口")
    @PostMapping("/tree")
    @ResponseBody
    public JSONArray tree(SysOffice model) {
        Example example = new Example(SysOffice.class);
        Example.Criteria criteria = example.createCriteria();
        JSONArray list=new JSONArray();
        if (!StringUtils.isEmpty(model.getName())) {
            criteria.andLike("name", "%" + model.getName() + "%");
        }

        if (StringUtils.isEmpty(model.getId())) {
            criteria.andEqualTo("parentId","0");//获取公司
        }else {
            criteria.andEqualTo("parentId",model.getId());
        }
        List<SysOffice> midList = service.selectByExample(example);
        if(midList!=null){
            for (SysOffice item:midList) {
                JSONObject  obj=new JSONObject();
                obj.put("name",item.getName());
                obj.put("id",item.getId());
                obj.put("pId",item.getParentId());
                obj.put("pIds",item.getParentIds());
                obj.put("isParent",""+isHaveChild(item.getId()));
                list.add(obj);
            }
        }
        return list;
    }


    /**
     * 判断是否有子部门
     * @param id
     * @return
     */
    public boolean isHaveChild(String id){
        Example example = new Example(SysOffice.class);
        example.createCriteria().andEqualTo("parentId",id);
        int count= service.selectCountByExample(example);
        return  count>0?true:false;
    }



}
