package com.egojit.easyweb.upms.dao.mapper;

import com.egojit.easyweb.upms.model.SysRole;
import com.egojit.easyweb.upms.model.SysUser;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysRoleMapper extends Mapper<SysRole> {

    List<SysRole> getRolesByUser(SysUser user);
}