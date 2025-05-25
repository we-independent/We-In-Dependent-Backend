package com.weindependent.app.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateUserQry {
  @NotBlank(message = "realName is required")
  private String realName;
  @NotBlank(message = "language is required")
  private String language;
  @NotBlank(message = "VisaType is required")
  private String visaType;
  @NotNull(message = "profileImageId is required")
  private Integer profileImageId;
  @NotNull(message = "isVisaPublic is required")
  private Boolean isVisaPublic;
}
