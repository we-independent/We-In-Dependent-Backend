package com.weindependent.app.database.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class DonateDO {
    private Integer userId;
    private String payerEmail;
    private String payerFirstName;
    private String payerLastName;
    private String payerPhone;
    private String txnId;
    private String paymentStatus;
    private Date paymentDate;
    private BigDecimal mcGross;
    private BigDecimal mcFee;
    private String mcCurrency;
    private String rawIpn;
    private String txnType;

}
