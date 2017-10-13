package com.cmcc.cn;

import com.cmcc.cn.annotation.ParseAnnotationService;
import com.cmcc.cn.bean.Student;
import com.cmcc.cn.service.hbase.inf.HbaseIOperateService;
import com.cmcc.cn.service.storm.StormOperateServer;
import org.apache.storm.Config;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by le on 2017/10/13.
 */
@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTest {
    @Resource(name = "hbaseOperateService")
    private HbaseIOperateService hbaseIOperateService;

    @Resource(name = "parseAnnotationService")
    private ParseAnnotationService parseAnnotationService;

    @Resource(name = "stormOperateService")
    private StormOperateServer stormOperateServer;

    @Test
    public void contextLoads() throws Exception {
        System.out.println("---------------");
        Student student=new Student();
        student.setRowKey("li1");
        String[] rowKeys={"li1"};
        student.setRowKeys(Arrays.asList(rowKeys));
        System.out.println(hbaseIOperateService.getHbaseList(student));
        System.out.println("-------------11111");
    }

    @Test
    public void testStorm() throws Exception {
        Config conf=new Config();
        conf.setNumAckers(1);
        conf.setDebug(true);
        conf.setMaxTaskParallelism(2);
        stormOperateServer.autoBuildStormTopology("SpringStorm",conf,"local");
    }

}
