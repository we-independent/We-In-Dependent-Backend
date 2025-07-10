package com.weindependent.app.database.mapper.weindependent;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.*;

@Mapper
public interface SearchSuggestMapper {
    @Select("SELECT keyword FROM keywords WHERE keyword LIKE CONCAT(#{query}, '%') LIMIT 10")
    List<String> suggestKeywords(@Param("query") String query);
}
