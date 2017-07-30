package com.migu.cn.webapi.data;

import com.migu.cn.utils.type.Column;
import lombok.Data;

/**
 * Created by le on 2017/6/28.
 */
@Data
public class BaseLineRequest {
    @Column(name = "基线id")
    private Integer id;
    @Column(isNull = "N",name = "基线版本号")
    private String baseLineVersion;
    @Column(isNull = "Y")
    private String projectId;
}
