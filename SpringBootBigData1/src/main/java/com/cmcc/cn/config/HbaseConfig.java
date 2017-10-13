package com.cmcc.cn.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * Created by le on 2017/10/12.
 */
@org.springframework.context.annotation.Configuration
public class HbaseConfig {

    @Value("${spring.hbase.zookeeper.quorum}")
    private String zookeeperQuorum;

    @Value("${spring.hbase.zookeeper.clientPort}")
    private String clientPort;

    @Bean(name = "hbaseConfiguration")
    public Configuration conf(){
       // 取得一个数据库连接的配置参数对象
       Configuration conf = HBaseConfiguration.create();
       // 设置连接参数：HBase数据库所在的主机IP
       conf.set("hbase.zookeeper.quorum", zookeeperQuorum);
       // 设置连接参数：HBase数据库使用的端口
       if(StringUtils.isEmpty(clientPort)){
           clientPort="2181";
       }
       conf.set("hbase.zookeeper.property.clientPort", clientPort);
       return conf;
   }
}
