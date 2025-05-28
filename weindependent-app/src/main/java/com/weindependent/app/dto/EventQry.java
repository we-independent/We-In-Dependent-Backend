package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventQry {
    @NotBlank(message = "title is mandatory")
    private String title;

    @NotBlank(message = "summary is mandatory")
    private String summary;

    @NotBlank(message = "content is mandatory")
    private String content;

    @NotNull(message = "eventTime is mandatory")
    private LocalDateTime eventTime;

    @NotBlank(message = "location is mandatory")
    private String location;

    private String videoLink;

    private String link;

    private Integer categoryId;

    private Long bannerId;
}