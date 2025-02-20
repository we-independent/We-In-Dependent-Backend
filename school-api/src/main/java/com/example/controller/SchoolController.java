package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/nearby")
    public List<Map<String, Object>> getNearbySchools(@RequestParam double latitude, @RequestParam double longitude) {
        String sql = """
            SELECT name, address, latitude, longitude,
                   (6371 * ACOS(COALESCE(COS(RADIANS(?)) * COS(RADIANS(latitude)) 
                   * COS(RADIANS(longitude) - RADIANS(?)) + SIN(RADIANS(?)) 
                   * SIN(RADIANS(latitude)), 0))) AS distance
            FROM esl_schools
            WHERE latitude IS NOT NULL AND longitude IS NOT NULL
            ORDER BY distance
            LIMIT 10;
        """;

        return jdbcTemplate.queryForList(sql, latitude, longitude, latitude);
    }
}
