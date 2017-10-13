package com.cmcc.cn.service.hbase.inf;

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

/**
 * Created by le on 2017/10/12.
 */
public interface HbaseIClientService {

    /*创建hbase Admin*/
    Admin buildHbaseAdmin();

    /*创建hbase连接*/
    Connection buildHbaseConnection();

    Table buildTable(String hbaseTableName) throws IOException;
}
