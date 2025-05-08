package com.weindependent.app.vo.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventVO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime eventTime;
    private String link;
    private Integer categoryId;
    private String categoryName;
    private String location;
    private Integer speakerId;
    private String speakerName;
    private String speakerTitle;
    private String speakerBackground;
    private String speakerDescription;
    private Long speakerBannerId;
    private String speakerBannerUrl;
    private Long bannerId;
    private String bannerUrl;
    private boolean isBookMarked;
}
