package com.weindependent.app.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class VerifyPasswordQry {
    @NotBlank(message = "oldPassword is mandatory")
    private String oldPassword;
}
