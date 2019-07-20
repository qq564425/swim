package com.hdnav.shiro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.hdnav.shiro.distributed.RedisSessionDao;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             Shiro 配置        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/
@Configuration
public class ShiroConfiguration {
	
	private static Logger log = LoggerFactory.getLogger(ShiroConfiguration.class);
	
    private ShiroSignInProperties signInProperties = new ShiroSignInProperties();

    private ShiroCookieProperties shiroCookieProperties = new ShiroCookieProperties();
    
    public Map<String, String> urls = new HashMap<>();  
    

    @Bean
    public EhCacheManager getEhCacheManager() {  
        EhCacheManager em = new EhCacheManager();  
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");  
        return em;  
    }
    
    @Bean(name = "UserRealm")
    public UserRealm myShiroRealm(EhCacheManager cacheManager) {  
    	UserRealm realm = new UserRealm(); 
        realm.setCacheManager(cacheManager);
        return realm;
    }
    

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    
    @Bean(name = "mySessionListener")
    public List<SessionListener> getMySessionListener() {
    	List<SessionListener> listenerList = new ArrayList<SessionListener>();
    	MySessionListener mySessionListener = new MySessionListener();
    	mySessionListener.setRedisManager(new com.hdnav.shiro.distributed.RedisManager());
    	listenerList.add(mySessionListener);
        return listenerList;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(UserRealm myShiroRealm, 
    		RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        //dwsm.setRealm(myShiroRealm(getCacheManage()));
        dwsm.setRealm(myShiroRealm(getEhCacheManager()));
        //用户授权/认证信息Cache, 采用EhCache 缓存 
        dwsm.setCacheManager(getEhCacheManager());
        //用户授权/认证信息Cache, 采用Redis 缓存
        //dwsm.setCacheManager(getRedisCacheManage());
        dwsm.setSessionManager(getSessionManage());
        dwsm.setRememberMeManager(rememberMeManager());
        return dwsm;
    }
    
    

    /**
     * 配置shiroFilter权限控制规则
     *
     * @param ShiroFilterFactoryBean
     * @return
     */
    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean){
    	Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();//获取filters
    	filters.put("o", new OnLineFilter());//将自定义 的OnLineFilter注入shiroFilter中
    	filters.put("p", new PermissionFilter());//将自定义 的PermissionFilter注入shiroFilter中
        /*下面这些规则配置最好配置到配置文件中 */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置退出过滤器,其中的具体代码Shiro已经替我们实现了
      //配置记住我或认证通过可以访问的地址
      	filterChainDefinitionMap.put("/user/check-login", "anon");//登录系统不拦截
      	filterChainDefinitionMap.put("/user/getLoginStatus", "anon");//检查是否登录不拦截
      	filterChainDefinitionMap.put("/user/getLoginStatusFromSession", "anon");//不拦截
      	filterChainDefinitionMap.put("/user/getChangeLoginStatus", "anon");//同上
      	filterChainDefinitionMap.put("/user/exit", "anon");//退出系统不拦截
      	filterChainDefinitionMap.put("/main/department/get-all-departments", "anon");//退出系统不拦截
        filterChainDefinitionMap.put("/**", "o");
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }

    /**
     * ShiroFilter
     * @param securityManager
     * @return ShiroFilterFactoryBean
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(org.apache.shiro.mgt.SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new MShiroFilterFactoryBean();
        // 必须设置 SecurityManager  
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        //下面代码无效
        //shiroFilterFactoryBean.setLoginUrl("http://190.0.1.34:8902");
        // 登录成功后要跳转的连接
        //下面代码无效
//        shiroFilterFactoryBean.setSuccessUrl("http://190.0.1.34:8900");
        // 未授权界面;
        //shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        loadShiroFilterChain(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }
    
    @ConditionalOnMissingBean
    @Bean(name = "defaultAdvisorAutoProxyCreator")
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }
    
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        //System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
  }
    
	@Bean(name = "rememberMeCookie")
	public SimpleCookie rememberMeCookie(){
	      //System.out.println("ShiroConfiguration.rememberMeCookie()");
	      //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
	      SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
	      //<!-- 记住我cookie生效时间30天 ,单位秒;-->
	      simpleCookie.setMaxAge(259200);
	      return simpleCookie;
	}
    
    @Bean(name = "sessionManager")
	public DefaultWebSessionManager getSessionManage() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		/*设置全局会话超时时间，默认30分钟(1800000)*/ 
		sessionManager.setGlobalSessionTimeout(1800000);
		/*会话验证器调度时间*/
		sessionManager.setSessionValidationScheduler(getExecutorServiceSessionValidationScheduler());
		/*定时检查失效的session*/
		sessionManager.setSessionValidationSchedulerEnabled(true);
		/*是否在会话过期后会调用SessionDAO的delete方法删除会话 默认true*/
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setSessionIdCookie(getSessionIdCookie());
		
		RedisSessionDao cacheSessionDao = new RedisSessionDao();
		cacheSessionDao.setCacheManager(getEhCacheManager());
		//cacheSessionDao.setCacheManager(getRedisCacheManage());
		cacheSessionDao.setRedisManager(new com.hdnav.shiro.distributed.RedisManager());
		sessionManager.setSessionDAO(cacheSessionDao);
		
		// -----可以添加session 创建、删除的监听器
		sessionManager.setSessionListeners(getMySessionListener());
		return sessionManager;
	}
    
    @Bean(name = "sessionValidationScheduler")
	public ExecutorServiceSessionValidationScheduler getExecutorServiceSessionValidationScheduler() {
		ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
		scheduler.setInterval(1800000);
		return scheduler;
	}
    
    @Bean(name = "sessionIdCookie")
	public SimpleCookie getSessionIdCookie() {
    	/*cookie的name,对应的默认是 JSESSIONID*/
		SimpleCookie cookie = new SimpleCookie("sid");
		cookie.setHttpOnly(false);
//		cookie.setHttpOnly(true);
		cookie.setMaxAge(1800000);
//		cookie.setMaxAge(-1);
		/*jsessionId的path为 / 用于多个系统共享jsessionId -->*/
		cookie.setPath("/");
		return cookie;
	}

}
