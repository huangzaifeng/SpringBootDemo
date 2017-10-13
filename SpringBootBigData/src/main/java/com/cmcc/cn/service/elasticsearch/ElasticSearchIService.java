package com.cmcc.cn.service.elasticsearch;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;

import java.rmi.Remote;

/**
 * Created by le on 2017/9/26.
 */
public interface ElasticSearchIService extends Remote {
    /*获取批量执行工具*/
    BulkProcessor bulkProcess(Client client);

}
