package com.hdnav.shiro;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import com.alibaba.fastjson.JSON;
import com.hdnav.entity.vo.ResultVO;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             用户权限过滤类       
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/
public class PermissionFilter extends AccessControlFilter {

    //private static Logger logger = Logger.getLogger(PermissionFilter.class);

    private String noPermissionUrl;

    public String getNoPermissionUrl() {
        return noPermissionUrl;
    }

    public void setNoPermissionUrl(String noPermissionUrl) {
        this.noPermissionUrl = noPermissionUrl;
    }

	/**
     * 请求被允许时调用的方法
     * @param requesServletRequestt, ServletResponse, Object
	 * @return boolean
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        String pers[] = (String[]) mappedValue;
        if (pers == null || pers.length <= 0) {
            return true;
        }
        Subject subject = SecurityUtils.getSubject();

        boolean permittedAll = true;

        for (String ps : pers) {
            String[] parray = ps.split("-");
            boolean isPermitted = false;
            for (String s : parray) {
                isPermitted = isPermitted || subject.isPermitted(s);
            }
            permittedAll = permittedAll && isPermitted;
        }
        if (permittedAll) {
            return true;
        }
        return false;
    }

    /**
     * 请求被拒绝时调用的方法
     * @param requesServletRequestt, ServletResponse
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpResponse.setCharacterEncoding("UTF-8");
        PrintWriter writer = httpResponse.getWriter();
        ResultVO resultVO = new ResultVO();
        resultVO.setOk(false);
        resultVO.setMsg("No permission");
        writer.write(JSON.toJSONString(resultVO));
        return false;
    }
}

