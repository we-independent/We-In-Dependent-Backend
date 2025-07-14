package com.weindependent.app.database.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class DonationProgressDO {
    private String txnId;
    private Integer step;
    private String message;
    private Date stepDate;
}
