package com.migu.cn.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by le on 2017/7/4.
 */
@Configuration
@ConfigurationProperties(prefix = "mysql")
@PropertySource(value = {"file:D:/spmspath/mysql.properties"})
public class DBSource {
    @Autowired
    private Environment env;

    @Bean(name = "basicDataSource")
    public BasicDataSource getDataSource() throws Exception {
        BasicDataSource basicDataSource= new BasicDataSource();
        basicDataSource.setDriverClassName(env.getProperty("mysql.driver"));
        basicDataSource.setUrl(env.getProperty("mysql.url"));
        basicDataSource.setUsername(env.getProperty("mysql.username"));
        basicDataSource.setPassword(env.getProperty("mysql.password"));
        System.out.println(env.getProperty("mysql.url"));
        return basicDataSource;
    }
}
