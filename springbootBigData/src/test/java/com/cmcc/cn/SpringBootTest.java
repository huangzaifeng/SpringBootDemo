package com.cmcc.cn;

import com.cmcc.cn.annotation.ParseAnnotationService;
import com.cmcc.cn.bean.Article;
import com.cmcc.cn.bean.Student;
import com.cmcc.cn.service.elasticsearch.ElasticSearchOperateService;
import com.cmcc.cn.service.hbase.inf.HbaseIOperateService;
import com.cmcc.cn.service.redis.RedisIService;
import com.cmcc.cn.service.storm.StormOperateServer;
import org.apache.storm.Config;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by le on 2017/10/13.
 */
@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTest {
//    @Resource(name = "hbaseOperateService")
//    private HbaseIOperateService hbaseIOperateService;

//    @Resource(name = "parseAnnotationService")
//    private ParseAnnotationService parseAnnotationService;

//    @Resource(name = "stormOperateService")
//    private StormOperateServer stormOperateServer;

//    @Resource(name = "redisService")
//    private RedisIService redisIService;

//    @Test
//    public void testStorm() throws Exception {
//        Config conf=new Config();
//        conf.setNumAckers(1);
//        conf.setDebug(true);
//        conf.setMaxTaskParallelism(2);
//        stormOperateServer.autoBuildStormTopology("SpringStorm",conf,"local");
//    }

    @Resource(name = "elasticSearchOperateService")
    private ElasticSearchOperateService elasticSearchOperateService;

    @Test
    public  void testRedis() throws Exception {
        Article article=new Article();
        article.setPageSize(3);
        article.setPageNo(1);
//        article.setId("124");
//        article.setTitle("功能");
        article.setContext("错误");
//        article.setDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ").format(new Date()));
//        elasticSearchOperateService.save(article);
        System.out.println(elasticSearchOperateService.search(article));
    }

}
