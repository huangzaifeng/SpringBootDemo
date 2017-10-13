package com.cmcc.cn.service.hbase.inf;

import java.io.IOException;
import java.util.List;

/**
 * Created by le on 2017/10/12.
 */
public interface HbaseIOperateService {
    /*单条插入*/
    <T> Boolean insertHbase(T valueClass) throws Exception;
    /*批量插入*/
    <T> Boolean insertHbase(List<T> valueClasss) throws Exception;
}
