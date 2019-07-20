package com.hdnav.shiro;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import com.hdnav.shiro.distributed.RedisManager;


public class MySessionListener implements SessionListener { 
	
	private String keyPrefix = "shiro_redis_session:";
	
	private RedisManager redisManager;
	
    @Override  
    public void onStart(Session session) {//会话创建时触发  
        //System.out.println("会话创建：" + session.getId());
        //System.out.println("会话过期时间：" + session.getTimeout());  
    }  
    @Override  
    public void onExpiration(Session session) {//会话过期时触发
//    	redisManager.del(SecurityUtils.getSubject().getSession(false).getId());
		redisManager.del(this.getByteKey(session.getId()));
        System.out.println("会话过期：" + session.getId());
    }  
    @Override  
    public void onStop(Session session) {//退出/会话过期时触发  
        	System.out.println("会话停止：" + session.getId());  
    }    
    
    /**
	 * @param key
	 * @return
	 */
	private byte[] getByteKey(Serializable sessionId){
		String preKey = this.keyPrefix + sessionId;
		return preKey.getBytes();
	}
	
	public RedisManager getRedisManager() {
		return redisManager;
	}

	public void setRedisManager(RedisManager redisManager) {
		this.redisManager = redisManager;
		this.redisManager.init();
	}
}  
