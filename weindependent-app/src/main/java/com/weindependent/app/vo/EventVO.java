package com.weindependent.app.vo;

import com.weindependent.app.database.dataobject.UserDO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventVO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime eventTime;
    private String location;
    private UserVO speaker;
    private String bannerUrl;
}
