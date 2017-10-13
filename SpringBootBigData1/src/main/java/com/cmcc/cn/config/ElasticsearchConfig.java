package com.cmcc.cn.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * Created by le on 2017/9/9.
 */
@Component
public class ElasticsearchConfig {
    //集群机器的ip地址
   @Value("${spring.data.elasticsearch.cluster-nodes}")
   private  String esHosts;
   //集群通信端口
   private  Integer port=9300;
   //集群名称
   @Value("${spring.data.elasticsearch.cluster-name}")
   private  String EsClusterName;

   private  Client client=null;

  @Bean(name="esClient")
   public Client getEsClient(){
      TransportClient client = null;
      try {
          Settings settings = Settings.builder()
                  .put("cluster.name", EsClusterName)
                  .put("client.transport.sniff", true)
                  .put("client.transport.ping_timeout", "10s")
                  .build();
           client = new PreBuiltTransportClient(settings);
           String[] hosts=esHosts.split(",");
           for(String host : hosts){
              client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),port));
           }
      }catch (Exception e) {
          e.printStackTrace();
      }
      return client;
   }
}
