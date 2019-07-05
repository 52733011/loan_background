package com.xiaochong.loan.background.dao;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by ray.liu on 2017/5/2.
 */
@Component("redisDao")
public class RedisDao {

    @Resource
    private RedisTemplate<String, ?> redisTemplate;


    public boolean set(final String key, final String value) {
        return this.set(key,value,7200L);
    }

    public boolean set(final String key, final String value,long second) {
        boolean result = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            connection.set(serializer.serialize(key), serializer.serialize(value));
            connection.expire(serializer.serialize(key), second);
            return true;
        });
        return result;
    }

    public String get(final String key){
        String result = redisTemplate.execute((RedisCallback<String>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] value =  connection.get(serializer.serialize(key));
            return serializer.deserialize(value);
        });
        return result;
    }

    public void delete(final String key){
        redisTemplate.execute((RedisCallback<Boolean>) connection ->{
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            connection.del(serializer.serialize(key));
            return null;
        });
    }



}
