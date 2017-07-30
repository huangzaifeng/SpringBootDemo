package com.migu.cn;

import javax.annotation.Resource;

import com.migu.cn.dao.entity.RedisModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.migu.cn.dao.inf.ProjectDOMapper;
import com.migu.cn.service.redis.RedisServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=StartProcess.class)
public class FileJunitsTest {
	@Resource(name = "ProjectDOMapper")
	private ProjectDOMapper projectDOMapper;
	@Resource(name="RedisServiceImpl")
	private RedisServiceImpl redisService;

	@Test
	public void test(){
		System.out.println(projectDOMapper.selectByPrimaryKey(12).getName());
	}
	
   @Test
	public void testRedis(){
//	   RedisModel redisModel=new RedisModel();
//	   redisModel.setRedisKey("lile04");
//	   redisModel.setName("lile4");
//	   redisModel.setAddress("119.23.12.2333");
//	   redisService.put(redisModel.getRedisKey(),redisModel,10);
	   System.out.println(redisService.getAll());
   }


}
