package com.cmcc.cn.annotation.hbase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by le on 2017/10/12.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface HbaseAnnotation {
    //表名
    String tableName();
    //列族
    String family();
    //列
    String column();
    //TTL时间
    short ttl() default 0;
}
