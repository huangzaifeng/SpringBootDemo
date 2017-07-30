package com.migu.cn.service.impl;

import com.migu.cn.service.inf.IBaseLineService;
import com.migu.cn.utils.type.RequestValidataUtil;
import com.migu.cn.webapi.data.BaseLineRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by le on 2017/6/28.
 */
@Service(value = "baseLineServiceImpl")
public class BaseLineServiceImpl implements IBaseLineService {
    @Override
    public void createNewBaseLine(BaseLineRequest request) throws Exception {
        RequestValidataUtil.ValidataBaseLineRequest(request);
        System.out.print(request.toString());
    }
}
