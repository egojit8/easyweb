package com.egojit.easyweb.upms.dao.mapper;

import com.egojit.easyweb.upms.model.SysUser;
import tk.mybatis.mapper.common.Mapper;

public interface SysUserMapper extends Mapper<SysUser> {

    SysUser getByLoginName(SysUser user);
}