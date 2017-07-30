package com.migu.cn.dao.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class RedisModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5938808534741880933L;
	private String redisKey;// redis中的key
	private String name;// 姓名
	private String tel;// 电话
	private String address;// 住址

}
