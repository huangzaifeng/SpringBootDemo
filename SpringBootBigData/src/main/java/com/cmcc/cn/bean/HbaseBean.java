package com.cmcc.cn.bean;

import lombok.Data;

import java.util.List;

/**
 * Created by le on 2017/10/12.
 */
@Data
public abstract class HbaseBean {
    /*起始键*/
    private String startRowKey;
    /*结束键*/
    private String endRowKey;
    /*rowkey键*/
    private String rowKey;
    /*rowKey集合*/
    private List<String> rowKeys;
}
