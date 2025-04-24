package com.weindependent.app.database.dataobject;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDO {
    private Long id;
    private String title;
    private String summary;
    private LocalDateTime eventTime;
    private String location;
    private Integer speakerUserId;
    private Integer bannerId;
    private boolean isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer createUserId;
    private Integer updateUserId;
}
