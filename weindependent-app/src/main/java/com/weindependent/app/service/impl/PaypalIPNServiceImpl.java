package com.weindependent.app.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.database.dataobject.DonateDO;
import com.weindependent.app.database.dataobject.DonationProgressDO;
import com.weindependent.app.database.mapper.weindependent.DonationProgressMapper;
import com.weindependent.app.database.mapper.weindependent.PaypalIPNMapper;
import com.weindependent.app.database.mapper.weindependent.UserMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.PaypalIPNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Elly
 * @date: July-03-2025
 */
@Service
public class PaypalIPNServiceImpl implements PaypalIPNService{
    private static final Logger log = LoggerFactory.getLogger(PaypalIPNServiceImpl.class);
    @Autowired 
    private PaypalIPNMapper ipnMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DonationProgressMapper donationProgressMapper;

    public int processIpn(String ipnMessage){
        log.info(ipnMessage);
        if (verifyIpn(ipnMessage)) {
            Map<String, String> ipnParams = parseIpnMessage(ipnMessage);

            Integer userId = userMapper.findByEmail(ipnParams.get("payer_email"));
            if (ipnMapper.insertIpnTransaction(
                    userId,
                    ipnParams.get("payer_email"),
                    ipnParams.get("first_name"),
                    ipnParams.get("last_name"),
                    ipnParams.get("contact_phone"),
                    ipnParams.get("txn_id"),
                    ipnParams.get("payment_status"),
                    parsePaymentDate(ipnParams.get("payment_date")),
                    ipnParams.containsKey("mc_gross") ? new BigDecimal(ipnParams.get("mc_gross")) : BigDecimal.ZERO,
                    ipnParams.containsKey("mc_fee") ? new BigDecimal(ipnParams.get("mc_fee")) : BigDecimal.ZERO,
                    ipnParams.get("mc_currency"),
                    ipnMessage,
                    ipnParams.get("txn_type")) >= 0) {
                // After successful insert, insert default step into donation_progress
                DonationProgressDO progress = new DonationProgressDO();
                progress.setTxnId(ipnParams.get("txn_id"));
                progress.setStep(1); // Step 1: Donation Received
                progress.setMessage("Donation received");
                progress.setStepDate(parsePaymentDate(ipnParams.get("payment_date")));

                donationProgressMapper.insertDonationProgressDO(progress);
                return ErrorCode.SUCCESS.getCode();
            }
            else return ErrorCode.UPDATE_DB_FAILED.getCode();
        } else return ErrorCode.IPN_INVALID.getCode();
    }
    public boolean verifyIpn(String ipnMessage){
        try {
            String payload = "cmd=_notify-validate&" + ipnMessage;
            log.info("payload "+payload);
            URL url = new URL("https://ipnpb.paypal.com/cgi-bin/webscr");
            //sandbox: https://ipnpb.sandbox.paypal.com/cgi-bin/webscr
            //read: https://ipnpb.paypal.com/cgi-bin/webscr

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Java-IPN-Verification-Script");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
            String result = response.toString().trim();
            log.info("ipn response : "+result);
            return "VERIFIED".equalsIgnoreCase(result);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    private Map<String, String> parseIpnMessage(String ipnMessage) {
        return Arrays.stream(ipnMessage.split("&"))
                .map(s -> {
                    String[] kv = s.split("=", 2);
                    try {
                        String key = URLDecoder.decode(kv[0], "UTF-8");
                        String value = kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : "";
                        return new AbstractMap.SimpleEntry<>(key, value);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    

    private java.util.Date parsePaymentDate(String paymentDateStr) {
        if (paymentDateStr == null) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy z", Locale.ENGLISH);
            return sdf.parse(paymentDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DonateDO> donateHistory(int userId){
        return ipnMapper.getDonateHistory(userId);
    }

    
    public DonateDO donateHistoryDetails(int userId, String txnId){
        if(!ipnMapper.checkTxnByUserId(userId, txnId)){
            throw new ResponseException(ErrorCode.UNAUTHORIZED_ACCESS.getCode(), ErrorCode.UNAUTHORIZED_ACCESS.getTitle());
        }
        return ipnMapper.getDonateHistoryDetails(userId, txnId);
    }

}
