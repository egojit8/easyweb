package com.egojit.easyweb.upms.dao.mapper;

import com.egojit.easyweb.upms.model.SysMenu;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SysMenuMapper extends Mapper<SysMenu> {

    List<SysMenu> selectByUserId(String userId);

    /**
     * 通过父菜单id获取用户拥有的菜单
     * @param userId
     * @param parentId
     * @return
     */
    List<SysMenu> selectUserMenuByPid(Map parm);

    /**
     * 某个角色是否存给出的菜单
     * @param parm
     * @return
     */
    int isRoleHaveMenu(Map parm);
}