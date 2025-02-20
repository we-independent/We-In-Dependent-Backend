package com.weindependent.app.controller;

import com.github.pagehelper.PageHelper;
import com.weindependent.app.dto.NearbySchoolQry;
import com.weindependent.app.service.SchoolService;
import com.weindependent.app.vo.SchoolVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    public List<SchoolVO> getNearbySchools(@RequestBody NearbySchoolQry nearbySchoolQry) {
        PageHelper.startPage(nearbySchoolQry.getPage(), nearbySchoolQry.getLimit());
        return schoolService.getNearbySchools(nearbySchoolQry.getLatitude(), nearbySchoolQry.getLongitude(), nearbySchoolQry.getRadius());
    }
}
