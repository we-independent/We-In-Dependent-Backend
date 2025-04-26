package com.weindependent.app.database.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TagArticleRelationDO {
    private Integer id;
    private Integer tagId;
    private Integer articleId;

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
