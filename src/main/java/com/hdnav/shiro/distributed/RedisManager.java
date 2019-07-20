package com.hdnav.shiro.distributed;

import java.io.IOException;
import java.util.Set;

import com.hdnav.utils.PropertiesUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {        
    // 0 - never expire  
    private int expire = 30*60;  
      
    //timeout for jedis try to connect to redis server, not expire time! In milliseconds  
    private int timeout = 0;  
      
    private String password = "root";  
      
    private static JedisPool jedisPool = null;  
      
    
    /** 
     * 初始化方法 
     */  
    public void init(){
    	PropertiesUtil p;
    	try {
		  p = new PropertiesUtil();
		  String host = p.readAllProperties().get("redisHost");
		  int port = Integer.parseInt(p.readAllProperties().get("redisPort"));
		  if(jedisPool == null){  
	            if(password != null && !"".equals(password)){  
	                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password,1);  
	            }else if(timeout != 0){  
	                jedisPool = new JedisPool(new JedisPoolConfig(), host, port,timeout);  
	            }else{  
	                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, null, 1);  
	            }  
	              
	        }  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
      
    /**
	 * get value from redis
	 * @param key
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public byte[] get(byte[] key){
		byte[] value = null;
		Jedis jedis = jedisPool.getResource();
		jedis.select(1);
		try{
			value = jedis.get(key);
		}finally{
			jedisPool.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * set 
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public byte[] set(byte[] key,byte[] value){
		Jedis jedis = jedisPool.getResource();
		jedis.select(1);
		try{
			jedis.set(key,value);
			if(this.expire != 0){
				jedis.expire(key, this.expire);
		 	}
		}finally{
			jedisPool.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * set 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public byte[] set(byte[] key,byte[] value,int expire){
		Jedis jedis = jedisPool.getResource();
		jedis.select(1);
		try{
			jedis.set(key,value);
			if(expire != 0){
				jedis.expire(key, expire);
		 	}
		}finally{
			jedisPool.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * del
	 * @param key
	 */
	@SuppressWarnings("deprecation")
	public void del(byte[] key){
		Jedis jedis = jedisPool.getResource();
		jedis.select(1);
		try{
			jedis.del(key);
		}finally{
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * flush
	 */
	@SuppressWarnings("deprecation")
	public void flushDB(){
		Jedis jedis = jedisPool.getResource();
		jedis.select(1);
		try{
			jedis.flushDB();
		}finally{
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * size
	 */
	@SuppressWarnings("deprecation")
	public Long dbSize(){
		Long dbSize = 0L;
		Jedis jedis = jedisPool.getResource();
		jedis.select(1);
		try{
			dbSize = jedis.dbSize();
		}finally{
			jedisPool.returnResource(jedis);
		}
		return dbSize;
	}

	/**
	 * keys
	 * @param regex
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Set<byte[]> keys(String pattern){
		Set<byte[]> keys = null;
		Jedis jedis = jedisPool.getResource();
		jedis.select(1);
		try{
			keys = jedis.keys(pattern.getBytes());
		}finally{
			jedisPool.returnResource(jedis);
		}
		return keys;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
