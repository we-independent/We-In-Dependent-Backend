package com.weindependent.app.controller;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.dto.LoginQry;
import com.weindependent.app.dto.RegisterQry;
import com.weindependent.app.utils.PasswordUtil;
import com.weindependent.app.dto.SendMailQry;
import com.weindependent.app.dto.ResetPasswordQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.EmailService;
import com.weindependent.app.service.SendEmailService;
import com.weindependent.app.service.UserService;
import com.weindependent.app.vo.LoginVO;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import com.weindependent.app.vo.UserVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Api(tags = "用户管理")
@Tag(name = "用户管理")
@Slf4j
@RestController
@RequestMapping(value = "/user", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private SendEmailService sendEmailService;


    @SignatureAuth
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    @CrossOrigin(origins = "*")
    public void register(@Validated @RequestBody RegisterQry registerQry) {
        userService.registerUser(registerQry);
        log.info("User registered successfully: {}", registerQry.getAccount());
    }

    @SignatureAuth
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public LoginVO login(@Validated @RequestBody LoginQry loginQry) throws Exception {
        UserDO user = userService.queryByEmailAndPassword(loginQry.getEmail(), loginQry.getPassword());

        if (ObjectUtils.isEmpty(user) || !PasswordUtil.verifyPassword(loginQry.getPassword(), user.getPassword())) {
            log.error("Login failed for user: {}", loginQry.getEmail());
            throw new ResponseException(401, "Invalid username or password");
        }

        SaLoginModel loginModel = new SaLoginModel();
        loginModel.setExtra("username", user.getRealName());
        StpUtil.login(user.getId(), loginModel);

        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        LoginVO loginVO = new LoginVO();
        loginVO.setSaTokenInfo(saTokenInfo);
        loginVO.setEmail(user.getAccount());
        loginVO.setUsername(user.getRealName());
        loginVO.setLanguage(user.getLanguage());
        loginVO.setVisaType(user.getVisaType());
        loginVO.setSubscription(user.isSubscription());

        log.info("Login successful for user: {}", loginQry.getEmail());
        return loginVO;
    }
    @SignatureAuth
    @Operation(summary = "重置密码")
    @PostMapping("/user/reset/password")
    @CrossOrigin(origins = "*")
    public void resetPassword(@Validated @RequestBody ResetPasswordQry resetPasswordQry){
        boolean success = userService.resetPassword(resetPasswordQry.getEmail(), resetPasswordQry.getPassword());
        if (success) {
            log.info("Reset password successful for user: {}", resetPasswordQry.getEmail());
        } else {
            log.info("Reset password failed");
        }
    }

//    @Operation(summary = "登录")
//    @SignatureAuth
//    @PostMapping("/login")
//    @CrossOrigin(origins = "*")
//    public LoginVO login(@Validated @RequestBody LoginQry loginQry) throws Exception {
//        UserDO user = userService.queryByEmailAndPassword(loginQry.getEmail(), loginQry.getPassword());
//        if (ObjectUtils.isEmpty(user)) {
//            log.error("Login failed for user: {}", loginQry.getEmail());
//            throw new ResponseException(ErrorCode.USERNAME_PASSWORD_ERROR.getCode(), ErrorCode.USERNAME_PASSWORD_ERROR.getTitle());
//        }
//
//        // Token挂载的扩展参数 （此方法只有在集成jwt插件时才会生效）
//        SaLoginModel loginModel = new SaLoginModel();
//        loginModel.setExtra("username", user.getAccount());
//        StpUtil.login(user.getId(), loginModel);
//
//        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
//        LoginVO loginVO = new LoginVO();
//        loginVO.setSaTokenInfo(saTokenInfo);
//        loginVO.setEmail(loginQry.getEmail());
//        loginVO.setUsername(user.getAccount());
//
//        log.info("Login successful for user: {}", loginQry.getEmail());
//        return loginVO;
//    }

    @SignatureAuth
    @Operation(summary = "登出")
    @GetMapping("/logout")
    @CrossOrigin(origins = "*")
    public Map<String, Object> logout() {
        StpUtil.logout();

        Map<String, Object> result = new HashMap<>();
        return result;
    }

    @SignatureAuth
    @Operation(summary = "查询登录状态")
    @GetMapping("/isLogin")
    @CrossOrigin(origins = "*")
    public Map<String, Object> isLogin() {
        Map<String, Object> result = new HashMap<>();
        result.put("isLogin", StpUtil.isLogin());

        log.info("Login status checked: isLogin={}", StpUtil.isLogin());
        return result;
    }

//    @SignatureAuth
//    @Operation(summary = "用户注册")
//    @GetMapping("/register")
//    @CrossOrigin(origins = "*")
//    public Map<String, Object> register(@Validated @RequestBody ) {
//        Map<String, Object> result = new HashMap<>();
//        result.put("code", ErrorCode.SUCCESS.getCode());
//        result.put("msg", ErrorCode.SUCCESS.getTitle());
//        Map<String, Object> data = new HashMap<>();
//        data.put("isLogin", StpUtil.isLogin());
//        result.put("data", data);
//
//        log.info("Login status checked: isLogin={}", StpUtil.isLogin());
//        return result;
//    }

//    @SignatureAuth
    @PostMapping("/send/email")
    @CrossOrigin(origins = "*")
    public boolean sendEmail(@Validated @RequestBody SendMailQry sendMailQry) {
        return sendEmailService.send(sendMailQry.getTemplateId(), sendMailQry.getEmail());
    }



    @SignatureAuth
    @PostMapping("/list")
    @CrossOrigin(origins = "*")
    public PageInfo<UserVO> userList(@RequestBody Map<String, Object> requestMap) {
        int pageNum = (int) requestMap.get("pageNum");
        int pageSize = (int) requestMap.get("pageSize");
        return userService.getAllUsers(pageNum, pageSize);
    }
}
