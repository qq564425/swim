package com.hdnav.shiro;

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdnav.entity.Account;
import com.hdnav.service.AccountService;


/** */
public class UserRealm extends AuthorizingRealm {
	
	@Autowired
	private AccountService accountService;
	
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        int userId = Integer.parseInt(principals.getPrimaryPrincipal().toString());
        SimpleAuthorizationInfo authorizationInfo = accountService.getAccountRolePermission(userId);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        Account account = accountService.getAccountByAccount(username);
        if (account == null) {
            throw new UnknownAccountException();
        }
        String accountPassword = account.getPassword();
        if (!password.equals(accountPassword)) {
            throw new IncorrectCredentialsException();
        }
        //处理session
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
        for(Session session:sessions){
            //清除该用户以前登录时保存的session
            if(account.getId().toString().equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
            	sessionManager.getSessionDAO().delete(session);
            }
        }
        this.setAuthenticationCachingEnabled(true);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(account.getId(), password, getName());
        return authenticationInfo;
    }
}
