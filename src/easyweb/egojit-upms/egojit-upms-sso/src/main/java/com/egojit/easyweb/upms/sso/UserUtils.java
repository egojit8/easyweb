package com.egojit.easyweb.upms.sso;

import com.egojit.easyweb.common.models.Company;
import com.egojit.easyweb.common.models.User;
import com.egojit.easyweb.common.utils.CacheUtils;
import com.egojit.easyweb.common.utils.SpringContextHolder;
import com.egojit.easyweb.common.utils.UserUtil;
import com.egojit.easyweb.upms.dao.mapper.SysMenuMapper;
import com.egojit.easyweb.upms.dao.mapper.SysOfficeMapper;
import com.egojit.easyweb.upms.dao.mapper.SysRoleMapper;
import com.egojit.easyweb.upms.dao.mapper.SysUserMapper;
import com.egojit.easyweb.upms.model.SysMenu;
import com.egojit.easyweb.upms.model.SysOffice;
import com.egojit.easyweb.upms.model.SysRole;
import com.egojit.easyweb.upms.model.SysUser;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.egojit.easyweb.common.utils.UserUtil.*;

/**
 * Description：系统用户工具
 * Auther：高露
 * Q Q:408365330
 * Company: 鼎斗信息技术有限公司
 * Time:2018-4-25
 */
public class UserUtils {

    private static SysUserMapper userMapper = SpringContextHolder.getBean(SysUserMapper.class);
    private static SysRoleMapper roleMapper = SpringContextHolder.getBean(SysRoleMapper.class);
    private static SysMenuMapper menuMapper = SpringContextHolder.getBean(SysMenuMapper.class);
    private static SysOfficeMapper officeMapper = SpringContextHolder.getBean(SysOfficeMapper.class);


    /**
     * 根据登录名获取用户
     *
     * @param loginName
     * @return 取不到返回null
     */
    public static User getUser(String loginName) {
        User user = UserUtil.getUserByLoginName(loginName);
        if (user == null) {
            user = new User();
            SysUser dbUser = new SysUser();
            dbUser.setLoginName(loginName);
            dbUser = userMapper.getByLoginName(dbUser);
            if (dbUser == null) {
                return null;
            } else {
                try {
                    BeanUtils.copyProperties(user, dbUser);
                    SysOffice company = officeMapper.selectByPrimaryKey(dbUser.getCompanyId());
                    if (company != null) {
                        Company companyMid = new Company();
                        BeanUtils.copyProperties(companyMid, company);
                        user.setCompany(companyMid);
                    }

                    SysOffice department = officeMapper.selectByPrimaryKey(dbUser.getCompanyId());
                    if (department != null) {
                        Company departmentMid = new Company();
                        BeanUtils.copyProperties(departmentMid,department);
                        user.setDepartment(departmentMid);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    return null;
                }
                UserUtil.saveUser(user);
            }

        }
        return user;
    }


    /**
     * 清除当前用户缓存
     */
    public static void clearUserAllCache() {
        removeCache(CACHE_AUTH_INFO);
        removeCache(CACHE_ROLE_LIST);
        removeCache(CACHE_MENU_LIST);
        removeCache(CACHE_AREA_LIST);
        removeCache(CACHE_OFFICE_LIST);
        removeCache(CACHE_OFFICE_ALL_LIST);
        UserUtils.clearUserCache(getUser());
    }

    /**
     * 清除指定用户缓存
     *
     */
    public static void clearUserCache(User user) {
        CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
        CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
//		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
//		if (user.getOffice() != null && user.getOffice().getId() != null){
//			CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
//		}
    }

    /**
     * 获取当前登录用户用户
     *
     * @return 取不到返回 new User()
     */
    public static User getUser() {
        String principal = getPrincipal();
        if (principal != null) {
            User user = getUser(principal);
            if (user != null) {
                return user;
            }
        }
        // 如果没有登录，则返回实例化空的User对象。
        return new User();
    }

    /**
     * 获取当前用户角色列表
     *
     * @return
     */
    public static List<SysRole> getRoleList() {
        @SuppressWarnings("unchecked")
        List<SysRole> roleList = (List<SysRole>) getCache(CACHE_ROLE_LIST);
        if (roleList == null) {
            User user = getUser();
            if (user.isAdmin()) {
                roleList = roleMapper.selectAll();
            } else {
                SysRole role = new SysRole();
//				role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
                roleList = roleMapper.select(role);
            }
            putCache(CACHE_ROLE_LIST, roleList);
        }
        return roleList;
    }

    /**
     * 获取当前用户授权菜单
     *
     * @return
     */
    public static List<SysMenu> getMenuList() {
        @SuppressWarnings("unchecked")
        List<SysMenu> menuList = (List<SysMenu>) getCache(CACHE_MENU_LIST);
        if (menuList == null) {
            User user = getUser();
            if (user.isAdmin()) {
                menuList = menuMapper.selectAll();
            } else {
                menuList = menuMapper.selectByUserId(user.getId());
            }
            putCache(CACHE_MENU_LIST, menuList);
        }
        return menuList;
    }

//	/**
//	 * 获取当前用户授权的区域
//	 * @return
//	 */
//	public static List<Area> getAreaList(){
//		@SuppressWarnings("unchecked")
//		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
//		if (areaList == null){
//			areaList = areaDao.findAllList(new Area());
//			putCache(CACHE_AREA_LIST, areaList);
//		}
//		return areaList;
//	}
//
//	/**
//	 * 获取当前用户有权限访问的部门
//	 * @return
//	 */
//	public static List<Office> getOfficeList(){
//		@SuppressWarnings("unchecked")
//		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_LIST);
//		if (officeList == null){
//			User user = getUser();
//			if (user.isAdmin()){
//				officeList = officeDao.findAllList(new Office());
//			}else{
//				Office office = new Office();
////				office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
//				officeList = officeDao.findList(office);
//			}
//			putCache(CACHE_OFFICE_LIST, officeList);
//		}
//		return officeList;
//	}
//
//	/**
//	 * 获取当前用户有权限访问的部门
//	 * @return
//	 */
//	public static List<Office> getOfficeAllList(){
//		@SuppressWarnings("unchecked")
//		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_ALL_LIST);
//		if (officeList == null){
//			officeList = officeDao.findAllList(new Office());
//		}
//		return officeList;
//	}
//

    /**
     * 获取授权主要对象
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前登录者对象
     */
    public static String getPrincipal() {
        try {
            Subject subject = SecurityUtils.getSubject();
            String principal = (String) subject.getPrincipal();
            if (principal != null) {
                return principal;
            }
        } catch (UnavailableSecurityManagerException e) {

        } catch (InvalidSessionException e) {

        }
        return null;
    }

    public static Session getSession() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
        } catch (InvalidSessionException e) {

        }
        return null;
    }

    // ============== User Cache ==============

    public static Object getCache(String key) {
        return getCache(key, null);
    }

    public static Object getCache(String key, Object defaultValue) {
        Object obj = getSession().getAttribute(key);
        return obj == null ? defaultValue : obj;
    }

    public static void putCache(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static void removeCache(String key) {
        getSession().removeAttribute(key);
    }
}
