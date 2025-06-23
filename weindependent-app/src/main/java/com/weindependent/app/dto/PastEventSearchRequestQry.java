package com.weindependent.app.dto;
import lombok.Data;

@Data
public class PastEventSearchRequestQry {
    private String keyword;
    private String mode;
    private EventFilterQry filter;
    private int page = 1;
    private int size = 10;
}
