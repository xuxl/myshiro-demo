package pres.xuxl.myshiro.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pres.xuxl.myshiro.service.UserService;

@Configuration
public class ShiroConfig {

	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private String redisPort;
	@Autowired
	private UserService userService;
	
	/**
	 * 指定加密方式
	 * @return
	 */
	@Bean("hashedCredentialsMatcher")
	public HashedCredentialsMatcher HashedCredentialsMatcher(){
		
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		//指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //加密次数
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
		
	}
	
	/**
	 * 身份认证、鉴权  的实现
	 * @param matcher
	 * @return
	 */
	@Bean("userRealm")
    public UserRealm userRealm(@Qualifier("hashedCredentialsMatcher") 
                               HashedCredentialsMatcher matcher) {
        
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(matcher);
        return userRealm;
    }
	
	@Bean
    public ShiroFilterFactoryBean shirFilter(@Qualifier("securityManager")
                               DefaultWebSecurityManager securityManager) {
        
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 设置 SecurityManager
        bean.setSecurityManager(securityManager);
		// 设置登录成功跳转Url
        bean.setSuccessUrl("/main");
        // 设置登录跳转Url
        bean.setLoginUrl("/toLogin");
        // 设置未授权提示Url
        bean.setUnauthorizedUrl("/error/unAuth");
        
        /**
         * anon：匿名用户可访问
         * authc：认证用户可访问
         * user：使用rememberMe可访问
         * perms：对应权限可访问
         * role：对应角色权限可访问
         **/
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/login","anon");//匿名用户都能访问
        
        filterMap.put("/user/index","authc");//登录了就能访问
        filterMap.put("/vip/index","roles[vip]");//指定角色才能访问
        
        filterMap.put("/druid/**", "anon");//匿名用户都能访问
        filterMap.put("/static/**","anon");//匿名用户都能访问

//        filterMap.put("/other/index","perms[other:list]");//有权限才能访问
        //从数据库动态加载需要做拦截的权限
        userService.loadPermissionFromDB(filterMap);
        
        filterMap.put("/**","authc");//登录才能访问
        filterMap.put("/logout", "logout");
        

        bean.setFilterChainDefinitionMap(filterMap);
        return bean;
    }
	
	
	
	
	/**
     * 注入 securityManager
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(
        HashedCredentialsMatcher hashedCredentialsMatcher) {
        
        DefaultWebSecurityManager securityManager = 
            new DefaultWebSecurityManager();
        // 关联realm.
        securityManager.setRealm(userRealm(hashedCredentialsMatcher));
        
        //自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        //自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }
    
    /**
    * 配置shiro redisManager
    * @return
    */
    public RedisManager redisManager() {

	    RedisManager redisManager = new RedisManager();
	    redisManager.setHost(redisHost);
	    redisManager.setPort(new Integer(redisPort));
	    redisManager.setExpire(1800);// 配置过期时间
	    // redisManager.setTimeout(timeout);
	    // redisManager.setPassword(password);
	    return redisManager;
    }

    /**
    * cacheManager 缓存 redis实现
    * @return
    */
    public RedisCacheManager cacheManager() {

	    RedisCacheManager redisCacheManager = new RedisCacheManager();
	    redisCacheManager.setRedisManager(redisManager());
	    return redisCacheManager;
    }

    /**
    * RedisSessionDAO shiro sessionDao层的实现 通过redis
    */
    public RedisSessionDAO redisSessionDAO() {

	    RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
	    redisSessionDAO.setRedisManager(redisManager());
	    redisSessionDAO.setKeyPrefix("shiro_redis_cache");
	    return redisSessionDAO;
    }

    /**
    * shiro session的管理
    */
    public DefaultWebSessionManager sessionManager() {

	    DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
	    sessionManager.setSessionDAO(redisSessionDAO());
	    return sessionManager;
    }
	
}
