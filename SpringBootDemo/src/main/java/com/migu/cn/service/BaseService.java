package com.migu.cn.service;

import com.migu.cn.dao.entity.ProjectDO;
import com.migu.cn.dao.inf.ProjectDOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Created by le on 2017/6/28.
 */
@Service("baseService")
public class BaseService {
    private static Logger logger = LoggerFactory.getLogger(BaseService.class);
    @Autowired
    private ProjectDOMapper projectDao;

    public String say(){
        ProjectDO projectDO= projectDao.selectByPrimaryKey(12);
        logger.info(projectDO.getProjectdetail());
        return projectDO.getName();
    }
}
