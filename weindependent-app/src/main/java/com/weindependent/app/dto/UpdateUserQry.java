package com.weindependent.app.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserQry {
  @NotBlank(message = "realName is required")
  private String realName;
  @NotBlank(message = "language is required")
  private String language;
  @NotBlank(message = "VisaType is required")
  private String visaType;
  @NotBlank(message = "imgId is required")
  private long imgId;
}
