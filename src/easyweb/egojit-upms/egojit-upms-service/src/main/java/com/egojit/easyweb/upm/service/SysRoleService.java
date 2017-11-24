package com.egojit.easyweb.upm.service;

import com.egojit.easyweb.common.base.BaseService;
import com.egojit.easyweb.upms.dao.mapper.SysRoleMapper;
import com.egojit.easyweb.upms.dao.mapper.SysUserMapper;
import com.egojit.easyweb.upms.model.SysRole;
import com.egojit.easyweb.upms.model.SysUser;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by egojit on 2017/11/23.
 */
@Service
public class SysRoleService extends BaseService<SysRoleMapper, SysRole> {


    /**
     * 获取用户角色
     *
     * @return
     */
    public List<SysRole> getRoleByUser(SysUser sysUser) {
        return mapper.getRolesByUser(sysUser);
    }
}
