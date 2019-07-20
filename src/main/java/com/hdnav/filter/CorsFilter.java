package com.hdnav.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
@Component 
public class CorsFilter implements Filter{

	final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CorsFilter.class);  
	  
	  
	  
    @Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {  
        HttpServletResponse response = (HttpServletResponse) res;  
  
        HttpServletRequest reqs = (HttpServletRequest) req;  
  
        response.setHeader("Access-Control-Allow-Origin",reqs.getHeader("Origin"));  
        response.setHeader("Access-Control-Allow-Credentials", "true");  
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
        response.setHeader("Access-Control-Max-Age", "3600");  
        response.setHeader( "Access-Control-Allow-Headers","Authorization,Origin, X-Requested-With, Content-Type, Accept");    
        chain.doFilter(req, res);  
    }  
    @Override
	public void init(FilterConfig filterConfig) {}  
    @Override
	public void destroy() {}  

}
