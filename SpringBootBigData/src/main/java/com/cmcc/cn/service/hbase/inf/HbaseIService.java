package com.cmcc.cn.service.hbase.inf;

import java.io.IOException;

/**
 * Created by le on 2017/10/12.
 */
public interface HbaseIService {

    //检验表是否存在
     Boolean isExistsTable(String tableName) throws IOException;

    //通过类来创建hbase表
    <T> void  createHbaseTable(T valueClass) throws IOException;
}
