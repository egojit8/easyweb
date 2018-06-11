package com.egojit.easyweb.upms.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.BaseWebController;
import com.egojit.easyweb.common.base.Page;
import com.egojit.easyweb.common.models.User;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upm.service.SysCompanyDicService;
import com.egojit.easyweb.upm.service.SysDictService;
import com.egojit.easyweb.upms.model.SysCompanyDic;
import com.egojit.easyweb.upms.model.SysDict;
import com.egojit.easyweb.upms.model.SysOffice;
import com.egojit.easyweb.upms.model.SysRole;
import com.egojit.easyweb.upms.sso.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 备注：数据维护
 * 作者：高露
 * Q Q:408365330
 * 日期：2018/05/26
 */
@Controller
@RequestMapping("/admin/companyDic")
@Api(value = "数据维护", description = "数据维护")
public class CompanyDicController  extends BaseWebController {

    @Autowired
    SysCompanyDicService service;
    @Autowired
    SysDictService dictService;

    @RequestMapping("/index")
    @ApiOperation(value = "数据维护-首页")
    public String index(){
        return "/upms/companyDic/index";
    }

    /**
     * 获取数据列表
     * @return
     */
    @PostMapping("/index")
    @ResponseBody
    @ApiOperation(value = "数据维护-接口")
    public Page<Map> index(HttpServletRequest request,
                               HttpServletResponse response, String label,String value,String companyId,String parentId) {
        Page<Map> pg = new Page<Map>(request, response,-1);
        Map pam=new HashMap();
        if (!StringUtils.isEmpty(label)) {
            pam.put("label",label);
        }
        if (!StringUtils.isEmpty(value)) {
            pam.put("value",value);
        }
        pam.put("companyId",companyId);
        pam.put("parentId",parentId);
        pg = service.selectByCompany(pam, pg);
        return pg;
    }

    @ResponseBody
    @PostMapping("/delete")
    @ApiOperation(value = "数据维护-删除接口")
    public BaseResult delete(String ids){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"删除成功");
        List<String> idList= JSON.parseArray(ids,String.class);
        int count= service.deleteByIds(idList);
        _log.info("删除了："+count+"数据");
        return result;
    }
    @RequestMapping("/edit")
    @ApiOperation(value = "数据维护-编辑界面")
    public String add(){
        return "/upms/companyDic/edit";
    }

    @ApiOperation(value = "数据维护-编辑接口")
    @PostMapping("/edit")
    @ResponseBody
    public BaseResult edit(String companyId,String dicIds){
        BaseResult result=new BaseResult(BaseResultCode.EXCEPTION,"添加失败！");
        if(StringUtils.isEmpty(companyId)){
            result=new BaseResult(BaseResultCode.ARGUMENT,"请选择企业！");
            return result;
        }
        if(StringUtils.isEmpty(dicIds)){
            result=new BaseResult(BaseResultCode.ARGUMENT,"请选择字典数据！");
            return result;
        }
        List<String> ids=JSON.parseArray(dicIds,String.class);
        List<SysCompanyDic> list=new ArrayList<>();
        for (String id:ids){
            User user=UserUtils.getUser();
            SysCompanyDic obj=new SysCompanyDic();
            obj.setCompanyId(companyId);
            obj.setDicId(id);
            obj.setCreateBy(user.getId());
            obj.setUpdateBy(user.getId());
            list.add(obj);
        }
        boolean isResult= service.add(list,companyId);
        if(isResult){
            result=new BaseResult(BaseResultCode.SUCCESS,"添加成功！");
        }
        return result;
    }

    @ResponseBody
    @GetMapping("/tree")
    @ApiOperation(value = "数据维护-根据企业id获取树结构")
    public JSONArray tree(String companyId){
        JSONArray array= service.getCompanyDicTree(companyId);
        return array;
    }

    @ResponseBody
    @GetMapping("/canSelectTree")
    @ApiOperation(value = "数据维护-企业还没选择的字典树")
    public JSONArray canSelectTree(String companyId){
        Example example=new Example(SysDict.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("isComm",false);
        JSONArray array= dictService.getAllTree(example,service.getCompanyDicIds(companyId));
        return array;
    }
}
