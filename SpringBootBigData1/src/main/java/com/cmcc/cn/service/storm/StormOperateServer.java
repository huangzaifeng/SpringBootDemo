package com.cmcc.cn.service.storm;

import org.apache.storm.Config;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;

/**
 * Created by le on 2017/9/27.
 */
public interface StormOperateServer {
    /*自动构建topology*/
    void autoBuildStormTopology(String topologyName,Config conf,String modelType) throws Exception;
}
