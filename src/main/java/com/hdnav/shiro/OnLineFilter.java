package com.hdnav.shiro;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.alibaba.fastjson.JSON;
import com.hdnav.entity.vo.ResultVO;


/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             用户在线过滤类       
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/
public class OnLineFilter extends FormAuthenticationFilter {

    @SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(OnLineFilter.class);

	/**
     * 请求被允许时调用的方法
     * @param requesServletRequestt, ServletResponse, Object
	 * @return boolean
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated() && subject.isRemembered()){
        	return true;
        }
        
        if (subject.isAuthenticated()) {
            return true;
        }
	    return false;
    }

    /**
     * 请求被拒绝时调用的方法
     * @param requesServletRequestt, ServletResponse, Object
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    	SecurityUtils.getSubject().logout();
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpResponse.setCharacterEncoding("UTF-8");
        PrintWriter writer = httpResponse.getWriter();
        ResultVO resultVO = new ResultVO();
        resultVO.setOk(false);
        resultVO.setMsg("Login Required");
        writer.print(JSON.toJSONString(resultVO));
        writer.flush();
        writer.close();
        //this.redirectToLogin(httpRequest, httpResponse);
        return false;
    }

}
