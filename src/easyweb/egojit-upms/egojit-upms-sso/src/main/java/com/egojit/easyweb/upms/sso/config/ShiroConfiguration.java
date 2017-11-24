package com.egojit.easyweb.upms.sso.config;


import com.egojit.easyweb.common.security.shiro.session.CacheSessionDAO;
import com.egojit.easyweb.common.security.shiro.session.SessionDAO;
import com.egojit.easyweb.common.security.shiro.session.SessionManager;
import com.egojit.easyweb.common.utils.IdGen;
import com.egojit.easyweb.upms.sso.security.SystemAuthorizingRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by egojit on 2017/10/24.
 */
@Configuration
public class ShiroConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);


    @Bean
    public FilterRegistrationBean delegatingFilterProxy(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(
            DefaultWebSecurityManager securityManager,
            FormAuthenticationFilter formAuthenticationFilter) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/oss/login");
        // 登录成功后要跳转的连接
        shiroFilterFactoryBean.setSuccessUrl("/admin/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        Map map=new HashMap<>();
//        map.put("authc",formAuthenticationFilter);
        shiroFilterFactoryBean.setFilters(map);
        loadShiroFilterChain(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }


    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(
            SystemAuthorizingRealm myShiroRealm,
            DefaultWebSessionManager sessionManager,
            EhCacheManager shiroCacheManager) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(myShiroRealm);
        dwsm.setSessionManager(sessionManager);
//      <!-- 用户授权/认证信息Cache, 采用EhCache 缓存 -->
        dwsm.setCacheManager(shiroCacheManager);
        return dwsm;
    }
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager getSessionManager(
            SessionDAO sessionDAO,
            SimpleCookie sessionIdCookie) {
        SessionManager sessionManager=new SessionManager();
//        SessionManager sessionManager = new SessionManager();
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setGlobalSessionTimeout(1800000);
        //定时清理失效会话, 清理用户直接关闭浏览器造成的孤立会话
        sessionManager.setSessionValidationInterval(120000);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean(name = "sessionIdCookie")
    public SimpleCookie getSessionIdCookie(){
        SimpleCookie cookie=new SimpleCookie("easysite.session.id");
        return cookie;

    }

    @Bean(name = "sessionDAO")
    public SessionDAO getSessionDAO(IdGen idGen, EhCacheManager shiroCacheManager){
        CacheSessionDAO cacheSessionDAO=new CacheSessionDAO();
        cacheSessionDAO.setSessionIdGenerator(idGen);
        cacheSessionDAO.setActiveSessionsCacheName("activeSessionsCache");
        cacheSessionDAO.setCacheManager(shiroCacheManager);
        return cacheSessionDAO;
    }

    @Bean(name = "idGen")
    public IdGen getidGen(){
       IdGen idGen=new IdGen();
       return idGen;
    }


    @Bean(name = "shiroCacheManager")
    public EhCacheManager getShiroCacheManager(EhCacheManagerFactoryBean cacheManager) {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManager(cacheManager.getObject());
        return em;
    }

    @Bean(name = "cacheManager")
    public EhCacheManagerFactoryBean getCacheManager() {
        EhCacheManagerFactoryBean em = new EhCacheManagerFactoryBean();
        em.setConfigLocation(new ClassPathResource("cache/ehcache-local.xml"));
        return em;
    }





    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }

    /**
     * 加载shiroFilter权限控制规则（从数据库读取然后配置）
     *
     * @author SHANHY
     * @create  2016年1月14日
     */
    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean){
        /////////////////////// 下面这些规则配置最好配置到配置文件中 ///////////////////////
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // authc：该过滤器下的页面必须验证后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        filterChainDefinitionMap.put("/admin/**", "authc");// 这里为了测试，只限制/user，实际开发中请修改为具体拦截的请求规则
        // anon：它对应的过滤器里面是空的,什么都没做
        logger.info("##################从数据库读取权限规则，加载到shiroFilter中##################");
//        filterChainDefinitionMap.put("/user/edit/**", "authc,perms[user:edit]");// 这里为了测试，固定写死的值，也可以从数据库或其他配置中读取
//
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/**", "anon");//anon 可以理解为不拦截
//        filterChainDefinitionMap.put("/admin/login","anon");
//        filterChainDefinitionMap.put("/admin/thymeleaf/cache/clear","anon");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }



}
