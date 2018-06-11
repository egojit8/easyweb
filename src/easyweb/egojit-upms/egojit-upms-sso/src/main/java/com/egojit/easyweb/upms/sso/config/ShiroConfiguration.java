package com.egojit.easyweb.upms.sso.config;


import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.egojit.easyweb.common.security.shiro.cache.RedisCacheManager;
import com.egojit.easyweb.common.security.shiro.session.RedisSessionDAO;
import com.egojit.easyweb.common.security.shiro.session.SessionManager;
import com.egojit.easyweb.common.utils.IdGen;
import com.egojit.easyweb.upms.sso.security.SystemAuthorizingRealm;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
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

//    @Autowired
//    RedisSessionDAO redisSessionDAO;

    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
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
        Map map = new HashMap<>();
//        map.put("authc",formAuthenticationFilter);
        shiroFilterFactoryBean.setFilters(map);
        loadShiroFilterChain(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }


    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(
            SystemAuthorizingRealm myShiroRealm,
            DefaultWebSessionManager sessionManager,
            CacheManager shiroCacheManager) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(myShiroRealm);
        dwsm.setSessionManager(sessionManager);
//      <!-- 用户授权/认证信息Cache, 采用redis 缓存 -->
        dwsm.setCacheManager(shiroCacheManager);
        return dwsm;
    }

    @Bean(name = "sessionManager")
    public DefaultWebSessionManager getSessionManager(
            RedisSessionDAO sessionDAO,
            SimpleCookie sessionIdCookie) {
        SessionManager sessionManager = new SessionManager();
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setGlobalSessionTimeout(1800000);
        //定时清理失效会话, 清理用户直接关闭浏览器造成的孤立会话4分钟检查一次
        sessionManager.setSessionValidationInterval(24000);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean(name = "sessionIdCookie")
    public SimpleCookie getSessionIdCookie() {
        SimpleCookie cookie = new SimpleCookie("jtlt.session.id");
        return cookie;

    }

////    @Bean(name = "sessionDAO")
//    public RedisSessionDAO getSessionDAO(IdGen idGen){
//        redisSessionDAO.setSessionIdGenerator(idGen);
//        redisSessionDAO.setActiveSessionsCacheName("activeSessionsCache");
////        redisSessionDAO.setCacheManager(shiroCacheManager);
//        return redisSessionDAO;
//    }

    @Bean(name = "idGen")
    public IdGen getidGen() {
        IdGen idGen = new IdGen();
        return idGen;
    }


//    /**
//     * 设置redis
//     * @return
//     */
//    @Bean(name = "shiroCacheManager")
//    public CacheManager getShiroCacheManager() {
//        RedisCacheManager cacheManager = new RedisCacheManager();
//        //默认超时时间,单位秒
//        cacheManager.setDefaultExpiration(600);
//        //根据缓存名称设置超时时间,0为不超时
////        Map<String,Long> expires = new ConcurrentHashMap<>();
////        cacheManager.setExpires(expires);
//
//        return cacheManager;
//    }


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
     * @create 2016年1月14日
     */
    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
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

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();

    }
}
