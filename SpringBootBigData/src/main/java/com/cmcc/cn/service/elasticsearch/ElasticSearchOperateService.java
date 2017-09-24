package com.cmcc.cn.service.elasticsearch;

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
     <T> boolean createIndex(T Class) throws IllegalAccessException, IOException;

     /*插入数据*/
     <T> void save(T Class) throws Exception ;
     <T> void save(List<T> Class) throws Exception ;
}
