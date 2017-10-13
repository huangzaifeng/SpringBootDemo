package com.cmcc.cn.service.storm;

import org.apache.storm.Config;

/**
 * Created by le on 2017/9/27.
 */
public interface StormOperateServer {
    /*自动构建topology*/
    void autoBuildStormTopology(String topologyName, Config conf, String modelType) throws Exception;
}