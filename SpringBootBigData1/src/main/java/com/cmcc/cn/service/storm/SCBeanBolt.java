package com.cmcc.cn.service.storm;

import com.cmcc.cn.annotation.storm.StormBolt;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by le on 2017/9/28.
 */
@StormBolt(id = "scBeanBolt",name = "解析kafka消息队列",parallelism_hint = 2,groupName = "kafkaSpout")
public class SCBeanBolt extends BaseRichBolt {

    private static Logger logger = LoggerFactory.getLogger(StormServer.class);

    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String message=tuple.getString(0);
        System.out.println("======"+message);
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("flag"));
    }
}
