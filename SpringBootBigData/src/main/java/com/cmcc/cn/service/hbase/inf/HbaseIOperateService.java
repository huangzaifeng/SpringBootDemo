package com.cmcc.cn.service.hbase.inf;

import java.util.List;

/**
 * Created by le on 2017/10/12.
 */
public interface HbaseIOperateService {

    /*单条插入*/
    <T> Boolean insertHbase(T valueClass) throws Exception;

    /*批量插入*/
    <T> Boolean insertHbase(List<T> valueClasss) throws Exception;

    /*扫描hbase表*/
    <T> List<T> scanHbase(T valueClass) throws Exception;

    /*通过rowkey获取get数据*/
    <T> T getHbase(T valueClass) throws Exception;

    /*通过rowkey集合获取get数据*/
    <T> List<T> getHbaseList(T valueClass) throws Exception;
}
