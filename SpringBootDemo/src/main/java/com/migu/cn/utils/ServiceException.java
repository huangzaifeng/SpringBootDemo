package com.migu.cn.utils;

import lombok.Data;

/**
 * Created by le on 2017/6/28.
 */
@Data
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 154141025257886487L;
    private String code;	//错误码
    private String msg;	//错误描述


    public ServiceException(String msg){
        this.code = "411";
        this.msg = msg;
    }



}
