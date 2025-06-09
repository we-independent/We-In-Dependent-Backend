package com.weindependent.app.vo.event;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventVO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime eventTime;
    private String link;
    private String videoLink;
    private Integer categoryId;
    private String categoryName;
    private String location;
    // private Integer speakerId;
    // private String speakerName;
    // private String speakerTitle;
    // private String speakerBackground;
    // private String speakerDescription;
    // private Long speakerBannerId;
    // private String speakerBannerUrl;
    private List<EventSpeakerVO> speakers;
    private Long bannerId;
    private String bannerUrl;
    private boolean isBookMarked;
}


