package com.cmcc.cn.annotation;

import java.util.Map;

/**
 * Created by le on 2017/9/24.
 */
public interface ParseAnnotationService {

   /*
   * 获取类属性key->value值方法
   * */
    Map<String,Object> gainFieldValue(Object object) throws IllegalAccessException;

    /*获取类中对象属性*/
    Map<String,Object> gainFieldProperties(Object object) throws IllegalAccessException;
}
