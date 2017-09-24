package com.cmcc.cn.bean;

import com.cmcc.cn.annotation.elasticsearch.ElasticSearchDocument;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by le on 2017/9/9.
 */
@Data
@ElasticSearchDocument(indexName = "migu",type = "errorLog",shards = 2)
public class Article implements Serializable {
    /**标题*/
    private String title;
    /**摘要*/
    private String context;

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @CreatedDate
    private Date date;


}
