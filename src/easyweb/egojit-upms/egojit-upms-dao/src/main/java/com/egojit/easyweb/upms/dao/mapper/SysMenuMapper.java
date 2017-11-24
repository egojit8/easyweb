package com.egojit.easyweb.upms.dao.mapper;

import com.egojit.easyweb.upms.model.SysMenu;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysMenuMapper extends Mapper<SysMenu> {

    List<SysMenu> selectByUserId(String userId);
}