package com.weindependent.app.database.dataobject;

import lombok.Data;

@Data
public class SchoolDO {
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
}
