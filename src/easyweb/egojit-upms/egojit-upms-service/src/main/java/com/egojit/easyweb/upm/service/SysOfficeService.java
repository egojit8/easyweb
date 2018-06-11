package com.egojit.easyweb.upm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.egojit.easyweb.common.base.BaseService;
import com.egojit.easyweb.common.base.CurdService;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upms.dao.mapper.SysOfficeMapper;
import com.egojit.easyweb.upms.dao.mapper.SysRoleMapper;
import com.egojit.easyweb.upms.dao.mapper.SysRoleMenuMapper;
import com.egojit.easyweb.upms.dao.mapper.SysUserMapper;
import com.egojit.easyweb.upms.model.SysOffice;
import com.egojit.easyweb.upms.model.SysRole;
import com.egojit.easyweb.upms.model.SysRoleMenu;
import com.egojit.easyweb.upms.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 备注：SysOfficeService 机构
 * 作者：egojit
 * 日期：2017/11/28
 */
@Service
@Transactional
public class SysOfficeService extends CurdService<SysOfficeMapper, SysOffice> {


    @Autowired
    SysUserMapper userMapper;

    /**
     * 重写-根据id获取机构
     * @param o 机构编码
     * @return
     */
    @Override
    public SysOffice selectByPrimaryKey(Object o) {
        SysOffice office = this.mapper.selectByPrimaryKey(o);
        if (office != null) {
            String pNames = getParentOffice(office.getParentId());
            office.setParentOfficeName(pNames);
            SysUser sysUser = userMapper.selectByPrimaryKey(office.getCreateBy());
            if (sysUser != null) {
                office.setCreateByName(sysUser.getName());
            }
        }
        return office;
    }

    /**
     * 根据机构id获取机构所有上级机构名称，逗号隔开
     * @param officeId 机构id
     * @return
     */
    public String getParentOffice(String officeId) {
        String officeNames = "";
        SysOffice office = this.mapper.selectByPrimaryKey(officeId);
        if (office != null) {
            officeNames = office.getName();
            String pName = getParentOffice(office.getParentId());
            if (StringUtils.isNotEmpty(pName)) {
                officeNames = pName + "," + officeNames;
            }
        }
        return officeNames;
    }

    /**
     * 查询所有的父部门以及自己
     *
     * @param office
     * @return
     */
    public List<SysOffice> getAllParents(SysOffice office) {
        SysOffice sysOffice = mapper.selectByPrimaryKey(office.getId());
        Example example = new Example(SysOffice.class);
        String pids = sysOffice.getParentIds();
        pids = pids.substring(0, pids.length() - 1);
        String[] idArrray = pids.split(",");
        example.createCriteria().andIn("id", java.util.Arrays.asList(idArrray));
        List<SysOffice> parentsOffices = mapper.selectByExample(example);
        parentsOffices.add(sysOffice);
        return parentsOffices;
    }


}
