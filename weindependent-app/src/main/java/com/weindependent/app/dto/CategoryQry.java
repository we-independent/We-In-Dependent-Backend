package com.weindependent.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CategoryQry {

    /**
     * 分类ID
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 0-未删除, 1-已删除
     */
    private Boolean isDeleted;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新人
     */
    private Integer updateUserId;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    private Integer pageNum;
    private Integer pageSize;
}
