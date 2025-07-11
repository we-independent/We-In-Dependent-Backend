package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.vo.DonationProgressVO;
import com.weindependent.app.vo.DonationProgressUserVO;
import com.weindependent.app.vo.UpdateProgressRequestVO;
import com.weindependent.app.database.dataobject.DonationProgressDO;

import java.util.List;

public interface DonationProgressMapper {
    List<DonationProgressVO> getAllWithTransactionInfo();
    List<DonationProgressUserVO> getProgressByTxnId(String txnId);
    void upsertProgress(UpdateProgressRequestVO request);
    void upsertProgress(DonationProgressDO request);
}
