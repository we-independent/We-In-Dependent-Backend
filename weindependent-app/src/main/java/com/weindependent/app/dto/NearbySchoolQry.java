package com.weindependent.app.dto;

import lombok.Data;

@Data
public class NearbySchoolQry {
    private Double latitude;
    private Double longitude;
    private Double radius;
}
