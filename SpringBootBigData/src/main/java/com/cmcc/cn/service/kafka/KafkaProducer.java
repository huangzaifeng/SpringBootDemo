package com.cmcc.cn.service.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 * Created by le on 2017/9/8.
 */
@Component
public class KafkaProducer implements ProducerListener {
    @Value("${spring.kafka.template.default-topic}")
    private String topic;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void send(String key,String message) {
        kafkaTemplate.send(topic,key,message);
    }
    public void send(String message) {
        kafkaTemplate.send(topic,message);
    }

    @Override
    public void onSuccess(String s, Integer integer, Object o, Object o2, RecordMetadata recordMetadata) {
        System.out.println("==========kafka发送数据正确（日志开始）==========");
    }

    @Override
    public void onError(String s, Integer integer, Object o, Object o2, Exception e) {
        System.out.println("==========kafka发送数据错误（日志开始）==========");
    }

    @Override
    public boolean isInterestedInSuccess() {
        return false;
    }
}
