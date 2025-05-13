package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventSpeakerQry {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "speakerTitle is mandatory")
    private String title;

    @NotBlank(message = "speakerBackground is mandatory")
    private String background;

    @NotBlank(message = "speakerDescription is mandatory")
    private String description;

    @NotNull(message = "bannerId is mandatory")
    private Long bannerId;
}
