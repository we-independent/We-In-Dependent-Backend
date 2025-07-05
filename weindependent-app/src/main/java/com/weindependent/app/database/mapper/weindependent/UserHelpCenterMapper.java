package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.HelpCenterRequestDO;
import com.weindependent.app.vo.HelpCenterRequestVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserHelpCenterMapper {
    void insert(HelpCenterRequestDO request);
    List<HelpCenterRequestVO> selectByUserId(@Param("userId") Long userId);
    Integer getMaxReferenceSequenceThisYear(@Param("year") String year);

}
