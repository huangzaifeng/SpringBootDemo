package com.migu.cn.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by le on 2017/6/28.
 */
@Service(value = "AsyncTaskService")
public class AsyncTaskService {
    @Async
    public void executeAsyncTask(Integer i){
        System.out.println("执行异步任务i = [" + i + "]");
    }
    @Async
    public void executeAsyncTaskPlus(Integer i){
        System.out.println("执行异步任务++i = [" + i + "]");
    }
}
