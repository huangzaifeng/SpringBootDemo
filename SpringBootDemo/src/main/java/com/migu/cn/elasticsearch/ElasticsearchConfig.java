package com.migu.cn.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import static com.oracle.jrockit.jfr.ContentType.Bytes;

/**
 * Created by le on 2017/9/9.
 */
@Configurable
@EnableElasticsearchRepositories
public class ElasticsearchConfig {
    //集群机器的ip地址
   private static String[] esHosts = {"192.168.0.105","192.168.0.116"};
   private static Integer port=9300;
   private static String EsClusterName="spms";
   private static Client client=null;

//   public static TransportClient client(){
//       TransportClient client = null;
//       try {
//       Settings settings = Settings.settingsBuilder()
//               .put("cluster.name",EsClusterName )
//               .put("client.transport.sniff", true)
//               .put("client.transport.ping_timeout","120s")
//               .build();
//         client = TransportClient.builder()
//               .settings(settings)
//               .build();
//       for(String host : esHosts){
//           client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),port));
//       }}catch (Exception e) {
//           e.printStackTrace();
//       }
//       return client;
//   }

  @Bean()
   public Client client(){
      TransportClient client = null;
      try {
          Settings settings = Settings.settingsBuilder()
                  .put("cluster.name",EsClusterName )
                  .put("client.transport.sniff", true)
                  .put("client.transport.ping_timeout","120s")
                  .build();
          client = TransportClient.builder()
                  .settings(settings)
                  .build();
          for(String host : esHosts){
              client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),port));
          }}catch (Exception e) {
          e.printStackTrace();
      }
      return client;
   }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }

//    //创建 EsClient
//    private static TransportClient createClient(){
//        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
//        if (client == null) {
//            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
//            synchronized (ElasticsearchConfig.class) {
//                //未初始化，则初始instance变量
//                if (client == null) {
//                    try {
//                        // 设置client.transport.sniff为true来使客户端去嗅探整个集群的状态，把集群中其它机器的ip地址加到客户端中，
//                        Settings settings = Settings.settingsBuilder()
//                                .put("cluster.name",EsClusterName )
//                                .put("client.transport.sniff", true)
//                                .put("client.transport.ping_timeout","120s")
//                                .build();
//                        client = TransportClient.builder()
//                                .settings(settings)
//                                .build();
//                        for(String host : esHosts){
//                            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),port));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return client ;
//                }
//            }
//        }
//        return client;
//
//    }

}
