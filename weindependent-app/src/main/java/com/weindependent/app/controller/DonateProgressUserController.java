package com.weindependent.app.controller;

import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.service.IDonationProgressService;
import com.weindependent.app.vo.DonationProgressUserVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/donate-progress")
@Tag(name = "Donation Progress")
public class DonateProgressUserController {

    @Resource
    private IDonationProgressService donationProgressService;

    @Operation(summary = "Get donation progress for specific transaction")
    @SignatureAuth
    @GetMapping("/{txnId}")
    public List<DonationProgressUserVO> getProgress(@PathVariable String txnId) {
        return donationProgressService.getProgressByTxnId(txnId);
    }
}
