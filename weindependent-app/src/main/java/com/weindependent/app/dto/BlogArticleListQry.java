package com.weindependent.app.dto;

import lombok.Data;


import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 获取文章Category的list,Json文件返回
 * 
 * @author Hurely
 *    2025-04-5
 */
@Data
public class BlogArticleListQry {
    // 当前页码，默认第一页
    private Integer pageNum = 1;

    // 每页显示文章数（Figma 设计要求 9 个）
    private Integer pageSize = 9;
    
    // 按分类筛选：传入 category_id 时进行分类查询，否则查询所有文章
    private Integer categoryId;
    
    /**
     * 排序规则，前端传入，比如 "most_saved" 或 "latest"
     * 默认 "latest"
     */
    private String orderBy = "latest";
    
    /**
     * 完整的排序子句，由 Service 层构造。
     * 当 orderBy 为 "most_saved" 时，orderClause 会设置为子查询表达式；
     * 否则设置为 "update_time DESC"。
     */
    @JsonIgnore // ✅ 不允许从前端传入
    private String orderClause;
}
