package com.weindependent.app.controller;

import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.DonateDO;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.PaypalIPNService;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "捐款管理")
@RestController
@RequestMapping("/donate")
public class DonateController {
    @Autowired
    private PaypalIPNService paypalIPNService;

    
    @Operation(summary = "接收paypal IPN")
    @PostMapping("/receive-paypal-ipn")
    public ResponseEntity<String> handleIPN(HttpServletRequest request) throws IOException {
        String ipnMessage = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        // int resultCode = paypalIPNService.processIpn(ipnMessage);
        // if (resultCode == ErrorCode.SUCCESS.getCode()) return;
        // else if (resultCode == ErrorCode.UPDATE_DB_FAILED.getCode()){
        //     throw new ResponseException(resultCode, ErrorCode.UPDATE_DB_FAILED.getTitle());
        // }
        // else if (resultCode == ErrorCode.IPN_INVALID.getCode()){
        //     throw new ResponseException(resultCode, ErrorCode.IPN_INVALID.getTitle());
        // }
        boolean isValid = paypalIPNService.verifyIpn(ipnMessage);

        System.out.println("Received IPN: " + ipnMessage);
        System.out.println("Verification result: " + (isValid ? "VERIFIED" : "INVALID"));

        // 在此先不做任何 insert，只返回 HTTP 200
        return ResponseEntity.ok("OK");
    }

    //@SignatureAuth
    @Operation(summary = "根据user id提取donate history")
    @GetMapping("/donate-history")
    public List<DonateDO> donateHistory(){
        int userId = StpUtil.getLoginIdAsInt();
        return paypalIPNService.donateHistory(userId);
    }
}
