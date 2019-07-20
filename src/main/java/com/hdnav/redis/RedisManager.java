package com.hdnav.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdnav.utils.DataUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by sxjun on 15-9-8.
 */
@Service("authRedisManager")
public class RedisManager {
	
    @Autowired
	private JedisPool jedisPool;

    public void publish(String key,String val){
        Jedis jedis = jedisPool.getResource();
        try {
             jedis.publish(key,val);
        } finally {
            jedis.close();
        }
    }


/*    public Long set(Object key, Object val, int dbIndex) throws IOException {
        Jedis jedis = jedisPool.getResource();
        jedis.select(dbIndex);
        try {
            // jedis.set(DataUtil.getBytesFromObject(key), DataUtil.getBytesFromObject(val));
             return jedis.hset(DataUtil.getBytesFromObject(key), DataUtil.getBytesFromObject("hostip"), DataUtil.getBytesFromObject(val));
        } finally {
            jedis.close();
        }
    }*/
    
    
    public String set(Object key, Object val, int dbIndex) throws IOException {
        Jedis jedis = jedisPool.getResource();
        jedis.select(dbIndex);
        try {
             return jedis.set(DataUtil.getBytesFromObject(key), DataUtil.getBytesFromObject(val));
        } finally {
            jedis.close();
        }
    }
    
    public Object get(Object key, int dbIndex) throws IOException, ClassNotFoundException {
        Jedis jedis = jedisPool.getResource();
        jedis.select(dbIndex);
        try {

            byte[] bytes = jedis.get(DataUtil.getBytesFromObject(key));
            //byte[] bytess = jedis.hget(DataUtil.getBytesFromObject(key), DataUtil.getBytesFromObject("hostip"));
            return DataUtil.getObjectFromByteArray(bytes);
        } finally {
            jedis.close();
        }
    }

    public Long del(Object key, int dbIndex) throws IOException {
        Jedis jedis = jedisPool.getResource();
        jedis.select(dbIndex);
        try {
            return jedis.del(DataUtil.getBytesFromObject(key));
        } finally {
            jedis.close();
        }
    }

    public Set<byte[]> getFuzzyKeys(Object key, int dbIndex) throws IOException {
        Jedis jedis = jedisPool.getResource();
        jedis.select(dbIndex);
        try {
            return jedis.keys(DataUtil.getBytesFromObject(key));
        } finally {
            jedis.close();
        }
    }

    public void flushDb(int dbIndex) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(dbIndex);
        try {
            jedis.flushDB();
        } finally {
            jedis.close();
        }
    }

    public Long size(int dbIndex) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(dbIndex);
        try {
            return jedis.dbSize();
        } finally {
            jedis.close();
        }
    }

    public void flushAll() {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.flushAll();
        } finally {
            jedis.close();
        }
    }

    public List<Object> mget(int dbIndex, byte[]... keys) throws IOException, ClassNotFoundException {
        List<Object> list = new ArrayList<>();
        Jedis jedis = jedisPool.getResource();
        jedis.select(dbIndex);
        try {
            List<byte[]> bytes = jedis.mget(keys);
            if (bytes == null) {
                return list;
            }
            for (byte[] b : bytes) {
                Object o = DataUtil.getObjectFromByteArray(b);
                list.add(o);
            }
        } finally {
            jedis.close();
        }
        return list;
    }

/*    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }*/
}

