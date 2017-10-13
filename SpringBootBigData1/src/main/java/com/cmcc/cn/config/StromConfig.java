package com.cmcc.cn.config;

import com.cmcc.cn.enums.Constants;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by le on 2017/9/27.
 */
@Component
public class StromConfig {

    @Value("${spring.storm.zookeeperCluster}")
    private String zookeeper;

    @Value("${spring.storm.kafka.topic}")
    private String kafkaTopic;

    @Value("${spring.storm.zkRoot}")
    private String zkRoot;

    @Value("${spring.storm.zkSpoutId}")
    private String zkSpoutId;

    @Value("${spring.storm.zookeeperName}")
    private String zookeeperName;

    @Bean(name = "stormClient")
    public KafkaSpout getStormClient(){
        ZkHosts zkHosts = new ZkHosts(zookeeper);
        SpoutConfig spoutConfig = new SpoutConfig(zkHosts,kafkaTopic,zkRoot ,zkSpoutId);
        spoutConfig.zkPort=2181;
        spoutConfig.zkRoot=zkRoot;
        spoutConfig.id=zkSpoutId;
        String[] zkServers=zookeeperName.split(",");
        spoutConfig.zkServers= Arrays.asList(zkServers);
        spoutConfig.scheme=new SchemeAsMultiScheme(new StringScheme());
        //从消费者的最早消息开始读
//        spoutConfig.maxOffsetBehind=Long.MIN_VALUE;
        //-2 从kafka头开始  -1 是从最新的开始 0 =无 从ZK开始
        spoutConfig.startOffsetTime=-1;
//        spoutConfig.useStartOffsetTimeIfOffsetOutOfRange=true;
        spoutConfig.fetchMaxWait=5000;
        spoutConfig.fetchSizeBytes=1024*1024;
        return new KafkaSpout(spoutConfig);
    }
}
