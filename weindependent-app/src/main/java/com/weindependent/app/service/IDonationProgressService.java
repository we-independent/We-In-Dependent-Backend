package com.weindependent.app.service;

import com.weindependent.app.vo.DonationProgressVO;
import com.weindependent.app.vo.UpdateProgressRequestVO;
import com.weindependent.app.vo.DonationProgressUserVO;

import java.util.List;

public interface IDonationProgressService {
    List<DonationProgressVO> getAllWithTransactionInfo();
    void updateProgress(String txnId, Integer step);
    List<DonationProgressUserVO> getProgressByTxnId(String txnId);
}
