package com.cmcc.cn.service;

import com.cmcc.cn.bean.ElasticsearchBasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**  
 * @ClassName: PublicService  
 * @Description:若需要分页查询-请继承该类
 * @author wcr
 * @date 2017年3月30日     
 */
public  abstract class PublicService implements IService {
	private static Logger logger = LoggerFactory.getLogger(PublicService.class);
	
	@Override
	public <T extends ElasticsearchBasePage> ElasticsearchBasePage setBasePage(T qryCriteria, int count) {
		
		//方法参数检查
		if(null == qryCriteria || !(qryCriteria instanceof ElasticsearchBasePage)){
			logger.error("please check the method args！");
			throw new RuntimeException("please check the method args！");
		}
		
		//设置分页相关的参数
		ElasticsearchBasePage baseForm = (ElasticsearchBasePage)qryCriteria;
		int limit = baseForm.getPageSize();	//每页记录总数
		if(limit > 0 && count > 0){
			int pages = (count%limit == 0) ? (count/limit) : (count/limit + 1);	//总页数
			int rowSrt = 0;	//开始行号
			int rowEnd = 0;	//结束行号
			if(baseForm.getPageNo() == -1 || baseForm.getPageNo()  == pages){//query last page
				rowSrt = (pages - 1)* limit;
				rowEnd = rowSrt + (count%limit == 0 ? limit : (count%limit)) - 1;
			}else{
				rowSrt = (baseForm.getPageNo() - 1)*limit;
				rowEnd = rowSrt + limit - 1;
			}
			baseForm.setRowSrt(rowSrt);
			baseForm.setRowEnd(rowEnd);
			baseForm.setPages(pages);
			baseForm.setCounts(count);
		}else{
			baseForm.setRowSrt(0);
			baseForm.setRowEnd(count-1);
			baseForm.setPages(1);
			baseForm.setCounts(count);
		}
		return baseForm;
	}
}