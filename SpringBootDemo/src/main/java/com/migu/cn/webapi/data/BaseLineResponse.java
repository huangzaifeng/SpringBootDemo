package com.migu.cn.webapi.data;

import com.migu.cn.webapi.BaseResponse;
import lombok.Data;

/**
 * Created by le on 2017/6/28.
 */
@Data
public class BaseLineResponse extends BaseResponse {
    private BaseLineRequest request;
}
