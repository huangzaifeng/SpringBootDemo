package com.cmcc.cn.service.storm;

import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.TopologyBuilder;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Created by le on 2017/9/27.
 */
public interface StormIServer {
   /*构建Storm Topology图*/
    StormTopology buildStormTopology()throws Exception;

   /*自动查询所有的spout数据源
   * packageName指定扫描包名,valueClass指定的注解类型*/
    <T extends Annotation> List<IRichSpout> findSpout(String packageName, Class<T> valueClass) throws Exception;

    /*自动查询所有的bolt数据源
       * packageName指定扫描包名,valueClass指定的注解类型*/
    <T extends Annotation> List<IRichBolt> findBolt(String packageName, Class<T> valueClass) throws Exception;
}
