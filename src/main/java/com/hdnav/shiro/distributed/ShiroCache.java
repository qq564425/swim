/*package com.shiro.shiro.distributed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import com.shiro.shiro.RedisManager;
*//**
* @author 作者 huangxinyu
* @version 创建时间：2018年1月8日 下午9:33:23
* cache共享
*//*
@SuppressWarnings("unchecked")
public class ShiroCache<K, V> implements Cache<K, V> {
    @Autowired
    private RedisManager redisManager;
    //private static final String REDIS_SHIRO_CACHE = "shiro_redis_session:";
    private static final String REDIS_SHIRO_CACHE = "shiro_redis_cache:";
    private String cacheKey;
    private long globExpire = 30;
    
    @SuppressWarnings("rawtypes")
    public ShiroCache(String name) {
    	this.cacheKey = REDIS_SHIRO_CACHE + name + ":";
    }
    
    @Override
    public V get(K key) throws CacheException {
		Object obj = null;
		try {
			obj = redisManager.get(getCacheKey(key), 1);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		if(obj==null){
		return null;
		}
		return (V) KyroUtil.deserialization((String)obj);
	}
    
	@Override
	public V put(K key, V value) throws CacheException {
		V old = get(key);
		try {
			redisManager.set(getCacheKey(key), value, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return old;
	}
	
	@Override
	public V remove(K key) throws CacheException {
		V old = get(key);
		try {
			redisManager.del(getCacheKey(key), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return old;
	}
		
	@Override
	public void clear() throws CacheException {
		for(String key : (Set<String>)keys()){
			try {
				redisManager.del(key, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public int size() {
		return keys().size();
	}
	
	@Override
	public Set<K> keys() {
		try {
			return (Set<K>) redisManager.getFuzzyKeys(getCacheKey("*"), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Collection<V> values() {
		Set<K> set = keys();
		List<V> list = new ArrayList<>();
		for (K s : set) {
			list.add(get(s));
		}
		return list;
	}

	private K getCacheKey(Object k) {
		return (K) (this.cacheKey + k);
	}
}*/