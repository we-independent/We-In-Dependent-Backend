package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class DonateVolunteerQry {
    @NotBlank(message = "fullName is mandatory")
    @Pattern(regexp = "^[A-Za-z\\u4e00-\\u9fa5 ]+$", message = "fullName must contain only letters, Chinese characters and spaces")
    @Size(min = 2, max = 40, message = "fullName must be between 2 and 40 characters")
    private String fullName;

    @NotBlank(message = "email is required")
    @Email(message = "email format is invalid")
    private String email;

    @NotBlank(message = "phoneNumber is required")
    @Pattern(regexp = "^[0-9]+$", message = "phoneNumber must contain only digits")
    private String phoneNumber;

    @NotNull(message = "at least one interest must be selected")
    @Size(min = 1, message = "at least one interest must be selected")
    private List<String> interests;
    private Boolean needsOPT = false;
    private String optStartTime;
    private String motivation;
    private String additionalInfo;

    @NotBlank(message = "please upload your resume")
    private String resumeUrl;

}
