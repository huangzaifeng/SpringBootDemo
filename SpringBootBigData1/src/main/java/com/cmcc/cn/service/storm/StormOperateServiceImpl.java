package com.cmcc.cn.service.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.utils.Utils;
import org.springframework.stereotype.Service;

/**
 * Created by le on 2017/9/27.
 */
@Service(value = "stormOperateService")
public class StormOperateServiceImpl extends StormServer implements StormOperateServer {
    @Override
    public void autoBuildStormTopology(String topologyName, Config conf,String modelType) throws Exception{
        if(modelType.equals("local")){
            /*本地调试模式*/
            LocalCluster cluster = new LocalCluster();
            /*自动构建topology图*/
            StormTopology stormTopology=buildStormTopology();
            cluster.submitTopology(topologyName, conf, stormTopology);
            Utils.sleep(100000);
            cluster.killTopology(topologyName);
            cluster.shutdown();
        }else{
            StormSubmitter.submitTopology(topologyName, conf, buildStormTopology());
        }
    }
}
