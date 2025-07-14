package com.weindependent.app.service.impl;

import com.weindependent.app.database.dataobject.DonationProgressDO;
import com.weindependent.app.database.mapper.weindependent.DonationProgressMapper;
import com.weindependent.app.service.IDonationProgressService;
import com.weindependent.app.vo.DonationProgressVO;
import com.weindependent.app.vo.UpdateProgressRequestVO;
import com.weindependent.app.vo.DonationProgressUserVO;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

@Service
public class DonationProgressServiceImpl implements IDonationProgressService {

    @Resource
    private DonationProgressMapper donationProgressMapper;

    @Override
    public List<DonationProgressVO> getAllWithTransactionInfo() {
        return donationProgressMapper.getAllWithTransactionInfo();
    }

    @Override
    public void updateProgress(String txnId, Integer step) {
        String message;
        switch (step) {
            case 1:
                message = "Donation received";
                break;
            case 2:
                message = "Processing donation";
                break;
            case 3:
                message = "Impact delivered";
                break;
            default:
                throw new IllegalArgumentException("Invalid step: " + step);
        }

        DonationProgressDO progress = new DonationProgressDO();
        progress.setTxnId(txnId);
        progress.setStep(step);
        progress.setMessage(message);
        progress.setStepDate(new Date()); // current timestamp

        donationProgressMapper.insertDonationProgressDO(progress);
    }

    @Override
    public List<DonationProgressUserVO> getProgressByTxnId(String txnId) {
        return donationProgressMapper.getProgressByTxnId(txnId);
    }
}
