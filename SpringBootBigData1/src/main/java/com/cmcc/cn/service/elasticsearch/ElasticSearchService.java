package com.cmcc.cn.service.elasticsearch;

import com.cmcc.cn.service.PublicService;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by le on 2017/9/26.
 */
public abstract class ElasticSearchService extends PublicService implements ElasticSearchIService {
    private static Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);

    @Override
    public BulkProcessor bulkProcess(Client client) {
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

        return bulkProcessor;

    }
}
