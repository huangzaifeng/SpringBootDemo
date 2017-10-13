package com.cmcc.cn.bean;

import com.cmcc.cn.annotation.hbase.HbaseAnnotation;
import lombok.Data;

/**
 * Created by le on 2017/10/12.
 */
@Data
@HbaseAnnotation(tableName = "student",family = "f",column = "info")
public class Student extends HbaseBean {
    private String name;
    private int age;
}
