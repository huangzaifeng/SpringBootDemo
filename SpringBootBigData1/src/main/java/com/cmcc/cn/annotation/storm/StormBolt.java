package com.cmcc.cn.annotation.storm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by le on 2017/9/27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface StormBolt {
    /*spout名称*/
    String id();
    /*spout描述*/
    String name();
    /*并发度*/
    int parallelism_hint() default 1;
    /*分组类型*/
    String groupType() default "shuffle";
    /*随机分组名*/
    String groupName() default "";
    /*类型分组名*/
    String fieldsGroupFile() default "";
}
