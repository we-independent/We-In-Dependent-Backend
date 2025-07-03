package com.weindependent.app.database.mapper.weindependent;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.weindependent.app.database.dataobject.DonateDO;

@Mapper
public interface PaypalIPNMapper {
    int insertIpnTransaction(@Param("userId") Integer userId,
        @Param("payerEmail") String payerEmail,
        @Param("payerFirstName") String payerFirstName,
        @Param("payerLastName") String payerLastName,
        @Param("payerPhone") String payerPhone,
        @Param("txnId") String txnId,
        @Param("paymentStatus") String paymentStatus,
        @Param("paymentDate") java.util.Date paymentDate,
        @Param("mcGross") java.math.BigDecimal mcGross,
        @Param("mcFee") java.math.BigDecimal mcFee,
        @Param("mcCurrency") String mcCurrency,
        @Param("rawIpn") String rawIpn,
        @Param("txnType") String txnType);

    List<DonateDO> getDonateHistory(@Param("userId") int userId);
}
