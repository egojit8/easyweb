package com.egojit.easyweb.upm.service;

import com.alibaba.fastjson.JSON;
import com.egojit.easyweb.common.base.BaseService;
import com.egojit.easyweb.common.base.CurdService;
import com.egojit.easyweb.upms.dao.mapper.SysRoleMapper;
import com.egojit.easyweb.upms.dao.mapper.SysRoleMenuMapper;
import com.egojit.easyweb.upms.dao.mapper.SysUserMapper;
import com.egojit.easyweb.upms.model.SysRole;
import com.egojit.easyweb.upms.model.SysRoleMenu;
import com.egojit.easyweb.upms.model.SysUser;
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
                roleMenuMapper.insert(roleMenu);
            }
        }
    }
}
