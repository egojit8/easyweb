/*
 * Copyright(c) 2017 千里授渔 All rights reserved.
 * 关注微信公众号【千里授渔】
 */

package com.egojit.easyweb.upm.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.CurdService;
import com.egojit.easyweb.common.base.Page;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upms.model.SysOffice;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.egojit.easyweb.upms.dao.mapper.SysCompanyDicMapper;
import com.egojit.easyweb.upms.model.SysCompanyDic;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description：{className}
 * Auther：高露
 * Q Q:408365330
 * Company: 鼎斗信息技术有限公司
 * Time:2018-4-25
 */
@Service
@Transactional
public class SysCompanyDicService extends CurdService<SysCompanyDicMapper, SysCompanyDic> {

    @Autowired
    SysDictService dictService;


    /**
     * 获取企业所有字典
     * @param companyId
     * @return
     */
    public  List<Map> selectAllByCompany(String companyId){
        return this.mapper.selectAllByCompany(companyId);
    }
    /**
     * 添加企业数据
     * @param companyDics
     * @return
     */
    public boolean add(List<SysCompanyDic> companyDics,String companyId){
        Example example=new Example(SysCompanyDic.class);
        example.createCriteria().andEqualTo("companyId",companyId);
        this.mapper.deleteByExample(example);
        int count=0;
        if(companyDics!=null){
            for (SysCompanyDic companyDic:companyDics){
                count+=this.insert(companyDic);
            }
        }
        return  count>0?true:false;
    }
    /**
     * 获取企业字典数据
     * @param parm
     * @return
     */
   public  Page<Map> selectByCompany(Map parm, Page<Map> page){
       if(!page.isDisabled()) {
           long count= this.mapper.selectCountByCompany(parm);
           page.setRecords(count);
           PageHelper.startPage(page.getPage(), page.getPageSize());
       }

       List<Map> list=this.mapper.selectByCompany(parm);
       return page.setList(list);
   }

    /**
     * 获取子节点
     * @param list
     * @param pId
     * @return
     */
    private JSONArray getChilds(List<Map> list,String pId){
        JSONArray array=new JSONArray();
        for (Map item:list){
            if(pId.equals(item.get("parentId").toString())){
                JSONObject obj=new JSONObject();
                obj.put("name",item.get("label"));
                obj.put("id",item.get("id"));
                obj.put("dicId",item.get("dicId"));
                boolean isHaveChild=isHaveChild(list,item.get("dicId").toString());
                obj.put("isParent",""+isHaveChild);
                if(isHaveChild){
                    obj.put("children",getChilds(list,item.get("dicId").toString()));
                }
                array.add(obj);
            }
        }
        return array;
    }

    /**
     * 判断是否有子部门
     * @param id
     * @return
     */
    public boolean isHaveChild(List<Map> list,String id){
        for (Map item:list){
            if(id.equals(item.get("parentId"))){
                return true;
            }
        }
        return  false;
    }


    /**
     * 返回企业已经选择的字典
     * @param companyId
     * @return
     */
    public JSONArray getCompanyDicTree(String companyId) {
        Map parm=new HashMap();
        parm.put("companyId",companyId);
        List<Map> list = mapper.selectByCompany(parm);
        JSONArray array= getChilds(list,"0");
        return array;
    }



    /**
     * 返回企业已经选择的字典id
     * @param companyId
     * @return
     */
    public List<String> getCompanyDicIds(String companyId) {
        Example example=new Example(SysCompanyDic.class);
        example.createCriteria().andEqualTo("companyId",companyId);
        List<SysCompanyDic> list = mapper.selectByExample(example);
        List<String> ids=new ArrayList<>();
        if(list!=null){
            for (SysCompanyDic dic:list){
                ids.add(dic.getDicId());
            }
        }
        return ids;
    }




}
