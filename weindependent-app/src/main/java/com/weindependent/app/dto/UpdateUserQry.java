package com.weindependent.app.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserQry {
  @NotBlank(message = "Username is required")
  private String realName;
  @NotBlank(message = "Username is required")
  private String language;
  @NotBlank(message = "Username is required")
  private String visaType;
}
