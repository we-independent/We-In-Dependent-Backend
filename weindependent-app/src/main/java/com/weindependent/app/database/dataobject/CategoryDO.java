package com.weindependent.app.database.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分类对象 category
 * 
 * @author christina
 *    2025-03-23
 */
@Data
public class CategoryDO
{
    /** 分类ID */
    private Integer id;

    /** 分类名称 */
    private String name;

    /** 0-未删除, 1-已删除 */
    private Boolean isDeleted;

    /** 创建人 */
    private Integer createUserId;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 最后更新人 */
    private Integer updateUserId;

    /** 最后更新时间 */
    private LocalDateTime  updateTime;

}
