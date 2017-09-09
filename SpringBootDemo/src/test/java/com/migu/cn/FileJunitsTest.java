package com.migu.cn;

import javax.annotation.Resource;

import com.migu.cn.dao.entity.RedisModel;
import com.migu.cn.kafka.KafkaProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
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

	@Autowired
	private KafkaProducer kafkaProducer;

	@Autowired
	private KafkaTemplate<String,String> kafkaTemplate;

	@Test
	public void test(){
		System.out.println(projectDOMapper.selectByPrimaryKey(12).getName());
	}
	
    @Test
	public void testKafka(){
//		kafkaTemplate.send("spms","李乐测试kakfa123");
		kafkaProducer.send("lile","xxx");
		System.out.println("发送成功");

	}


}
