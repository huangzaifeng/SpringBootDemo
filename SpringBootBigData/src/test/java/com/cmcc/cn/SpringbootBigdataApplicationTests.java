package com.cmcc.cn;

import com.cmcc.cn.annotation.ParseAnnotationService;
import com.cmcc.cn.bean.Article;
import com.cmcc.cn.service.elasticsearch.ElasticSearchOperateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootBigdataApplicationTests {

	@Autowired
	private ElasticSearchOperateService elasticSearchOperateService;

	@Autowired
	private ParseAnnotationService annotationService;


	@Test
	public void contextLoads() {
	}

	@Test
	public void testElasticSearch(){
		System.out.println("---------------");
		System.out.println(elasticSearchOperateService.gainEsClusterInformation());
		System.out.println("---------------");
	}

	@Test
	public void testParseAnnotation() throws Exception {
		List<Article> articleList=new ArrayList<Article>();
		Article article=new Article();
		article.setTitle("spms_permit_service1");
		article.setContext("this is error logs");
		article.setDate(new Date());
		articleList.add(article);
		elasticSearchOperateService.save(articleList);
	}

}
