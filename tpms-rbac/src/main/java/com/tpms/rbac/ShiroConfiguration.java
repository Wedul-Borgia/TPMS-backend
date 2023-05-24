package com.tpms.rbac;

import com.tpms.common.web.shiro.realm.TpRealm;
import com.tpms.rbac.shiro.realm.RbacRealm;
import com.tpms.common.web.shiro.session.CustomSessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.crazycake.shiro.serializer.ObjectSerializer;
import org.crazycake.shiro.serializer.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wld
 * @date 2023/5/14 - 0:18
 */
@Configuration
public class ShiroConfiguration {
    //1.创建realm
    @Bean
    public TpRealm getRealm() {
        return new RbacRealm();
    }

    //2.创建安全管理器
    @Bean
    public SecurityManager getSecurityManager(TpRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);

        //将自定义的会话管理器注册到安全管理器中
        securityManager.setSessionManager(sessionManager());
        //将自定义的redis缓存管理器注册到安全管理器中
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    /**
     * 配置shiro的过滤器工厂
     * 再web程序中，shiro进行权限控制全部是通过一组过滤器集合进行控制
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        //1.创建过滤器工厂
        ShiroFilterFactoryBean filterFactory = new ShiroFilterFactoryBean();
        //2.设置安全管理器
        filterFactory.setSecurityManager(securityManager);
        //3.通用配置（跳转登录页面，未授权跳转的页面）
        //跳转url地址
        filterFactory.setLoginUrl("/autherror?code=1");
        //未授权的url
        filterFactory.setUnauthorizedUrl("/autherror?code=2");
        //4.设置过滤器集合
        Map<String, String> filterMap = new LinkedHashMap<>();
        //anon -- 匿名访问
        filterMap.put("/user/login", "anon");
        filterMap.put("/office/list", "anon");
        filterMap.put("/autherror", "anon");
        filterMap.put("/log/log", "anon");

        //perms -- 具有某中权限 (使用注解配置授权)
        filterMap.put("/user/page","perms[API-USER]");
        filterMap.put("/user/list","perms[API-USER]");
        filterMap.put("/role/power/**","perms[API-POWER]");
//        filterMap.put("/role/**","perms[API-ROLE]");
        filterMap.put("/office/**","perms[API-OFFICE]");
        filterMap.put("/user/test","perms[API-TEST]");
//        filterMap.put("/user/test","roles[API-TEST]");
        //authc -- 认证之后访问（登录）
        filterMap.put("/**", "authc");
        filterFactory.setFilterChainDefinitionMap(filterMap);
        return filterFactory;
    }

    @Value("${spring.datasource.redis.host}")
    private String host;
    @Value("${spring.datasource.redis.port}")
    private int port;

    /**
     * 1.redis的控制器，操作redis
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host + ":" + port);
        return redisManager;
    }

    /**
     * 2.sessionDao
     */
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());
        return sessionDAO;
    }

    /**
     * 3.会话管理器
     */
    public DefaultWebSessionManager sessionManager() {
        CustomSessionManager sessionManager = new CustomSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        //禁用cookie
        //sessionManager.setSessionIdCookieEnabled(false);
        //禁用url重写   url;jsessionid=id
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 4.缓存管理器
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setPrincipalIdFieldName("userId");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(200000);
        //设置序列化方式
        redisCacheManager.setKeySerializer(new StringSerializer());
        redisCacheManager.setValueSerializer(new ObjectSerializer());
        return redisCacheManager;
    }


    //开启对shior注解的支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
