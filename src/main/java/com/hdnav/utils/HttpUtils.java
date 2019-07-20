package com.hdnav.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	public static String sendPostRequest(Map<String, Object> params, String url, HttpServletRequest request){
		//获取可关闭的 httpCilent
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //配置超时时间
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000).setRedirectsEnabled(true).build();
        HttpPost httpPost = new HttpPost(url);
        //设置超时时间
        httpPost.setConfig(requestConfig);
        //装配post请求参数
        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
        	list.add(new BasicNameValuePair(entry.getKey(), (String) entry.getValue()));  //请求参数
        }
        String strResult = "";
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"UTF-8"); 
            //设置post求情参数
            httpPost.setEntity(entity);
            httpPost.addHeader("token", Token.generateToken());
            if(request != null){
	            Enumeration<?> headerNames = request.getHeaderNames();  
	            while (headerNames.hasMoreElements()) { 
	              String headerName = (String) headerNames.nextElement();         
	              String headerValue = request.getHeader(headerName);  
	              if (headerValue.length() != 0) {  
	            	  httpPost.addHeader(headerName, headerValue);  
	              }
	            }
            }
            httpPost.removeHeaders(HTTP.CONTENT_LEN);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if(httpResponse != null){ 
                System.out.println(httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                	System.out.println(httpResponse.getEntity().getContentType());
                	System.out.println(httpResponse.getEntity().getContentLength());
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                } else if (httpResponse.getStatusLine().getStatusCode() == 500) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                } else {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                } 
            }else{
                 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(httpClient != null){
                    httpClient.close(); //释放资源
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strResult;
	}
	
	
	
	public static String sendGetRequest(String param, String url, HttpServletRequest request){
		//获取可关闭的 httpCilent
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //配置超时时间
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000).setRedirectsEnabled(true).build();
        HttpGet httpGet = new HttpGet(url);
        //设置超时时间
        httpGet.setConfig(requestConfig);
        httpGet.addHeader("token", Token.generateToken());
        String strResult = "";
        try {
        	if(request != null){
        		httpGet.removeHeaders(HTTP.CONTENT_LEN);
	            Enumeration<?> headerNames = request.getHeaderNames();  
	            while (headerNames.hasMoreElements()) { 
	              String headerName = (String) headerNames.nextElement();         
	              String headerValue = request.getHeader(headerName);  
	              if (headerValue.length() != 0) {  
	            	  httpGet.addHeader(headerName, headerValue);  
	              }
	            }
            }
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if(httpResponse != null){ 
                System.out.println(httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                	System.out.println(httpResponse.getEntity().getContentType());
                	System.out.println(httpResponse.getEntity().getContentLength());
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                } else if (httpResponse.getStatusLine().getStatusCode() == 500) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                } else {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                } 
            }else{
                 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(httpClient != null){
                    httpClient.close(); //释放资源
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strResult;
	}
	

}
