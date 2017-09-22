package com.cmcc.cn.annotation;

import com.cmcc.cn.annotation.elasticsearch.ElasticSearchId;
import org.springframework.data.elasticsearch.annotations.Document;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by le on 2017/9/14.
 */
public class ElasticsearchAnnotation {

    public static <T> void analysisClassAnnotation(T Class){
        //获取类的所有注解
        Annotation[]  annotations=Class.getClass().getAnnotations();
        for(Annotation annotation:annotations){
            if(annotation instanceof Document){
                Document documentAnnotation=(Document) annotation;
                String indexName=documentAnnotation.indexName();
                String type=documentAnnotation.type();
            }
        }
    }

    public static Map<String,Object> analysisResource(Object object) throws IllegalAccessException {
        Map<String,Object> resource=new HashMap<String,Object>();
        Field[] fields=object.getClass().getDeclaredFields();
//        Assert.isNull(object,object.getClass()+"不能为空");
        for(Field field:fields){
            field.setAccessible(true);
            String key=field.getName();
            Object value=field.get(object);
            resource.put(key,value);
        }
        return resource;
    }


    public static <T> T analysisFieldAnnotation(T Class) throws IllegalAccessException {
        Field[] fields=Class.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            if(field.isAnnotationPresent(ElasticSearchId.class)){
                ElasticSearchId elasticSearchId=field.getAnnotation(ElasticSearchId.class);
                String typeId=elasticSearchId.type();
                if(typeId.equals("uuid")){
                    field.set(Class, System.nanoTime());
                }
            }
        }
        return Class;
    }
}
