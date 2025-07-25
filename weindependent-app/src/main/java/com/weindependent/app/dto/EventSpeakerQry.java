package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventSpeakerQry {

    @NotBlank(message = "speakerFirstName is mandatory")
    private String firstName;

    @NotBlank(message = "speakerLastName is mandatory")
    private String lastName;

    private String title;

    @NotBlank(message = "speakerBackground is mandatory")
    private String background;

    @NotBlank(message = "speakerDescription is mandatory")
    private String description;

    @NotNull(message = "bannerUrl is mandatory")
    private String bannerUrl;
}
