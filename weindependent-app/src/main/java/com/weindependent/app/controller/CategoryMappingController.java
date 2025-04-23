package com.weindependent.app.controller;

import com.weindependent.app.dto.CategoryMappingQry;
import com.weindependent.app.enums.CategoryEnum;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/category")
public class CategoryMappingController {

    @GetMapping("/mapping")
    public Object getCategoryMappingInfo(@RequestParam(required = false) String code,
                                         @RequestParam(required = false) String name) {
            if(code != null){
                return CategoryEnum.getNameByCode(code);
            }
        
            if(name != null){
                return CategoryEnum.getCodeByName(name);
            }
            
            return Arrays.stream(CategoryEnum.values())
                .map(c -> new CategoryMappingQry(c.getCode(), c.getName()))
                .collect(Collectors.toList());
    }

}
