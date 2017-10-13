package com.cmcc.cn.annotation;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by le on 2017/9/24.
 */
@Service(value = "parseAnnotationService")
public class ParseAnnotationServiceImpl implements ParseAnnotationService {

    @Override
    public Map<String, Object> gainFieldValue(Object object) throws IllegalAccessException {
        Map<String,Object> resource=new HashMap<String,Object>();
        Field[] fields=object.getClass().getDeclaredFields();
//        Assert.isNull(object,object.getClass()+"不能为空");
        for(Field field:fields){
            field.setAccessible(true);
            String key=field.getName();
            if(!ObjectUtils.isEmpty(field.get(object))){
                Object value=field.get(object);
                resource.put(key,value);
            }
        }
        /*获取超类属性*/
        Field[] superFields=object.getClass().getSuperclass().getDeclaredFields();
        for(Field field:superFields){
            field.setAccessible(true);
            String key=field.getName();
            if(!ObjectUtils.isEmpty(field.get(object))){
                Object value=field.get(object);
                resource.put(key,value);
            }
        }
        return resource;
    }

    @Override
    public Map<String, Object> gainFieldProperties(Object object) throws IllegalAccessException {
        Map<String,Object> resource=new HashMap<String,Object>();
        Field[] fields=object.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            String key=field.getName();
            Object value=field.getGenericType();
            resource.put(key,value);
        }
        return resource;
    }

}
