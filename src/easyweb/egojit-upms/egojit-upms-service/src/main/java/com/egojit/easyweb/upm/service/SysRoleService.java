package com.egojit.easyweb.upm.service;

import com.alibaba.fastjson.JSON;
import com.egojit.easyweb.common.base.BaseService;
import com.egojit.easyweb.common.base.CurdService;
import com.egojit.easyweb.common.utils.IdGen;
import com.egojit.easyweb.common.utils.MD5Util;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upms.dao.mapper.SysRoleMapper;
import com.egojit.easyweb.upms.dao.mapper.SysRoleMenuMapper;
import com.egojit.easyweb.upms.dao.mapper.SysUserMapper;
import com.egojit.easyweb.upms.dao.mapper.SysUserRoleMapper;
import com.egojit.easyweb.upms.model.SysRole;
import com.egojit.easyweb.upms.model.SysRoleMenu;
import com.egojit.easyweb.upms.model.SysUser;
import com.egojit.easyweb.upms.model.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by egojit on 2017/11/23.
 */
@Service
@Transactional
public class SysRoleService extends CurdService<SysRoleMapper, SysRole> {


    @Autowired
    SysRoleMenuMapper roleMenuMapper;
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;
    /**
     * 获取用户角色
     *
     * @return
     */
    public List<SysRole> getRoleByUser(SysUser sysUser) {
        return mapper.getRolesByUser(sysUser);
    }

    /**
     * 设置角色权限
     * @param roleId 角色id
     * @param menusIds 权限列表
     */
    public void setPower(String roleId,String menusIds) {
        List<String> menus= JSON.parseArray(menusIds,String.class);
        Example example=new Example(SysRoleMenu.class);
        example.createCriteria().andEqualTo("roleId",roleId);
        roleMenuMapper.deleteByExample(example);
        if(menus!=null){
            for (String menuId:menus) {
                SysRoleMenu roleMenu=new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenu.setId(IdGen.uuid());
                roleMenuMapper.insert(roleMenu);
            }
        }
    }


    /**
     * 设置用户角色
     * @param userId  用户id
     * @return
     */
    public boolean setUserRoles(String userId,List<String> roleIds){
        Example example=new Example(SysUserRole.class);
        example.createCriteria().andEqualTo("userId",userId);
        sysUserRoleMapper.deleteByExample(example);
        SysUserRole sysUserRole=new SysUserRole();
        int count=0;
        for(String roleId:roleIds){
            if(StringUtils.isNotEmpty(roleId)){
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(userId);
                sysUserRole.setId(IdGen.uuid());
                count+=sysUserRoleMapper.insert(sysUserRole);
            }
        }
        return count>0?true:false;
    }
}
