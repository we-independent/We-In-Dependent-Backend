package com.weindependent.app.database.dataobject;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TagEventRelationDO {
    private Long id;
    private Long eventId;
    private Integer tagId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}