package com.cmcc.cn.annotation.elasticsearch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by le on 2017/9/14.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSearchId {

   /*索引id*/
   String id() default "0";

   /*id类型*/
   String type() default "uuid";

   /*是否为空*/
   String isNull() default "Y";
}
