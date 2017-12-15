package com.cmcc.cn.bean;

import com.cmcc.cn.annotation.elasticsearch.ElasticSearchDocument;
import com.cmcc.cn.annotation.elasticsearch.ElasticSearchId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;

/**
 * Created by le on 2017/9/9.
 */
@Data
@ElasticSearchDocument(indexName = "migu",type = "errorLog1",shards = 2)
public class Article extends ElasticsearchBasePage implements Serializable {
    /*索引id*/
    @ElasticSearchId(isNull = "N")
    private String id;

    /**标题*/
    private String title;

    /**摘要*/
    private String context;

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @CreatedDate
    private String date;
}
