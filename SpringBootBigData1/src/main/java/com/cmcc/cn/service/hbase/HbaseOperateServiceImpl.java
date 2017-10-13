package com.cmcc.cn.service.hbase;

import com.cmcc.cn.annotation.ParseAnnotationService;
import com.cmcc.cn.annotation.hbase.HbaseAnnotation;
import com.cmcc.cn.service.hbase.inf.HbaseIClientService;
import com.cmcc.cn.service.hbase.inf.HbaseIOperateService;
import com.cmcc.cn.utils.JsonTool;
import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by le on 2017/10/12.
 */
@Service(value = "hbaseOperateService")
public class HbaseOperateServiceImpl extends HbaseServiceImpl implements HbaseIOperateService {

    @Resource(name = "hbaseClientService")
    private HbaseIClientService hbaseIClientService;

    @Resource(name = "parseAnnotationService")
    private ParseAnnotationService annotationService;

    @Override
    public <T> Boolean insertHbase(T valueClass) throws Exception {
        System.out.printf("sssss");
        /*判断是否为HbaseAnnotation注解*/
        if(valueClass.getClass().isAnnotationPresent(HbaseAnnotation.class)){
            List<T> valueClasss=new ArrayList<T>();
            valueClasss.add(valueClass);
            insertHbase(valueClasss);
        }else{
            throw new RuntimeException(valueClass.getClass().getName()+"不是HbaseAnnotation注解类");
        }
        return null;
    }

    @Override
    public <T> Boolean insertHbase(List<T> valueClasss) throws Exception {
        if(!CollectionUtils.isEmpty(valueClasss)){
            T valueClass=valueClasss.get(0);
           /*判断是否为HbaseAnnotation注解*/
            if(valueClass.getClass().isAnnotationPresent(HbaseAnnotation.class)){
            /*获取HbaseAnnotation注解属性*/
                HbaseAnnotation hbaseAnnotation=valueClass.getClass().getAnnotation(HbaseAnnotation.class);
                String tableName=hbaseAnnotation.tableName();
                String family=hbaseAnnotation.family();
                String column=hbaseAnnotation.column();
            /*判断hbase表是否存在，不存在则创建*/
                if(!isExistsTable(tableName)){
                    createHbaseTable(valueClass);
                }
                Table hbaseTable=hbaseIClientService.buildTable(tableName);
                List<Put> puts=new ArrayList<Put>();
                for(T targeClass:valueClasss){
                    Map<String,Object> fieldValue= annotationService.gainFieldValue(targeClass);
                    if(fieldValue.containsKey("rowKey")){
                        String rowKey=fieldValue.get("rowKey").toString();
                        String value=JsonTool.dataToJson(valueClass).toString();
                        Put put=new Put(Bytes.toBytes(rowKey));
                        put.addColumn(Bytes.toBytes(family),Bytes.toBytes(column),Bytes.toBytes(value));
                        puts.add(put);
                    }
                }
                hbaseTable.put(puts);
               return true;
            }
        }
        return null;
    }
}
