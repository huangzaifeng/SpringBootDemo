package com.cmcc.cn.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * Created by le on 2017/10/16.
 */
@Service("redisService")
public class RedisServiceImpl implements RedisIService {

    @Resource(name = "jedis.pool")
    private JedisPool jedisPool;

    @Resource(name = "redisTemplate")
    private StringRedisTemplate redisTemplate;

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key,value);
//       jedisPool.getResource().set(key,value);
    }
}
