package com.cmcc.cn.service.storm;

import com.cmcc.cn.annotation.storm.StormBolt;
import com.cmcc.cn.service.PublicService;
import com.cmcc.cn.utils.SpringUtil;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by le on 2017/9/27.
 */
@Component
public abstract class StormServer extends PublicService implements StormIServer{

    private static Logger logger = LoggerFactory.getLogger(StormServer.class);

    @Value("${spring.storm.kafka.parallelism_hint}")
    private String parallelism_hint;

    @Override
    public StormTopology buildStormTopology() throws Exception {
        TopologyBuilder topologyBuilder=new TopologyBuilder();
        /*构建kafka Spout数据源*/
        KafkaSpout kafkaSpoutCustom= SpringUtil.getBeanByName("kafkaSpout", KafkaSpout.class);
        topologyBuilder.setSpout("kafkaSpout",kafkaSpoutCustom,Integer.valueOf(parallelism_hint));
        /*查找所有的bolt处理流*/
        List<IRichBolt> iRichBolts=findBolt("com.cmcc.cn.service.*",StormBolt.class);
        /*构建bolt处理预源*/
        for(IRichBolt iRichBolt:iRichBolts){
            StormBolt stormBolt=iRichBolt.getClass().getAnnotation(StormBolt.class);
            String boltId=stormBolt.id();
            int boltParallelism=stormBolt.parallelism_hint();
            String groupType=stormBolt.groupType();
            if(groupType.equals(com.cmcc.cn.enums.Constants.STORM.groupType)){
                topologyBuilder.setBolt(boltId,iRichBolt,boltParallelism).shuffleGrouping(stormBolt.groupName());
            }else {
                topologyBuilder.setBolt(boltId,iRichBolt,boltParallelism).fieldsGrouping(stormBolt.groupName(),new Fields(stormBolt.fieldsGroupFile()));
            }
        }
        return topologyBuilder.createTopology();
    }

    @Override
    public  <T extends Annotation> List<IRichSpout> findSpout(String packageName, Class<T> valueClass) throws Exception {
        List<IRichSpout> iSpouts=new ArrayList<IRichSpout>();
        List<Class<?>> targetClasss=findTargetClass(packageName, valueClass);
        for(Class<?> targetClass:targetClasss){
            IRichSpout iRichSpout=(IRichSpout) targetClass.newInstance();
            iSpouts.add(iRichSpout);
        }
        return iSpouts;
    }

    @Override
    public <T extends Annotation> List<IRichBolt> findBolt(String packageName, Class<T> valueClass) throws Exception {
        List<IRichBolt> iSpouts = new ArrayList<IRichBolt>();
        List<Class<?>> targetClasss = findTargetClass(packageName, valueClass);
        for (Class<?> targetClass : targetClasss) {
            IRichBolt iRichBolt = (IRichBolt) targetClass.newInstance();
            iSpouts.add(iRichBolt);
        }
        return iSpouts;
    }

}
