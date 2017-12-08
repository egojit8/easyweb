package com.egojit.easyweb.upm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.egojit.easyweb.common.base.BaseService;
import com.egojit.easyweb.common.base.CurdService;
import com.egojit.easyweb.upms.dao.mapper.SysOfficeMapper;
import com.egojit.easyweb.upms.dao.mapper.SysRoleMapper;
import com.egojit.easyweb.upms.dao.mapper.SysRoleMenuMapper;
import com.egojit.easyweb.upms.model.SysOffice;
import com.egojit.easyweb.upms.model.SysRole;
import com.egojit.easyweb.upms.model.SysRoleMenu;
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
