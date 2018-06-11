package com.egojit.easyweb.upms.dao.mapper;

import com.egojit.easyweb.upms.model.SysUser;
import com.egojit.easyweb.upms.model.SysUserExt;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

public interface SysUserMapper extends Mapper<SysUser> {

    SysUser getByLoginName(SysUser user);

    List<SysUserExt> selectExtByExample(Example example);
}