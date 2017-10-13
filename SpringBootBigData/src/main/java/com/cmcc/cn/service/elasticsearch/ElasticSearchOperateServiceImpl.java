package com.cmcc.cn.service.elasticsearch;

import com.cmcc.cn.annotation.ParseAnnotationService;
import com.cmcc.cn.annotation.elasticsearch.ElasticSearchDocument;
import com.cmcc.cn.annotation.elasticsearch.ElasticSearchId;
import com.cmcc.cn.bean.ElasticsearchBasePage;
import com.cmcc.cn.utils.JsonTool;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.PackageNameFilter;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by le on 2017/9/24.
 */

@Service("elasticSearchOperateService")
public class ElasticSearchOperateServiceImpl extends ElasticSearchService implements ElasticSearchOperateService {

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
    public <T> boolean createIndex(T valueClass) throws IllegalAccessException, IOException {
       //获取类的所有注解
        short shards=1;
        short replicas=1;
        Annotation[]  annotations=valueClass.getClass().getAnnotations();
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
          byte[] json=mapper.writeValueAsBytes(valueClass);
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
    public <T> void save(T valueClass) throws Exception {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        Map<String,Object> resource=annotationService.gainFieldValue(valueClass);
        /*检查Class属性中ElasticsearchId是否为空*/
        Object[] elasticSearchIdIsNull=gainElasticsearchIdProperties(valueClass);
        /*判断类属性中id是否为空*/
        if(!ObjectUtils.isEmpty(elasticSearchIdIsNull)&&"N".equals(elasticSearchIdIsNull[0])){
            bulkRequest.add(client.prepareIndex(index,type,elasticSearchIdIsNull[1].toString()).setSource(resource));
        }else{
            bulkRequest.add(client.prepareIndex(index,type).setSource(resource));
        }
        BulkResponse bulkResponse=bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            logger.error("批量创建索引错误！");
        }
    }

    @Override
    public <T> void save(List<T> valueClass) throws Exception {
        boolean  flag=true;
        BulkProcessor bulkProcessor=bulkProcess(client);
        for(T obj:valueClass){
        /*判断类属性中id是否为空*/
            Object[] elasticSearchIdIsNull=gainElasticsearchIdProperties(obj);
            if(!ObjectUtils.isEmpty(elasticSearchIdIsNull)&&"N".equals(elasticSearchIdIsNull[0])){
                bulkProcessor.add(new IndexRequest(index,type,elasticSearchIdIsNull[1].toString()).source(annotationService.gainFieldValue(obj)));
            }else{
                bulkProcessor.add(new IndexRequest(index,type).source(annotationService.gainFieldValue(obj)));
            }
        }
        bulkProcessor.flush();
        bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
    }

    @Override
    public   <T extends ElasticsearchBasePage> List<T> search(T valueClass) throws Exception {
        Map<String,Object> queryConditions=annotationService.gainFieldValue(valueClass);
        BoolQueryBuilder queryBuilder=QueryBuilders. boolQuery();
        for(Map.Entry<String,Object> entry:queryConditions.entrySet()){
            queryBuilder.must(QueryBuilders.termQuery(entry.getKey(),entry.getValue()));
        }
        /*查询总数量*/
        SearchResponse responseCount = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setExplain(true)
                .get();
        int totalCount=(int)responseCount.getHits().totalHits;
        /*设置分页参数*/
        ElasticsearchBasePage basePage=setBasePage(valueClass,totalCount);
        /*设置获取的起始行*/
        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(basePage.getRowSrt()).setSize(basePage.getRowEnd()+1)
                .setExplain(true)
                .get();

        List<T> beanList = new ArrayList<T>();
        SearchHits searchHits = response.getHits();
        for (SearchHit hit : searchHits) {
            String resource=hit.getSourceAsString();
            T bean = (T) JsonTool.jsonToObject(resource,valueClass.getClass(),true);
            beanList.add(bean);
        }
        return beanList;
    }

    @Override
    public <T> void deleteByCriteria(T valueClass) throws Exception {
        /*获取批量删除工具类*/
        BulkProcessor bulkProcessor=bulkProcess(client);
        /*获取需查询的条件*/
        Map<String,Object> queryConditions=annotationService.gainFieldValue(valueClass);
        BoolQueryBuilder queryBuilder=QueryBuilders. boolQuery();
        for(Map.Entry<String,Object> entry:queryConditions.entrySet()){
            queryBuilder.must(QueryBuilders.matchQuery(entry.getKey(),entry.getValue()));
        }
        /*根据查询条件搜索*/
        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setExplain(true)
                .get();
        /*将搜索结果值添加到批量操作中*/
        SearchHits searchHits = response.getHits();
        for (SearchHit hit : searchHits) {
            bulkProcessor.add(new DeleteRequest(index, type, hit.getId()));
        }
        bulkProcessor.flush();
        bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
    }

//    @PostConstruct
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

    private <T> Object[]  gainElasticsearchIdProperties(T clazz) throws IllegalAccessException {
        /*检查Class属性中ElasticsearchId是否为空*/
        Field[] fields=clazz.getClass().getDeclaredFields();
        String elasticSearchIdIsNull="Y";
        for(Field field:fields){
            String fieldName=field.getName();
            field.setAccessible(true);
            if(field.isAnnotationPresent(ElasticSearchId.class)){
                ElasticSearchId elasticSearchIdAnnotation=field.getAnnotation(ElasticSearchId.class);
                Object id=field.get(clazz);
                if(!ObjectUtils.isEmpty(id)){
                    elasticSearchIdIsNull="N";
                }
                Object[] elasticsearchIdProperties={elasticSearchIdIsNull,id};
                return elasticsearchIdProperties;
            }
        }
        return null;
    }

}
