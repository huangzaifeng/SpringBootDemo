package com.migu.cn.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Created by le on 2017/9/8.
 */
public class ConsumerListener {
    @Value("${spring.kafka.template.default-topic}")
    private String topic;

    @KafkaListener(topics = "spms")
    public void listen(ConsumerRecord<?, ?> record) {
        System.out.println(("kafka的key: " + record.key()));
        System.out.println("kafka的value: " + record.value().toString());
    }
}
