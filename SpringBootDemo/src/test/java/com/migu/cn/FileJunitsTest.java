package com.migu.cn;

import javax.annotation.Resource;

import com.migu.cn.annotation.ElasticSearchId;
import com.migu.cn.annotation.ElasticsearchAnnotation;
import com.migu.cn.elasticsearch.Article;
import com.migu.cn.elasticsearch.ArticleService;
import com.migu.cn.kafka.KafkaProducer;
import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ClassFilter;
import net.sf.corn.cps.PackageNameFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.migu.cn.dao.inf.ProjectDOMapper;
import com.migu.cn.service.redis.RedisServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

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

	@Autowired
	private ArticleService articleService;

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
	@Test
	public void testElasticSearch() throws IllegalAccessException {
		Article article=new Article();
		article.setTitle("errordfr123");
		article.setContent("this is error log6789");
		article.setAbstracts("spms_permit_service6789");
		article.setCreatedTime(new Date());
//		ElasticsearchAnnotation.analysisFieldAnnotation(article);
		articleService.save(article);
	}

	@Test
	public void testAnnotation() throws IllegalAccessException {
		Article article=new Article();
//		ElasticsearchAnnotation.analysisFieldAnnotation(article);
		System.out.println(article);
	}

	@Test()
	public void testScanClass(){
		List<Class<?>> classes = CPScanner.scanClasses(
				new PackageNameFilter("com.migu.cn.*"));
		for(Class<?> clazz: classes){
			if(clazz.isAnnotationPresent(Document.class)){
				System.out.println(clazz.getName());
			}
		}
	}


}
