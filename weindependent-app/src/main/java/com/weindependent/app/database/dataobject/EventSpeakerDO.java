package com.weindependent.app.database.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSpeakerDO {
    private Long id;
    // private Long userId;
    private String firstName;
    private String lastName;
    private String title; // Optional
    private String background;
    private String description;
    private Long bannerId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}