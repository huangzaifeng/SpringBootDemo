package com.migu.cn.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by le on 2017/9/9.
 */
public interface ArticleSearchRepository extends ElasticsearchRepository<Article,Long> {
}
