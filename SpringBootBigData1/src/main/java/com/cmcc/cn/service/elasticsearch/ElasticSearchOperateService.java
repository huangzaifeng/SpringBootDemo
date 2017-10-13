package com.cmcc.cn.service.elasticsearch;

import com.cmcc.cn.bean.ElasticsearchBasePage;

import java.io.IOException;
import java.util.List;

/**
 * Created by le on 2017/9/24.
 */
public interface ElasticSearchOperateService {
    /*获取集群信息*/
    String gainEsClusterInformation();

    /*判断集群是否存在索引*/
     boolean isIndexExists(String index);

    /*判断集群是否存在改类型*/
    boolean isTypeExists(String index,String type);

    /*创建索引*/
     <T> boolean createIndex(T valueClass) throws IllegalAccessException, IOException;

     /*插入数据*/
     <T> void save(T valueClass) throws Exception ;
     <T> void save(List<T> valueClass) throws Exception ;

     /*精确匹配查询*/
     <T extends ElasticsearchBasePage> List<T> search(T valueClass ) throws Exception;

     /*删除*/
     <T> void deleteByCriteria(T valueClass) throws Exception;
}
