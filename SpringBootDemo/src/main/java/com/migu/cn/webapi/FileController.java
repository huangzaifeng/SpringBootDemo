package com.migu.cn.webapi;

import com.migu.cn.elasticsearch.Article;
import com.migu.cn.elasticsearch.ArticleService;
import com.migu.cn.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by le on 2017/7/2.
 */
@RestController()
@RequestMapping("/file")
public class FileController extends BaseController{
    @Resource(name = "baseService")
    private BaseService baseService;

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/hello")
    public String file(){
        Article article=new Article();
        article.setTitle("error567");
        article.setContent("this is error log67");
        article.setAbstracts("spms_permit_service67");
        articleService.save(article);
        return baseService.say();
    }
}
