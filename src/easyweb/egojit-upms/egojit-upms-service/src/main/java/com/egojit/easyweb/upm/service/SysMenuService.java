package com.egojit.easyweb.upm.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egojit.easyweb.common.base.CurdService;
import com.egojit.easyweb.common.models.User;
import com.egojit.easyweb.common.utils.CacheUtils;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.common.utils.UserUtil;
import com.egojit.easyweb.upms.dao.mapper.SysDictMapper;
import com.egojit.easyweb.upms.dao.mapper.SysMenuMapper;
import com.egojit.easyweb.upms.model.SysDict;
import com.egojit.easyweb.upms.model.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.egojit.easyweb.common.utils.UserUtil.CACHE_MENU_LIST;

/**
 * 备注：菜单服务类
 * 作者：egojit
 * 日期：2017/12/8
 */
@Service
public class SysMenuService extends CurdService<SysMenuMapper, SysMenu> {

    @Autowired
    SysMenuMapper menuMapper;

    /**
     * 返回菜单
     *
     * @param getAll 是否返回所有菜单
     * @return
     */
    public JSONArray getMenus(String roleId, String pMenuId, boolean getAll) {
        Example example = new Example(SysMenu.class);
        Example.Criteria criteria = example.createCriteria();
        JSONArray list = new JSONArray();
        if (StringUtils.isEmpty(pMenuId)) {
            criteria.andEqualTo("parentId", "0");//获取公司
        } else {
            criteria.andEqualTo("parentId", pMenuId);
        }
        List<SysMenu> midList = mapper.selectByExample(example);
        if (midList != null) {
            for (SysMenu item : midList) {
                JSONObject obj = new JSONObject();
                obj.put("name", item.getName());
                obj.put("id", item.getId());
                obj.put("pId", item.getParentId());
                if(StringUtils.isNotEmpty(roleId)) {
                    obj.put("checked", isRoleHaveMenu(roleId, item.getId()));
                }
                boolean isParent = isHaveChild(item.getId());
                obj.put("isParent", "" + isParent);
                if (getAll && isParent) {
                    obj.put("children", getMenus(roleId, item.getId(), getAll));
                }
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 返回树形结构所有菜单,并且设置被勾选的id
     * @param ckId 被选中的id
     * @param pMenuId
     * @return
     */
    public JSONArray getMenus(String ckId, String pMenuId) {
        Example example = new Example(SysMenu.class);
        Example.Criteria criteria = example.createCriteria();
        JSONArray list = new JSONArray();
        if (StringUtils.isEmpty(pMenuId)) {
            criteria.andEqualTo("parentId", "0");//获取公司
        } else {
            criteria.andEqualTo("parentId", pMenuId);
        }
        List<SysMenu> midList = mapper.selectByExample(example);
        if (midList != null) {
            for (SysMenu item : midList) {
                JSONObject obj = new JSONObject();
                obj.put("name", item.getName());
                obj.put("id", item.getId());
                obj.put("pId", item.getParentId());
                if(StringUtils.isNotEmpty(ckId)&&item.getId().equals(ckId)) {
                    obj.put("checked",true);
                }else {
                    obj.put("false",true);
                }
                boolean isParent = isHaveChild(item.getId());
                obj.put("isParent", "" + isParent);
                if (isParent) {
                    obj.put("children", getMenus(ckId,item.getId()));
                }
                list.add(obj);
            }
        }
        return list;
    }


    /**
     * 判断是否有子菜单
     *
     * @param id
     * @return
     */
    public boolean isHaveChild(String id) {
        Example example = new Example(SysMenu.class);

        example.createCriteria().andEqualTo("parentId", id);
        int count = mapper.selectCountByExample(example);
        return count > 0 ? true : false;
    }


    /**
     * 返回菜单
     *
     * @param getAll 是否返回所有菜单
     * @return
     */
    public JSONArray getMenusByUserId(String roleId, String userId, String pMenuId, boolean getAll) {
        JSONArray list = new JSONArray();
        if (StringUtils.isEmpty(pMenuId)) {
            pMenuId = "0";//查询顶级
        }
        Map parm = new HashMap();
        parm.put("userId", userId);
        parm.put("parentId", pMenuId);
        List<SysMenu> midList = mapper.selectUserMenuByPid(parm);//mapper.selectByExample(example);
        if (midList != null) {
            for (SysMenu item : midList) {
                JSONObject obj = new JSONObject();
                obj.put("name", item.getName());
                obj.put("id", item.getId());
                obj.put("pId", item.getParentId());
                if(StringUtils.isNotEmpty(roleId)) {
                    obj.put("checked", isRoleHaveMenu(roleId, item.getId()));
                }
                boolean isParent = isHaveChild(item.getId());
                obj.put("isParent", "" + isParent);
                if (getAll && isParent) {
                    obj.put("children", getMenusByUserId(roleId, userId, item.getId(), getAll));
                }
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 根据人员获取人员拥有的权限菜单和按钮
     *
     * @param userId
     * @return
     */
    public List<SysMenu> getUserMenusByUserId(String userId) {
        List<SysMenu> menuList = menuMapper.selectByUserId(userId);
        return menuList;
    }

    /**
     * 判断角色是否包含某个菜单
     *
     * @param roleId
     * @param menuId
     * @return
     */
    public boolean isRoleHaveMenu(String roleId, String menuId) {
        Map parm = new HashMap();
        parm.put("roleId", roleId);
        parm.put("menuId", menuId);
        int count = menuMapper.isRoleHaveMenu(parm);
        return count > 0 ? true : false;
    }

}
