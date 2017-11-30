/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.egojit.easyweb.upms.sso.security;
import com.egojit.easyweb.common.Encodes;
import com.egojit.easyweb.common.Global;
import com.egojit.easyweb.common.security.shiro.session.SessionDAO;
import com.egojit.easyweb.common.utils.MD5Util;
import com.egojit.easyweb.common.web.Servlets;
import com.egojit.easyweb.common.web.ValidateCodeServlet;
import com.egojit.easyweb.upm.service.SysUserService;
import com.egojit.easyweb.upms.dao.mapper.SysUserMapper;
import com.egojit.easyweb.upms.model.SysMenu;
import com.egojit.easyweb.upms.model.SysRole;
import com.egojit.easyweb.upms.model.SysUser;
import com.egojit.easyweb.upms.sso.LoginUtil;
import com.egojit.easyweb.upms.sso.UserUtils;
import com.mysql.jdbc.log.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 系统安全认证实现类
 * @author Egojit
 * @version 2017-7-5
 */
@Service
public class SystemAuthorizingRealm extends AuthorizingRealm {

	private Logger logger = LoggerFactory.getLogger(getClass());
//	public static final String HASH_ALGORITHM = "MD5";
//	public static final int HASH_INTERATIONS = 1024;
	@Autowired
	private SessionDAO sessionDao;
	@Autowired
	private SysUserService sysUserService;
	
	public SystemAuthorizingRealm() {
		this.setCachingEnabled(false);
	}


	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

		int activeSessionSize =sessionDao.getActiveSessions(false).size();
		if (logger.isDebugEnabled()){
			logger.debug("login submit, active session size: {}, username: {}", activeSessionSize, token.getUsername());
		}

		// 校验登录验证码
		if (LoginUtil.isValidateCodeLogin(token.getUsername(), false, false)){
			Session session = UserUtils.getSession();
			String code = (String)session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
			if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)){
				throw new AuthenticationException("msg:验证码错误, 请重试.");
			}
		}

		// 校验用户名密码
		SysUser user = sysUserService.getByLoginName(token.getUsername());
		if (user != null) {
			if (Global.NO.equals(user.getLoginFlag())){
				throw new AuthenticationException("msg:该已帐号禁止登录.");
			}

			ByteSource credentialsSalt = ByteSource.Util.bytes(user.getLoginName());


//			SimpleAuthenticationInfo info =  new SimpleAuthenticationInfo(new Principal(user),
//					user.getPassword(), credentialsSalt, user.getName());
			SimpleAuthenticationInfo info =  new SimpleAuthenticationInfo(user.getLoginName(),
					user.getPassword(), credentialsSalt, user.getName());
			return info;
		} else {
			return null;
		}
	}
	
	/**
	 * 获取权限授权信息，如果缓存中存在，则直接从缓存中获取，否则就重新获取， 登录成功后调用
	 */
	protected AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null) {
            return null;
        }
		
        AuthorizationInfo info = null;
        info = (AuthorizationInfo)UserUtils.getCache(UserUtils.CACHE_AUTH_INFO);

        if (info == null) {
            info = doGetAuthorizationInfo(principals);
            if (info != null) {
            	UserUtils.putCache(UserUtils.CACHE_AUTH_INFO, info);
            }
        }

        return info;
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String principal = (String) getAvailablePrincipal(principals);
		// 获取当前已登录的用户
//		if (!Global.TRUE.equals(Global.getInstance().getConfig("user.multiAccountLogin"))){
//			Collection<Session> sessions = getSystemService().getSessionDao().getActiveSessions(true, principal, UserUtils.getSession());
//			if (sessions.size() > 0){
//				// 如果是登录进来的，则踢出已在线用户
//				if (UserUtils.getSubject().isAuthenticated()){
//					for (Session session : sessions){
//						getSystemService().getSessionDao().delete(session);
//					}
//				}
//				// 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
//				else{
//					UserUtils.getSubject().logout();
//					throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
//				}
//			}
//		}
		SysUser user =sysUserService.getByLoginName(principal);
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<SysMenu> list = UserUtils.getMenuList();
			for (SysMenu menu : list){
				if (StringUtils.isNotBlank(menu.getPermission())){
					// 添加基于Permission的权限信息
					for (String permission : StringUtils.split(menu.getPermission(),",")){
						info.addStringPermission(permission);
					}
				}
			}
			// 添加用户权限
			info.addStringPermission("user");
			// 添加用户角色信息
			for (SysRole role : UserUtils.getRoleList()){
				info.addRole(role.getEnname());
			}
//			// 更新登录IP和时间
//			getSystemService().updateUserLoginInfo(user);
//			// 记录登录日志
//			LogUtils.saveLog(Servlets.getRequest(), "系统登录");
			return info;
		} else {
			return null;
		}
	}
	
	@Override
	protected void checkPermission(Permission permission, AuthorizationInfo info) {
		authorizationValidate(permission);
		super.checkPermission(permission, info);
	}
	
	@Override
	protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
        		authorizationValidate(permission);
            }
        }
		return super.isPermitted(permissions, info);
	}
	
	@Override
	public boolean isPermitted(PrincipalCollection principals, Permission permission) {
		authorizationValidate(permission);
		return super.isPermitted(principals, permission);
	}
	
	@Override
	protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
            	authorizationValidate(permission);
            }
        }
		return super.isPermittedAll(permissions, info);
	}
	
	/**
	 * 授权验证方法
	 * @param permission
	 */
	private void authorizationValidate(Permission permission){
		// 模块授权预留接口
	}
	
	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(MD5Util.HASH_ALGORITHM);
		matcher.setHashIterations(MD5Util.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}
	
//	/**
//	 * 清空用户关联权限认证，待下次使用时重新加载
//	 */
//	public void clearCachedAuthorizationInfo(Principal principal) {
//		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
//		clearCachedAuthorizationInfo(principals);
//	}

	/**
	 * 清空所有关联认证
	 * @Deprecated 不需要清空，授权缓存保存到session中
	 */
	@Deprecated
	public void clearAllCachedAuthorizationInfo() {
//		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
//		if (cache != null) {
//			for (Object key : cache.keys()) {
//				cache.remove(key);
//			}
//		}
	}


	
//	/**
//	 * 授权用户信息
//	 */
//	public static class Principal implements Serializable {
//
//		private static final long serialVersionUID = 1L;
//
//		private String id; // 编号
//		private String loginName; // 登录名
//		private String name; // 姓名
////		private boolean mobileLogin; // 是否手机登录
//
////		private Map<String, Object> cacheMap;
//
//		public Principal(SysUser user) {
//			this.id = user.getId();
//			this.loginName = user.getLoginName();
//			this.name = user.getName();
////			this.mobileLogin = mobileLogin;
//		}
//
//		public String getId() {
//			return id;
//		}
//
//		public String getLoginName() {
//			return loginName;
//		}
//
//		public String getName() {
//			return name;
//		}
//
////		public boolean isMobileLogin() {
////			return mobileLogin;
////		}
//
////		@JsonIgnore
////		public Map<String, Object> getCacheMap() {
////			if (cacheMap==null){
////				cacheMap = new HashMap<String, Object>();
////			}
////			return cacheMap;
////		}
//
//		/**
//		 * 获取SESSIONID
//		 */
//		public String getSessionid() {
//			try{
//				return (String) UserUtils.getSession().getId();
//			}catch (Exception e) {
//				return "";
//			}
//		}
//
//		@Override
//		public String toString() {
//			return id;
//		}
//
//	}



	public static void main(String[] args){


    /*  //md5加密
        String hashAlgorithmName = "MD5";
        String credentials = "admin";
        int hashIterations = 1024;
        Object obj = new SimpleHash(hashAlgorithmName, credentials, null, hashIterations);
        System.out.println(obj);*/



		String credentials = "admin";
		ByteSource credentialsSalt = ByteSource.Util.bytes("admin");
		Object obj = new SimpleHash(MD5Util.HASH_ALGORITHM, credentials, credentialsSalt, MD5Util.HASH_INTERATIONS);
		System.out.println(obj);

	}
}
