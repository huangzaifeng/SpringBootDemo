package com.cmcc.cn.service;

import com.cmcc.cn.bean.ElasticsearchBasePage;

import java.lang.annotation.Annotation;
import java.rmi.Remote;
import java.util.List;

/**
 * @ClassName: IService  
 * @Description:服务公共接口
 * @author wcr
 * @date 2017年3月30日     
 */  
public interface IService extends Remote {
	
	/**  
	 * 设置分页参数
	 * @Title: setBasePage  
	 * @Description:
	 * @param @param qryCriteria
	 * @return void 
	 * @throws  
	 */  
	public <T extends ElasticsearchBasePage> ElasticsearchBasePage setBasePage(T qryCriteria, int count);

    public List<Class<?>> findTargetClass(String packageName, Class<? extends Annotation> annotationClass);


}
