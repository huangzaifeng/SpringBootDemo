package com.cmcc.cn.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * Created by le on 2017/9/17.
 */
@Configuration
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(null==SpringUtil.applicationContext){
            SpringUtil.applicationContext=applicationContext;
        }
    }

    public static <T> T getBeanByClass(Class<T> object){
        return  applicationContext.getBean(object);
    }

    public static <T> T getBeanByName(String name,Class <T> object){
        return  applicationContext.getBean(name,object);
    }


}
