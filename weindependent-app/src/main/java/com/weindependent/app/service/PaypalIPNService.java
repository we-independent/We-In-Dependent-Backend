package com.weindependent.app.service;

import java.util.List;

import com.weindependent.app.database.dataobject.DonateDO;

/**
 * @author Elly
 * @date Jul-03-2025
 */
public interface PaypalIPNService {
    int processIpn(String ipnMessage);
    boolean verifyIpn(String ipnMessage);
    //Map<String, String> parseIpnMessage(String ipnMessage);
    List<DonateDO> donateHistory(int userId);
    DonateDO donateHistoryDetails(int userId, String txnId);

}
