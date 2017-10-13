package com.cmcc.cn.annotation.elasticsearch;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by le on 2017/9/14.
 */
public class ElasticsearchAnnotation {

    public static <T> void analysisClassAnnotation(T Class){
        //获取类的所有注解
        Annotation[]  annotations=Class.getClass().getAnnotations();
        for(Annotation annotation:annotations){
            if(annotation instanceof ElasticSearchDocument){
                ElasticSearchDocument documentAnnotation=(ElasticSearchDocument) annotation;
                String indexName=documentAnnotation.indexName();
                String type=documentAnnotation.type();
            }
        }
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
