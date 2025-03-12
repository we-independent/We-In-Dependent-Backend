package com.weindependent.app.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.SchoolDO;
import com.weindependent.app.database.mapper.weindependent.SchoolMapper;
import com.weindependent.app.service.SchoolService;
import com.weindependent.app.vo.SchoolVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolMapper schoolMapper;

    public SchoolServiceImpl(SchoolMapper schoolMapper) {
        this.schoolMapper = schoolMapper;
    }

    @Override
    public Integer getNearbySchoolsTotal(Double latitude, Double longitude, Double radius) {
        return schoolMapper.findNearbySchools(latitude, longitude, radius).size();
    }

    @Override
    public PageInfo<SchoolVO> getNearbySchools(Double latitude, Double longitude, Double radius) {
        //这里将 SchoolDO 转换为 SchoolVO
        List<SchoolVO> schoolVOList= schoolMapper.findNearbySchools(latitude, longitude, radius)
                .stream()
                .map(this::convertToSchoolVO) // 调用转换方法
                .collect(Collectors.toList());
        return new PageInfo<>(schoolVOList);
    }

    // SchoolDO 转换为 SchoolVO
    private SchoolVO convertToSchoolVO(SchoolDO schoolDO) {
        SchoolVO schoolVO = new SchoolVO();
        schoolVO.setName(schoolDO.getName());
        schoolVO.setLatitude(schoolDO.getLatitude());
        schoolVO.setLongitude(schoolDO.getLongitude());
        return schoolVO;
    }
}
