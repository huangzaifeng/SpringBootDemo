package com.cmcc.cn.service.hbase;

import com.cmcc.cn.service.hbase.inf.HbaseIClientService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by le on 2017/10/12.
 */
@Service(value = "hbaseClientService")
public class HbaseClientServiceImpl implements HbaseIClientService {

    @Resource(name = "hbaseConfiguration")
    private Configuration hbaseConf;

    private Connection connection;

    private  Admin admin;

    @Override
    public Admin buildHbaseAdmin() {
        if(null==admin){
            try {
                admin= buildHbaseConnection().getAdmin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return admin;

    }

    @Override
    public Connection buildHbaseConnection() {
        if(null==connection){
            try {
                connection=ConnectionFactory.createConnection(hbaseConf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    @Override
    public Table buildTable(String hbaseTableName) throws IOException {
        return buildHbaseConnection().getTable(TableName.valueOf(hbaseTableName));
    }
}
