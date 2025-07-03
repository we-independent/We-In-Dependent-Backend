package com.weindependent.app.service;

import java.util.List;

import com.weindependent.app.database.dataobject.DonateDO;

public interface PaypalIPNService {
    int processIpn(String ipnMessage);
    boolean verifyIpn(String ipnMessage);
    //Map<String, String> parseIpnMessage(String ipnMessage);
    List<DonateDO> donateHistory(int userId);

}
