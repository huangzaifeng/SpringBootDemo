package com.migu.cn.kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
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
//        kafkaTemplate.metrics();
//
//        kafkaTemplate.execute(new KafkaOperations.ProducerCallback<String, String, Object>() {
//            @Override
//            public Object doInKafka(Producer<String, String> producer) {
//                //这里可以编写kafka原生的api操作
//                return null;
//            }
//        });
//
//        //消息发送的监听器，用于回调返回信息
//        kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
//            @Override
//            public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {
//                System.out.println("成功：topic:"+topic+" value:"+value);
//            }
//
//            @Override
//            public void onError(String topic, Integer partition, String key, String value, Exception exception) {
//                System.out.println("失败：topic:"+topic+" value:"+value);
//            }
//
//            @Override
//            public boolean isInterestedInSuccess() {
//                return false;
//            }
//        });
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
