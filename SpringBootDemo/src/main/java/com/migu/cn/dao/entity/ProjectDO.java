package com.migu.cn.dao.entity;

import lombok.Data;

@Data
public class ProjectDO {
	//项目id
    private Integer pid;
    //项目名称
    private String name;

    private Integer creatorid;

    private Integer departmentid;

    private Integer companyid;

    private String projectdetail;

    private Byte visible;
}