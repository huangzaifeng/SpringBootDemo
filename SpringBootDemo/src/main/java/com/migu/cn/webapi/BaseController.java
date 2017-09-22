package com.migu.cn.webapi;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.migu.cn.elasticsearch.Article;
import com.migu.cn.elasticsearch.ArticleSearchRepository;
import com.migu.cn.elasticsearch.ArticleService;
import com.migu.cn.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.migu.cn.utils.JsonTool;
import com.migu.cn.utils.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by le on 2017/6/28.
 */
public class BaseController {
    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ArticleService articleService;

    public <V> V  ParseMsg(HttpServletRequest req, Class<V> valueClass){
        return ParseMsg(req,valueClass, false);
    }

    private <V> V ParseMsg(HttpServletRequest req, Class<V> valueClass, boolean html) {
        try{
            String body = readReqMsg(req);
            if(body!=null&&!body.isEmpty()){
                if(body.length()<1024){
                    req.getSession().setAttribute("requestbody", body);
                }
                else{
                    req.getSession().setAttribute("requestbody", "body too large");
                }
                V object =  JsonTool.jsonToObject(body, valueClass, false);
                if(object==null){
                    logger.error("请求参数对象为空");
                    throw new ServiceException("亲，@请求参数对象为空！");
                }
                if(!html){
                    Field[] fields = object.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if (field.getType() == String.class) {
                            field.setAccessible(true);
                            String oldValue = (String) field.get(object);
                            if(oldValue!=null){
//                                String newValue = htmlEncode(oldValue);
//                                field.set(object, newValue);
                            }
                        }
                    }
                }
                return object;
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private String readReqMsg(HttpServletRequest request) {
        StringBuffer reqMsg = new StringBuffer();
        BufferedReader reader;
        try {
            reader = request.getReader();
            String str = "";
            while ((str = reader.readLine()) != null) {
                reqMsg.append(str);
            }
            return reqMsg.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String generateResponse(BaseResponse result, int code){
        result.setCode(code);
        result.setRemark("成功");
        return JsonTool.dataToJson(result);
    }

    @ResponseBody
    @ExceptionHandler
    private String handlerException(HttpServletRequest request, HttpServletResponse response, Throwable ex){
        BaseResponse errorResponse  = new BaseResponse();
        errorResponse.setRemark("系统服务器异常");
        kafkaProducer.send(ex.getMessage());
        Article article=new Article();
        article.setTitle("系统服务器异常");
        article.setContent(ex.getMessage());
        article.setCreatedTime(new Date());
        articleService.save(article);
//        throw new ServiceException("系统服务器异常");
        return JsonTool.dataToJson(errorResponse);
    }

}
