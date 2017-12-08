package com.egojit.easyweb.upm.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egojit.easyweb.common.base.CurdService;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upms.dao.mapper.SysDictMapper;
import com.egojit.easyweb.upms.dao.mapper.SysMenuMapper;
import com.egojit.easyweb.upms.model.SysDict;
import com.egojit.easyweb.upms.model.SysMenu;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 备注：SysMenuService
 * 作者：egojit
 * 日期：2017/12/8
 */
@Service
public class SysMenuService extends CurdService<SysMenuMapper, SysMenu> {

    /**
     * 返回菜单
     * @param model
     * @param getAll 是否返回所有菜单
     * @return
     */
    public JSONArray getMenus(SysMenu model,boolean getAll){
        Example example = new Example(SysMenu.class);
        Example.Criteria criteria = example.createCriteria();
        JSONArray list=new JSONArray();
        if (StringUtils.isEmpty(model.getId())) {
            criteria.andEqualTo("parentId","0");//获取公司
        }else {
            criteria.andEqualTo("parentId",model.getId());
        }
        List<SysMenu> midList = mapper.selectByExample(example);
        if(midList!=null){
            for (SysMenu item:midList) {
                JSONObject obj=new JSONObject();
                obj.put("name",item.getName());
                obj.put("id",item.getId());
                obj.put("pId",item.getParentId());
                boolean isParent=isHaveChild(item.getId());
                obj.put("isParent",""+isParent);
                if(getAll&&isParent) {
                    obj.put("children", getMenus(item,getAll));
                }
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 判断是否有子菜单
     * @param id
     * @return
     */
    public boolean isHaveChild(String id){
        Example example = new Example(SysMenu.class);
        example.createCriteria().andEqualTo("parentId",id);
        int count= mapper.selectCountByExample(example);
        return  count>0?true:false;
    }
}
