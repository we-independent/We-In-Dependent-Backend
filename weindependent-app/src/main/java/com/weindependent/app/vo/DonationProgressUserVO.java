package com.weindependent.app.vo;

import lombok.Data;
import java.util.Date;

@Data
public class DonationProgressUserVO {
    private Integer step;
    private String message;
    private Date stepDate;
}
