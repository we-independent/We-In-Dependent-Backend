package com.weindependent.app.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.database.dataobject.HelpCenterRequestDO;
import com.weindependent.app.database.mapper.weindependent.UserHelpCenterMapper;
import com.weindependent.app.dto.HelpCenterRequestQry;
import com.weindependent.app.enums.MailTypeEnum;
import com.weindependent.app.service.IUserHelpCenterService;
import com.weindependent.app.vo.HelpCenterRequestVO;

import java.time.LocalDateTime;

@Service
public class UserHelpCenterServiceImpl implements IUserHelpCenterService{
    
    @Autowired
    private UserHelpCenterMapper userHelpCenterMapper;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Override
    public void saveHelpRequest(Long userId, HelpCenterRequestQry qry){
        HelpCenterRequestDO request = new HelpCenterRequestDO();
        request.setUserId(userId);
        request.setName(qry.getName());
        request.setEmail(qry.getEmail());
        request.setSubject(qry.getSubject());
        request.setMessage(qry.getMessage());
        request.setCreateTime(LocalDateTime.now());

        userHelpCenterMapper.insert(request);

        // 通知用户收到 HelpCenter 发来的请求
        Map<String, String> emailParams = new HashMap<>();
        emailParams.put("name", qry.getName());
        emailParams.put("message", qry.getMessage());

        emailServiceImpl.send(qry.getEmail(), MailTypeEnum.HELP_CENTER, emailParams);

        // 通知客服 info@weindependent.org
        Map<String, String> adminMailParams = new HashMap<>();
        adminMailParams.put("name", qry.getName());
        adminMailParams.put("email", qry.getEmail());
        adminMailParams.put("subject", qry.getSubject());
        adminMailParams.put("message", qry.getMessage());
        adminMailParams.put("replyTo", qry.getEmail());

        emailServiceImpl.send("info@weindependent.org", MailTypeEnum.HELP_CENTER_NOTIFY, adminMailParams);
        
    }

    @Override
    public List<HelpCenterRequestVO> getHelpRequestHistory(Long userId){
        return userHelpCenterMapper.selectByUserId(userId);
    }

    
}
