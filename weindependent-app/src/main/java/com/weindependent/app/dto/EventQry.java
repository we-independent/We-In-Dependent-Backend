package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class EventQry {
    @NotBlank(message = "title is mandatory")
    private String title;

    @NotBlank(message = "summary is mandatory")
    private String summary;

    @NotBlank(message = "content is mandatory")
    private String content;

    @NotNull(message = "eventTime is mandatory(UTC)")
    private Instant eventTime;

    @NotNull(message = "duration is mandatory(minutes)")
    private Integer duration;

    @NotBlank(message = "location is mandatory")
    private String location;

    private String videoLink;

    private String link;

    private Integer categoryId;

    private Long bannerId;
}