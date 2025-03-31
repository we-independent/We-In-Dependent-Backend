package com.weindependent.app.database.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签对象 tag
 * 
 * @author chistina
 *    2025-03-23
 */
@Data
public class TagDO
{
    /** 标签ID **/
    private Integer id;

    /** tag名称 */
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
