package com.weindependent.app.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.dto.NearbySchoolQry;
import com.weindependent.app.service.SchoolService;
import com.weindependent.app.vo.SchoolVO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping(value ="/schools", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
public class SchoolController {

    @Resource
    private SchoolService schoolService;

    @PostMapping("/nearby/total")
    public Integer getNearbySchoolsTotal(@RequestBody NearbySchoolQry nearbySchoolQry) {
        return schoolService.getNearbySchoolsTotal(nearbySchoolQry.getLatitude(), nearbySchoolQry.getLongitude(), nearbySchoolQry.getRadius());
    }

    @PostMapping("/nearby")
    public PageInfo<SchoolVO> getNearbySchools(@RequestBody NearbySchoolQry nearbySchoolQry) {
        PageHelper.startPage(nearbySchoolQry.getPage(), nearbySchoolQry.getLimit());
        return schoolService.getNearbySchools(nearbySchoolQry.getLatitude(), nearbySchoolQry.getLongitude(), nearbySchoolQry.getRadius());
    }
}
