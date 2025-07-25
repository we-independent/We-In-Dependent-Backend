package com.weindependent.app.vo.event;

import lombok.Data;

@Data
public class EventSpeakerVO {
    private Long speakerId;
    private String firstName;
    private String lastName;
    private String title;
    private String background;
    private String description;
    private String bannerUrl;       // From event_speaker_image.file_path
}
