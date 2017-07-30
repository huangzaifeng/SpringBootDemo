package com.migu.cn.webapi.data;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by le on 2017/6/28.
 */
@Data
@ConfigurationProperties(prefix = "hello")
public class FunctionRequirement {
    private String name;

}
