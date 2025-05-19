package com.weindependent.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weindependent.app.dto.HelpCenterRequestQry;
import com.weindependent.app.vo.HelpCenterRequestVO;
import com.weindependent.app.service.IUserHelpCenterService;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/user/profile/help")
public class UserHelpCenterController {
    
    @Autowired
    private IUserHelpCenterService userHelpCenterService;

    @Operation(summary = "提交 User Profile Help Center 请求")
    @SaCheckLogin
    @PostMapping("/send")
    public void sendHelp(@RequestBody HelpCenterRequestQry qry){
        Long userId = StpUtil.getLoginIdAsLong();
        userHelpCenterService.saveHelpRequest(userId, qry);
    }

    @Operation(summary = "获取User的 Help 历史记录请求")
    @SaCheckLogin
    @GetMapping("/history")
    public List<HelpCenterRequestVO> getHelpRequestHistory(){
        Long userId = StpUtil.getLoginIdAsLong();
        return userHelpCenterService.getHelpRequestHistory(userId);
    }
    
}
