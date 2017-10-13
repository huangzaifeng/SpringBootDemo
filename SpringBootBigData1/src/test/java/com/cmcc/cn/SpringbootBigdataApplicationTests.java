package com.cmcc.cn;

import com.cmcc.cn.bean.Student;
import com.cmcc.cn.service.hbase.inf.HbaseIOperateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootBigdataApplicationTests {

	@Resource(name = "hbaseOperateService")
	private HbaseIOperateService hbaseIOperateService;


	@Test
	public void testElasticSearch() throws Exception {
		System.out.println("---------------");
		Student student=new Student();
		student.setName("lile");
//		hbaseIOperateService.insertHbase(student);
		System.out.println("-------------11111");
	}




}
