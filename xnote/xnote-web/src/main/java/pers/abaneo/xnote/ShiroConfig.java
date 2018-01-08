package pers.abaneo.xnote;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pers.abaneo.xnote.support.shiro.ShiroAuthcFilter;
import pers.abaneo.xnote.support.shiro.ShiroRealm;

@Configuration
public class ShiroConfig {

    @Bean(name="shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("xnote");
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        
        Map<String, Filter> filters=new HashMap<String, Filter>();
        filters.put("authc", new ShiroAuthcFilter());
        shiroFilterFactoryBean.setFilters(filters);

        
        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置不会被拦截的链接 顺序判断,
        // ** 不能放在最前
        // 相同匹配规则后面覆盖前面
        // 不同匹配规则匹配同一个url,第一个有效
        // 静态资源
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.png", "anon");
        // 登录注册
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/signin", "anon");
        filterChainDefinitionMap.put("/user/**", "anon");
//        filterChainDefinitionMap.put("/xnote/**", "user");
//        filterChainDefinitionMap.put("/service/xnote/**", "user");

        // 默认的配置
        // 约定:/**/view* 为非用户登录情况可查看
        // 约定:/**/detail* 为用户登录情况可查看
        filterChainDefinitionMap.put("/**/view/*", "anon");
        filterChainDefinitionMap.put("/**/create*", "user");
        filterChainDefinitionMap.put("/**/detail*", "user");
        filterChainDefinitionMap.put("/**/update*", "user");
        filterChainDefinitionMap.put("/**/delete*", "user");
        filterChainDefinitionMap.put("/**/upload*", "user");
        filterChainDefinitionMap.put("/**", "user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    
    @Bean(name = "sessionIdCookie")
	public SimpleCookie getSessionIdCookie() {
		SimpleCookie cookie = new SimpleCookie("sid");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(-1);
		return cookie;
	}

	@Bean(name = "rememberMeCookie")
	public SimpleCookie getRememberMeCookie() {
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		simpleCookie.setHttpOnly(true);
		simpleCookie.setMaxAge(2592000);
		return simpleCookie;
	}

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setSessionManager(getSessionManager());
        securityManager.setRememberMeManager(getRememberManager());
        return securityManager;
    }
    
    @Bean(name = "sessionManager")
	public DefaultWebSessionManager getSessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(1800000);
//		sessionManager.setSessionValidationScheduler(getExecutorServiceSessionValidationScheduler());
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setSessionIdCookie(getSessionIdCookie());
		sessionManager.setSessionIdUrlRewritingEnabled(false);
//		EnterpriseCacheSessionDAO cacheSessionDAO = new EnterpriseCacheSessionDAO();
//		cacheSessionDAO.setCacheManager(getCacheManage());
//		sessionManager.setSessionDAO(cacheSessionDAO);
		// -----可以添加session 创建、删除的监听器
		
		return sessionManager;
	}
    
    @Bean
	public CookieRememberMeManager getRememberManager(){
		CookieRememberMeManager meManager = new CookieRememberMeManager();
		meManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
		meManager.setCookie(getRememberMeCookie());
		return meManager;
	}

    /**
     * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
     * @return
     */
    @Bean
    public ShiroRealm myShiroRealm() {
    	ShiroRealm myShiroRealm = new ShiroRealm();
        return myShiroRealm;
    }
}
