package com.migu.cn.utils.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by le on 2017/6/28.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    // 字段名称
    String name() default "";

    // 是否可为空（N代表否，Y代表是，“”代表根据条件（condition）为空）
    String isNull() default "";
}
