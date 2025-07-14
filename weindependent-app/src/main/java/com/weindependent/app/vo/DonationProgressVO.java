package com.weindependent.app.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DonationProgressVO {
    private int userId;
    private String txnId;
    private String payerEmail;
    private String payerFirstName;
    private String payerLastName;
    private String payerPhone;
    private String paymentStatus;
    private Date paymentDate;
    private Double mcGross;
    private Double mcFee;
    private String txnType;
    private String mcCurrency;

    private Integer step;
    private String message;
    private Date stepDate;
}
