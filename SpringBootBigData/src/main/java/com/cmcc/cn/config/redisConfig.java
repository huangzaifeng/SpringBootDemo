package com.cmcc.cn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by le on 2017/10/16.
 */
@Configuration
public class redisConfig {

    @Bean(name= "jedis.pool")
    public JedisPool jedisPool(
            @Qualifier("jedis.pool.config") JedisPoolConfig config,
            @Value("${jedis.pool.host}")String host,
            @Value("${jedis.pool.port}")int port) {
        return new JedisPool(config, host, port);
    }

    @Bean(name = "redisTemplate")
    public StringRedisTemplate redisTemplate(
            @Value("${jedis.pool.host}") String hostName,
            @Value("${jedis.pool.port}") int port,
            @Value("${jedis.pool.password}") String password,
            @Value("${jedis.pool.config.maxIdle}") int maxIdle,
            @Value("${jedis.pool.config.maxTotal}") int maxTotal,
            @Value("${jedis.pool.index}") int index,
            @Value("${jedis.pool.config.maxWaitMillis}") long maxWaitMillis){
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(connectionFactory(hostName, port, password,
                maxIdle, maxTotal, index, maxWaitMillis));
        return temple;
    }

    @Bean(name= "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig (
            @Value("${jedis.pool.config.maxTotal}")int maxTotal,
            @Value("${jedis.pool.config.maxIdle}")int maxIdle,
            @Value("${jedis.pool.config.maxWaitMillis}")long maxWaitMillis) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        return config;
    }

    public RedisConnectionFactory connectionFactory(
            String hostName, int port,
            String password, int maxIdle, int maxTotal, int index,
            long maxWaitMillis) {
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName(hostName);
        jedis.setPort(port);
        if (!StringUtils.isEmpty(password)) {
            jedis.setPassword(password);
        }
        if (index != 0) {
            jedis.setDatabase(index);
        }
        jedis.setPoolConfig(jedisPoolConfig(maxIdle, maxTotal, maxWaitMillis));
        // 初始化连接pool
        jedis.afterPropertiesSet();
        RedisConnectionFactory factory = jedis;
        return factory;
    }
}
