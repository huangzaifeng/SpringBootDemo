package com.cmcc.cn.service.hbase;

import com.cmcc.cn.annotation.hbase.HbaseAnnotation;
import com.cmcc.cn.service.PublicService;
import com.cmcc.cn.service.hbase.inf.HbaseIClientService;
import com.cmcc.cn.service.hbase.inf.HbaseIService;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by le on 2017/10/12.
 */
@Component
public abstract class HbaseServiceImpl extends PublicService implements HbaseIService {

    @Resource(name = "hbaseClientService")
    private HbaseIClientService hbaseIClientService;

    @Override
    public Boolean isExistsTable(String tableName) throws IOException {
        // 新建一个数据表表名对象
        TableName table = TableName.valueOf(tableName);
        Admin hbaseAdmin=hbaseIClientService.buildHbaseAdmin();
        if(hbaseAdmin.tableExists(table)){
           return true;
        }else{
            return false;
        }
    }

    @Override
    public <T> void createHbaseTable(T valueClass) throws IOException {
        if(valueClass.getClass().isAnnotationPresent(HbaseAnnotation.class)){
            /*获取HbaseAnnotation注解属性*/
            HbaseAnnotation hbaseAnnotation=valueClass.getClass().getAnnotation(HbaseAnnotation.class);
            String tableName=hbaseAnnotation.tableName();
            String family=hbaseAnnotation.family();
            String column=hbaseAnnotation.column();
            int ttl=hbaseAnnotation.ttl();
            // 数据表描述对象
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            // 列族描述对象
            HColumnDescriptor hbaseFamily= new HColumnDescriptor(family);
            /*设置数据存活时间*/
            if(ttl!=0){
                hbaseFamily.setTimeToLive(ttl);
            }
            hTableDescriptor.addFamily(hbaseFamily);
            Admin hbaseAdmin=hbaseIClientService.buildHbaseAdmin();
            hbaseAdmin.createTable(hTableDescriptor);
        }
    }
}
