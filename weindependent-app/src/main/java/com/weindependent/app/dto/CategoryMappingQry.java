package com.weindependent.app.dto;
import lombok.Data;

@Data
public class CategoryMappingQry {
    private String code;
    private String name;

    public CategoryMappingQry(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
