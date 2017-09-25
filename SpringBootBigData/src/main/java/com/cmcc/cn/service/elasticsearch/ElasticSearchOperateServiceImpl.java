package com.cmcc.cn.service.elasticsearch;

import com.cmcc.cn.annotation.ParseAnnotationService;
import com.cmcc.cn.annotation.elasticsearch.ElasticSearchDocument;
import com.cmcc.cn.annotation.elasticsearch.ElasticSearchId;
import com.cmcc.cn.config.ElasticsearchConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.PackageNameFilter;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by le on 2017/9/24.
 */

@Service("elasticSearchOperateService")
public class ElasticSearchOperateServiceImpl implements ElasticSearchOperateService {

    private static Logger logger = LoggerFactory.getLogger(ElasticSearchOperateServiceImpl.class);

    private String index="migu";

    private String type="errorLog";

    @Resource(name = "esClient")
    private Client client;

    @Autowired
    private ParseAnnotationService annotationService;

    @Override
    public String gainEsClusterInformation() {
        return  client.admin().cluster().prepareHealth().get().toString();
    }

    @Override
    public boolean isIndexExists(String index) {
        if(Objects.equals(client, null)){
            logger.info("--------- IndexAPI isIndexExists 请求客户端为null");
            return false;
        }
        if(StringUtils.isEmpty(index)){
            logger.info("--------- IndexAPI isIndexExists 索引名称为空");
            return false;
        }
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        IndicesExistsResponse response = indicesAdminClient.prepareExists(index).get();
        logger.info(String.valueOf(response.isExists()));
        System.out.println(String.valueOf(response.isExists()));
        return response.isExists();
    }

    @Override
    public boolean isTypeExists(String index, String type) {
        if(!isIndexExists(index)){
            logger.info("--------- isTypeExists 索引 [{}] 不存在",index);
            return false;
        }else{
            IndicesAdminClient indicesAdminClient = client.admin().indices();
            TypesExistsResponse response = indicesAdminClient.prepareTypesExists(index).setTypes(type).get();
            logger.info(String.valueOf(response.isExists()));
            System.out.println(String.valueOf(response.isExists()));
            return response.isExists();
        }
    }

    @Override
    public <T> boolean createIndex(T Class) throws IllegalAccessException, IOException {
       //获取类的所有注解
        short shards=1;
        short replicas=1;
        Annotation[]  annotations=Class.getClass().getAnnotatedSuperclass().getAnnotations();
        for(Annotation annotation:annotations){
            if(annotation instanceof ElasticSearchDocument){
                ElasticSearchDocument documentAnnotation=(ElasticSearchDocument) annotation;
                 index=documentAnnotation.indexName();
                 type=documentAnnotation.type();
                 shards=documentAnnotation.shards();
                 replicas=documentAnnotation.replicas();
            }
        }
      if(isIndexExists(index)&&isTypeExists(index,type)){
          logger.info("elasticsearch集群已存在索引【{}】或类型【{}】",index,type);
          return false;
      }else{
          // 创建settings
          Settings settings = Settings.builder()
                  .put("index.number_of_shards", shards)
                  .put("index.number_of_replicas", replicas)
                  .build();
          ObjectMapper mapper = new ObjectMapper();
          byte[] json=mapper.writeValueAsBytes(Class);
          //创建mapper
          XContentBuilder mappingBuilder=null;
          try {
               mappingBuilder=XContentFactory.jsonBuilder()
                       .startObject()
                         .startObject(type)
                            .startObject("properties")
                              .startObject("title").field("type", "string").field("store", "yes").endObject()
                              .startObject("context").field("type", "string").field("store", "yes").endObject()
                              .startObject("date").field("type", "date").field("store", "yes").endObject()
                            .endObject()
                          .endObject()
                       .endObject();
          }catch (Exception e){
              logger.error("创建索引异常。。。");
          }
          IndicesAdminClient indicesAdminClient = client.admin().indices();
          PutMappingResponse response = indicesAdminClient
                  .preparePutMapping(index)
                  .setType(type)
//                  .setSource(json, XContentType.JSON)
                  .setSource(mappingBuilder)
                  .get();
          return response.isAcknowledged();
      }
    }

    @Override
    public <T> void save(T Class) throws Exception {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        Map<String,Object> resource=annotationService.gainFieldValue(Class);
        Field[]  fields=Class.getClass().getDeclaredFields();
        /*检查Class属性中ElasticsearchId是否为空*/
        String elasticSearchIdIsNull=gainElasticsearchIdProperties(Class);
        /*判断类属性中id是否为空*/
        if(!StringUtils.isEmpty(elasticSearchIdIsNull)){
            bulkRequest.add(client.prepareIndex(index,type,elasticSearchIdIsNull).setSource(resource));
        }else{
            bulkRequest.add(client.prepareIndex(index,type).setSource(resource));
        }
        BulkResponse bulkResponse=bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            logger.error("批量创建索引错误！");
        }
    }

    @Override
    public <T> void save(List<T> Class) throws Exception {
        boolean  flag=true;
        BulkProcessor bulkProcessor =BulkProcessor.builder(client, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {

            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
               if(bulkResponse.hasFailures()){
                   logger.error("批量插入失败");
               }
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {

            }
        }).setBulkActions(1000)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .setBackoffPolicy( BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();

        for(T obj:Class){
        /*判断类属性中id是否为空*/
            String elasticSearchIdIsNull=gainElasticsearchIdProperties(Class);
            if(!StringUtils.isEmpty(elasticSearchIdIsNull)){
                bulkProcessor.add(new IndexRequest(index,type,elasticSearchIdIsNull).source(annotationService.gainFieldValue(obj)));
            }else{
                bulkProcessor.add(new IndexRequest(index,type).source(annotationService.gainFieldValue(obj)));
            }
        }
        bulkProcessor.flush();
        bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
    }

    @PostConstruct
    private void init() throws Exception{
        List<Class<?>> classes = CPScanner.scanClasses(
                new PackageNameFilter("com.cmcc.cn.*"));
        /*获取ElasticSearchDocument注解*/
        for(Class<?> clazz: classes) {
            if (clazz.isAnnotationPresent(ElasticSearchDocument.class)) {
                ElasticSearchDocument documentAnnotation = clazz.getAnnotation(ElasticSearchDocument.class);
                index = documentAnnotation.indexName();
                type = documentAnnotation.type();
                /*初始化创建索引*/
                createIndex(clazz);
            }
        }
    }

    private <T> String  gainElasticsearchIdProperties(T clazz){
        /*检查Class属性中ElasticsearchId是否为空*/
        Field[] fields=clazz.getClass().getDeclaredFields();
        String elasticSearchIdIsNull="Y";
        String elasticSearchId=null;
        for(Field field:fields){
            field.setAccessible(true);
            if(field.getAnnotation(ElasticSearchId.class)!=null){
                ElasticSearchId elasticSearchIdAnnotation=field.getAnnotation(ElasticSearchId.class);
                String isNull=elasticSearchIdAnnotation.isNull();
                if(isNull.equals("N")){
                    elasticSearchId=elasticSearchIdAnnotation.id();
                }
            }
        }
        return elasticSearchId;
    }

}
