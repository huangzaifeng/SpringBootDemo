package com.migu.cn.elasticsearch;
import com.alibaba.fastjson.JSONObject;
import com.migu.cn.annotation.ElasticsearchAnnotation;
import com.migu.cn.utils.SpringUtil;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by le on 2017/9/10.
 */
@Service
public class ArticleService {

    @Autowired
    ArticleSearchRepository articleSearchRepository;

    @Autowired
    Client client;

    public void  save(Article article){
        try {
            saveStringId(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Article cityResult= articleSearchRepository.save(article);
//        String queryString="lile";
//        QueryStringQueryBuilder builder=new QueryStringQueryBuilder(queryString);
//        articleSearchRepository.search(builder);
        articleSearchRepository.refresh();
    }

    public Article  getByName(Long id){
        Article cityResult= articleSearchRepository.findOne(id);
        return  cityResult;
    }

    private void saveStringId(Article article) throws Exception {
//        TransportClient transportClient= SpringUtil.getBeanByClass(TransportClient.class);
//        Client client=SpringUtil.getBeanByName("esClient",Client.class);
//        System.out.println("==========="+client.admin().cluster());
//        Client client=ElasticsearchConfig.getClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        String indexName="";
        String type="";
        ElasticsearchAnnotation.analysisFieldAnnotation(article);
        Map<String,Object> resource=ElasticsearchAnnotation.analysisResource(article);
        Annotation[]  annotations=article.getClass().getAnnotations();
        for(Annotation annotation:annotations){
            if(annotation instanceof Document){
                Document documentAnnotation=(Document) annotation;
                 indexName=documentAnnotation.indexName();
                 type=documentAnnotation.type();
            }
        }
        bulkRequest.add(client.prepareIndex(indexName,type).setSource(resource));
        BulkResponse bulkResponse=bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            System.out.println("批量创建索引错误！");
        }
        client.close();
        System.out.println("批量创建索引成功");
    }
}