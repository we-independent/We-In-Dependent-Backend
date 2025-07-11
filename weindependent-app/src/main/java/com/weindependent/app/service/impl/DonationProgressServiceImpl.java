package com.weindependent.app.service.impl;

import com.weindependent.app.database.mapper.weindependent.DonationProgressMapper;
import com.weindependent.app.service.IDonationProgressService;
import com.weindependent.app.vo.DonationProgressVO;
import com.weindependent.app.vo.UpdateProgressRequestVO;
import com.weindependent.app.vo.DonationProgressUserVO;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public void updateProgress(UpdateProgressRequestVO request) {
        donationProgressMapper.upsertProgress(request);
    }

    @Override
    public List<DonationProgressUserVO> getProgressByTxnId(String txnId) {
        return donationProgressMapper.getProgressByTxnId(txnId);
    }
}
