package com.weindependent.app.vo.event;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class EventVO {
    private Long id;
    private String title;
    private String content;
    private Instant eventTime;
    private Integer duration;
    private String link;
    private String videoLink;
    private Integer tagId;
    private String tagName;
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


