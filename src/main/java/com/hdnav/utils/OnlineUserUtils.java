package com.hdnav.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             用户在线工具类       
 * JDK version used:     JDK1.8                             
 * Author：                                         zhanglx               
 * Create Date：                           2017-07-26 
*/
public class OnlineUserUtils {
	
	/**
     * 根据管区编号查询所有在线用户ID(ALL)
     *
     * @param String
     * @return List<Integer>
     */
	public static List<Integer> getOnlineUserIdBySuperVise(String superviseCode){
		List<Integer> onlineUserList = new ArrayList<Integer>();
		DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
        for(Session session:sessions){
        	String currentSupervise = session.getAttribute("supervise").toString();
        	if(superviseCode.equals(currentSupervise)){
        		if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null){
            		int currentUserId = Integer.parseInt(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY).toString());
                	onlineUserList.add(currentUserId);
            	}
        	}
          }
		return onlineUserList;
	}
	
	/**
     * 根据管区编号查询所有在线用户NAME(除了自己)
     *
     * @param String
     * @return List<String>
     */
	public static List<String> getOnlineUsersExpectSelf(){
		Subject subject = SecurityUtils.getSubject();
		Session curSession = subject.getSession(false);
		String curUserName = curSession.getAttribute("username").toString();
		List<String> onlineUserList = new ArrayList<String>();
		DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
        for(Session session:sessions){
        	String usernameTmp = session.getAttribute("username").toString();
        	if(curUserName.equals(usernameTmp)){
        		continue;
        	}else{
        		if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null){
            		String currentUserName = session.getAttribute("username").toString();
                	onlineUserList.add(currentUserName);
            	}
        	}
          }
		return onlineUserList;
	}
	
	
	
	/**
     * 根据管区编号查询所有在线用户NAME(也包含全部管区的，但是除了自己)
     *
     * @param String
     * @return List<String>
     */
	public static List<String> getOnlineUserNameBySuperVise(){
		Subject subject = SecurityUtils.getSubject();
		Session curSession = subject.getSession(false);
		String curSuperVise = curSession.getAttribute("supervise").toString();
		String curUserName = curSession.getAttribute("username").toString();
		List<String> onlineUserList = new ArrayList<String>();
		DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
        for(Session session:sessions){
        	String superviseTmp = session.getAttribute("supervise").toString();
        	String usernameTmp = session.getAttribute("username").toString();
        	if(curUserName.equals(usernameTmp)){
        		continue;
        	}else if("all".equals(curSuperVise) || curSuperVise.equals(superviseTmp)){
        		if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null){
            		String currentUserName = session.getAttribute("username").toString();
                	onlineUserList.add(currentUserName);
            	}
        	}
          }
		return onlineUserList;
	}
	
	
	/**
     * 获取当前登陆用户ID
     *
     * @param 
     * @return String
     */
	public static String getCurrentLoginUser(){
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession(false);
		return session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY).toString();
	}
	
	
	/**
     * 获取当前关注的管区
     *
     * @param 
     * @return String
     */
	public static String getCurrentSuperVise(){
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession(false);
		return session.getAttribute("supervise").toString();
	}

}
