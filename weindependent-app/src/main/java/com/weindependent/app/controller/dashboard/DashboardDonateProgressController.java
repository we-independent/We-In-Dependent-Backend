package com.weindependent.app.controller.dashboard;

import com.weindependent.app.service.IDonationProgressService;
import com.weindependent.app.vo.DonationProgressVO;
import com.weindependent.app.vo.UpdateProgressRequestVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard/donation-progress")
@Tag(name = "Dashboard - Donation Progress")
public class DashboardDonateProgressController {

    @Resource
    private IDonationProgressService donationProgressService;

    @Operation(summary = "List all transactions and their progress")
    @GetMapping("/all")
    public List<DonationProgressVO> getAllProgress() {
        return donationProgressService.getAllWithTransactionInfo();
    }

    @Operation(summary = "Update progress for a transaction")
    @PutMapping("/update")
    public void updateProgress(@RequestBody UpdateProgressRequestVO request) {
        donationProgressService.updateProgress(request);
    }
}
