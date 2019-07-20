package com.hdnav.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "server")
@PropertySource("classpath:config.properties")
public class AuthConfigPropertyUtils {
	//请求转发的Ip
	public static String forwardIp;
	//请求转发的Port
    public static String forwardPort;
    //跳转登录的Ip
    public static String ip;
    //跳转登录的Port
    public static String port;
    //websocketPort
    public static String websocketPort;
    //跳转登录的Url
    public static String loginUrl;
    //登陆成功跳转主页的Url
    public static String indexUrl;
    
	public static String getForwardIp() {
		return forwardIp;
	}
	public static void setForwardIp(String forwardIp) {
		AuthConfigPropertyUtils.forwardIp = forwardIp;
	}
	public static String getForwardPort() {
		return forwardPort;
	}
	public static void setForwardPort(String forwardPort) {
		AuthConfigPropertyUtils.forwardPort = forwardPort;
	}
	public static String getLoginUrl() {
		return loginUrl;
	}
	public static void setLoginUrl(String loginUrl) {
		AuthConfigPropertyUtils.loginUrl = loginUrl;
	}
	public static String getIndexUrl() {
		return indexUrl;
	}
	public static void setIndexUrl(String indexUrl) {
		AuthConfigPropertyUtils.indexUrl = indexUrl;
	}
	public static String getIp() {
		return ip;
	}
	public static void setIp(String ip) {
		AuthConfigPropertyUtils.ip = ip;
	}
	public static String getPort() {
		return port;
	}
	public static void setPort(String port) {
		AuthConfigPropertyUtils.port = port;
	}
	public static String getWebsocketPort() {
		return websocketPort;
	}
	public static void setWebsocketPort(String websocketPort) {
		AuthConfigPropertyUtils.websocketPort = websocketPort;
	}
     
}
