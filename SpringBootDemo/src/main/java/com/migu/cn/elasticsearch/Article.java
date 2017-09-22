package com.migu.cn.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.migu.cn.annotation.ElasticSearchId;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by le on 2017/9/9.
 */
@Data
@Document(indexName = "migu",type = "errorLog",shards = 2)
public class Article implements Serializable {
    @ElasticSearchId()
    private Long id;
    /**标题*/
    @Column(name = "标题")
    private String title;
    /**摘要*/
    private String abstracts;
    /**内容*/
    private String content;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @CreatedDate
    private Date createdTime;


}
