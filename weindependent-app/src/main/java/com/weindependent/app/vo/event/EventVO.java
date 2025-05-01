package com.weindependent.app.vo.event;

import com.weindependent.app.vo.UserVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventVO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime eventTime;
    private String location;
    private Integer speakerId;
    private String speakerName;
    private Long bannerId;
    private String bannerUrl;
}
