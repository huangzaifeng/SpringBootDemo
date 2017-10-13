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
public @interface StormSpout {

    /*spout名称*/
   String id();

   /*spout描述*/
   String name() default "";
   /*并发度*/
   int parallelism_hint() default 1;
}
