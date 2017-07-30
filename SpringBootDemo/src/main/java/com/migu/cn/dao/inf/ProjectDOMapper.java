package com.migu.cn.dao.inf;

import java.util.List;

import com.migu.cn.dao.entity.ProjectCriteria;
import com.migu.cn.dao.entity.ProjectDO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
@Repository("ProjectDOMapper")
public interface ProjectDOMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(ProjectCriteria record);

    int insertSelective(ProjectCriteria record);
    ProjectDO selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(ProjectCriteria record);

    int updateByPrimaryKey(ProjectCriteria record);
    
    List<ProjectDO> selectByCriteria(ProjectCriteria record);
}