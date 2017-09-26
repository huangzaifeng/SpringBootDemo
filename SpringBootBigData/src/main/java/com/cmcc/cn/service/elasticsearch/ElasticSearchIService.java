package com.cmcc.cn.service.elasticsearch;

import com.cmcc.cn.service.IService;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;

/**
 * Created by le on 2017/9/26.
 */
public interface ElasticSearchIService extends IService {
    /*获取批量执行工具*/
    BulkProcessor bulkProcess(Client client);

}
