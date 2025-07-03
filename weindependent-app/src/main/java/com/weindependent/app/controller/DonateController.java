package com.weindependent.app.controller;

import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.DonateDO;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.PaypalIPNService;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "捐款管理")
@RestController
@RequestMapping("/api/donate")
public class DonateController {
    @Autowired
    private PaypalIPNService paypalIPNService;

    /**
     * Lisen from paypal instant payment notification(IPN) and save to db
     * @param request message from paypal IPN
     * @throws IOException
     * @author Elly
     * @date Jul-03-2025
     */
    @Operation(summary = "接收paypal IPN")
    @PostMapping("/receive-paypal-ipn")
    public void handleIPN(HttpServletRequest request) throws IOException {
        //receive response msg
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
           sb.append(line);
        }
        String ipnMessage = sb.toString();
        //verify and save to db
        int resultCode = paypalIPNService.processIpn(ipnMessage);
        if (resultCode == ErrorCode.SUCCESS.getCode()) return;
        else if (resultCode == ErrorCode.UPDATE_DB_FAILED.getCode()){
            throw new ResponseException(resultCode, ErrorCode.UPDATE_DB_FAILED.getTitle());
        }
        else if (resultCode == ErrorCode.IPN_INVALID.getCode()){
            throw new ResponseException(resultCode, ErrorCode.IPN_INVALID.getTitle());
        }
        // boolean isValid = paypalIPNService.verifyIpn(ipnMessage);

        // System.out.println("Received IPN: " + ipnMessage);
        // System.out.println("Verification result: " + (isValid ? "VERIFIED" : "INVALID"));

        // 在此先不做任何 insert，只返回 HTTP 200
        //return ResponseEntity.ok("OK");
    }

    /**
     * get user's all donate history, please pass user token from header
     * @return a list of donate data object selected by user id
     * @author Elly
     * @date Jul-03-2025
     */
    @SignatureAuth
    @Operation(summary = "根据user id提取donate history")
    @GetMapping("/donate-history")
    public List<DonateDO> donateHistory(){
        int userId = StpUtil.getLoginIdAsInt();
        return paypalIPNService.donateHistory(userId);
    }

    /**
     * get transaction details by user id and transaction id, please pass user token from header
     * @param txnId transaction id, path variable
     * @return donate data object selected by user id and transaction id
     * 
     * @auther Elly
     * @date Jul-03-2025
     */
    @SignatureAuth
    @Operation(summary  = "get transaction details by user id and transaction id")
    @GetMapping("/donate-history-details/{txnId}")
    public DonateDO donateHistoryDetails(@PathVariable String txnId){
        int userId = StpUtil.getLoginIdAsInt();
        return paypalIPNService.donateHistoryDetails(userId, txnId);
    }
}
