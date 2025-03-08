package com.weindependent.app.service;

import com.weindependent.app.vo.SchoolVO;

import java.util.List;

public interface SchoolService {
    //Custom query to find schools within a given radius (using Haversine formula or a simplified version)
    Integer getNearbySchoolsTotal(Double latitude, Double longitude, Double radius);
    PageInfo<SchoolVO> getNearbySchools(Double latitude, Double longitude, Double radius);
}

