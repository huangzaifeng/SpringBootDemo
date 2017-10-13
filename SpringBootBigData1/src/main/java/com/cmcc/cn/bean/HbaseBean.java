package com.cmcc.cn.bean;

import com.cmcc.cn.annotation.hbase.HbaseAnnotation;
import lombok.Data;

/**
 * Created by le on 2017/10/12.
 */
@Data
public abstract class HbaseBean {
    private String startRowKey;

    private String endRowKey;

    private String rowKey;
}
