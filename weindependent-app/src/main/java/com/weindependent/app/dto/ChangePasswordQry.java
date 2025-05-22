package com.weindependent.app.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordQry {
    @NotBlank(message = "oldPassword is mandatory")
    private String oldPassword;
    @NotBlank(message = "newPassword is mandatory")
    private String newPassword;
}