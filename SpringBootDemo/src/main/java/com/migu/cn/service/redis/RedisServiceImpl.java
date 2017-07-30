package com.migu.cn.service.redis;

import org.springframework.stereotype.Service;

import com.migu.cn.dao.entity.RedisModel;
@Service("RedisServiceImpl")
public class RedisServiceImpl extends IRedisService<RedisModel> {
	 private static final String REDIS_KEY = "TEST_REDIS_KEY";

	@Override
	protected String getRedisKey() {
		// TODO Auto-generated method stub
		return this.REDIS_KEY;
	}

}
