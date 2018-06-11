package com.egojit.easyweb.upm.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.CurdService;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upms.dao.mapper.SysDictMapper;
import com.egojit.easyweb.upms.model.SysDict;
import com.egojit.easyweb.upms.model.SysUser;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * 备注：SysDictService 字典服务
 * 作者：egojit
 * 日期：2017/12/4
 */
@Service
public class SysDictService extends CurdService<SysDictMapper, SysDict> {

    public String update(SysDict model, String userId) {
        String result = "成功！";
        model.setDescription(model.getRemarks());
        if ("0".equals(model.getParentId())) {
            model.setType(model.getValue());
        } else {
            SysDict pDict = mapper.selectByPrimaryKey(model.getParentId());
            model.setType(pDict.getValue());
        }
        model.setUpdateBy(userId);
        if (StringUtils.isEmpty(model.getId())) {
            model.setCreateBy(userId);
            model.setId(model.getValue());
            if (mapper.existsWithPrimaryKey(model)) {
                result = "该字典值已经存在！";
            } else {
                this.insert(model);
            }
        } else {
            Example example = new Example(SysDict.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andNotEqualTo("id", model.getId());
            this.updateByPrimaryKeySelective(model);
        }
        return result;
    }

    /**
     * 获取子字典
     *
     * @param parentId
     * @return
     */
    public List<SysDict> getDicByParentId(String parentId) {
        Example example = new Example(SysDict.class);
        Example.Criteria criteria = example.createCriteria();
        if (parentId == null)
            parentId = "0";
        criteria.andEqualTo("parentId", parentId);
        return mapper.selectByExample(example);
    }

    /**
     * 根据编号获取字典
     *
     * @param code
     * @return
     */
    public SysDict getDicByCode(String code) {
//        Example example = new Example(SysDict.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("value", code);
//        List<SysDict> dicts = mapper.selectByExample(example);
//        if (dicts != null && dicts.size() > 0) {
//            return dicts.get(0);
//        } else {
//            return null;
//        }
        return mapper.selectByPrimaryKey(code);
    }

    /**
     * 根据example获取字典树
     * @return
     */
    public JSONArray getAllTree(Example example,List<String> selectIds) {
        List<SysDict> list= mapper.selectByExample(example);
        return getChilds(list,"0",selectIds);
    }

    /**
     * 获取子节点
     * @param list
     * @param pId
     * @return
     */
    private JSONArray getChilds(List<SysDict> list, String pId,List<String> selectIds){
        JSONArray array=new JSONArray();
        for (SysDict item:list){
            if(pId.equals(item.getParentId())){
                JSONObject obj=new JSONObject();
                obj.put("name",item.getLabel());
                obj.put("id",item.getId());
                boolean isHaveChild=isHaveChild(list,item.getId());
                obj.put("isParent",""+isHaveChild);
                obj.put("checked",selectIds.contains(item.getId())+"");
                if(isHaveChild){
                    obj.put("children",getChilds(list,item.getId(),selectIds));
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
    public boolean isHaveChild(List<SysDict> list,String id){
        for (SysDict item:list){
            if(id.equals(item.getParentId())){
                return true;
            }
        }
        return  false;
    }
}
