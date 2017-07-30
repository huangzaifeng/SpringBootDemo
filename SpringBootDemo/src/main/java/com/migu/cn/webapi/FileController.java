package com.migu.cn.webapi;

import com.migu.cn.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by le on 2017/7/2.
 */
@RestController()
@RequestMapping("/file")
public class FileController {
    @Resource(name = "baseService")
    private BaseService baseService;
    @RequestMapping(value = "/hello")
    public String file(){
        return baseService.say();
    }
}
