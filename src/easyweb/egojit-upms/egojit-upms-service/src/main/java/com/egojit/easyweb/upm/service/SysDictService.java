package com.egojit.easyweb.upm.service;

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

/**
 * 备注：SysDictService 字典服务
 * 作者：egojit
 * 日期：2017/12/4
 */
@Service
public class SysDictService extends CurdService<SysDictMapper, SysDict> {

    public String update(SysDict model, SysUser user) {
        String result = "成功！";
        model.setDescription(model.getRemarks());
        if ("0".equals(model.getParentId())) {
            model.setType(model.getValue());
        } else {
            SysDict pDict = mapper.selectByPrimaryKey(model.getParentId());
            model.setType(pDict.getValue());
        }
        model.setUpdateBy(user.getId());
        if (StringUtils.isEmpty(model.getId())) {
            model.setCreateBy(user.getId());
            model.setId(model.getLabel());
            if (mapper.existsWithPrimaryKey(model)) {
                result = "该字典值已经存在！";
            } else {
                this.insert(model);
            }
        } else {
            model.setId(model.getLabel());
            Example example = new Example(SysDict.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andNotEqualTo("id", model.getId());
            int count = mapper.selectCountByExample(example);
            if (count > 1) {
                result = "该字典值已经存在！";
            } else {
                this.updateByPrimaryKeySelective(model);
            }
        }
        return result;
    }

    /**
     * 获取子字典
     * @param parentId
     * @return
     */
    public List<SysDict> getDicByParentId(String parentId) {
        Example example = new Example(SysDict.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId",parentId);
        return mapper.selectByExample(example);
    }

}
