package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventQry {
    @NotNull
    private String title;
    @NotNull
    private String summary;
    @NotNull
    private LocalDateTime eventTime;
    @NotNull
    private String location;
    @NotNull
    private Integer speakerUserId;
    @NotNull
    private Integer bannerId;
}