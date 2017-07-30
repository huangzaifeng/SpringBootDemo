package com.migu.cn.service.impl;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by le on 2017/6/28.
 */
@EnableScheduling
@Service(value = "ScheduledTaskService")
public class ScheduledTaskService {
//    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime(){
        System.out.println("执行调度");
    }
}
