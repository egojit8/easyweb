package com.egojit.easyweb.upm.service;

import com.egojit.easyweb.common.base.CurdService;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upms.dao.mapper.SysDictMapper;
import com.egojit.easyweb.upms.model.SysDict;
import com.egojit.easyweb.upms.model.SysUser;
import org.springframework.stereotype.Service;

/**
 * 备注：SysDictService 字典服务
 * 作者：egojit
 * 日期：2017/12/4
 */
@Service
public class SysDictService extends CurdService<SysDictMapper, SysDict> {

    public void update(SysDict model, SysUser user) {
        model.setDescription(model.getRemarks());
        if ("0".equals(model.getParentId())) {
            model.setType(model.getLabel());
        } else {
            SysDict pDict = mapper.selectByPrimaryKey(model.getParentId());
            model.setType(pDict.getLabel());
        }
        model.setUpdateBy(user.getId());

        if (StringUtils.isEmpty(model.getId())) {
            model.setCreateBy(user.getId());
            this.insert(model);
        } else {
            this.updateByPrimaryKeySelective(model);
        }
    }
}
