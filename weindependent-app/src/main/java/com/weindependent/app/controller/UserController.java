package com.weindependent.app.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.temp.SaTempUtil;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.dataobject.NotificationSettingsDO;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.database.mapper.weindependent.UserImageMapper;
import com.weindependent.app.dto.*;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.enums.MailTypeEnum;
import com.weindependent.app.enums.NotificationFieldEnum;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IEmailService;
import com.weindependent.app.service.UserService;
import com.weindependent.app.service.impl.EmailServiceImpl;
import com.weindependent.app.utils.PasswordUtil;
import com.weindependent.app.vo.LoginVO;
import com.weindependent.app.vo.user.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;



@Tag(name = "用户管理")
@Slf4j
@RestController
// @RequestMapping(value = "/user", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
@RequestMapping(value = "/api/user", produces = "application/json;charset=UTF-8")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private IEmailService emailService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Resource
    private UserImageMapper userImageMapper;


    @SignatureAuth
    @Operation(summary = "用户注册")
    @PostMapping(value = "/register", consumes = "application/json;charset=UTF-8")
    public void register(@Validated @RequestBody RegisterQry registerQry) {
        userService.registerUser(registerQry);
        log.info("User registered successfully: {}", registerQry.getAccount());
    }

    @SignatureAuth
    @Operation(summary = "用户登录")
    // @PostMapping("/login")    
    @PostMapping(value = "/login", consumes = "application/json;charset=UTF-8")
    public LoginVO login(@Validated @RequestBody LoginQry loginQry) throws Exception {
        UserDO user = userService.queryByEmailAndPassword(loginQry.getEmail(), loginQry.getPassword());

        if (ObjectUtils.isEmpty(user) || !PasswordUtil.verifyPassword(loginQry.getPassword(), user.getPassword())) {
            log.error("Login failed for user: {}", loginQry.getEmail());
            throw new ResponseException(ErrorCode.USERNAME_PASSWORD_ERROR.getCode(), ErrorCode.USERNAME_PASSWORD_ERROR.getTitle());
        }

        SaLoginModel loginModel = new SaLoginModel();
        loginModel.setExtra("username", user.getRealName());
        loginModel.setIsLastingCookie(loginQry.getRemember());
        StpUtil.login(user.getId(), loginModel);

        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        LoginVO loginVO = new LoginVO();
        loginVO.setSaTokenInfo(saTokenInfo);
        loginVO.setEmail(user.getAccount());
        loginVO.setUsername(user.getRealName());
        loginVO.setLanguage(user.getLanguage());
        loginVO.setVisaType(user.getVisaType());
        loginVO.setSubscription(user.isSubscription());
        loginVO.setAvatar(user.getAvatar());
        log.info("Login successful for user: {}", loginQry.getEmail());
        return loginVO;
    }
    @SignatureAuth
    @Operation(summary = "重置密码")
    // @PostMapping("/reset/password")
    @PostMapping(value = "/reset/password", consumes = "application/json;charset=UTF-8")
    public void resetPassword(@Validated @RequestBody ResetPasswordQry resetPasswordQry)throws Exception{
        String token = resetPasswordQry.getToken();
        int success = userService.resetPassword(token, resetPasswordQry.getPassword());
        if (success == 0) {
            return;
        } else if (success == ErrorCode.TOKEN_NOT_EXIST_OR_EXPIRED.getCode()){
            throw new ResponseException(ErrorCode.TOKEN_NOT_EXIST_OR_EXPIRED.getCode(), ErrorCode.TOKEN_NOT_EXIST_OR_EXPIRED.getTitle());
        } else if (success == ErrorCode.USER_NOT_EXIST.getCode()){
            throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), ErrorCode.USER_NOT_EXIST.getTitle());
        } else if (success == ErrorCode.UPDATE_DB_FAILED.getCode()){
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), ErrorCode.UPDATE_DB_FAILED.getTitle());
        }
    }

//    @Operation(summary = "登录")
//    @SignatureAuth
//    @PostMapping("/login")
//
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

    public Map<String, Object> logout() {
        StpUtil.logout();

        Map<String, Object> result = new HashMap<>();
        return result;
    }

    @SignatureAuth
    @Operation(summary = "查询登录状态")
    @GetMapping("/isLogin")

    public Map<String, Object> isLogin() {
        Map<String, Object> result = new HashMap<>();
        result.put("isLogin", StpUtil.isLogin());
        log.info("Login status checked: isLogin={}", StpUtil.isLogin());
        return result;
    }

//    @SignatureAuth
//    @Operation(summary = "用户注册")
//    @GetMapping("/register")
//
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
    @Operation(summary = "发送邮件")
    // @PostMapping("/send/email")
    @PostMapping(value = "/send/email", consumes = "application/json;charset=UTF-8")
    public boolean sendEmail(@Validated @RequestBody SendMailQry sendMailQry) {
        Map<String, String> sendMailParams = new HashMap<>();
        String email= sendMailQry.getEmail();;
        UserDO user= userService.findUserByAccount(email);
        if(user == null) {
            throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), ErrorCode.USER_NOT_EXIST.getTitle()); // no such user return true directly
        }
        sendMailParams.put("name", user.getRealName());
        String token = SaTempUtil.createToken(user.getId(), 900);
        sendMailParams.put("link", frontendUrl + "/?reset-password=true&token="+token);
        return emailService.send(sendMailQry.getEmail(),MailTypeEnum.RESET_PASSWORD, sendMailParams);
    }



    @SignatureAuth
    @Operation(summary = "用户列表")
    // @PostMapping("/list")
    @PostMapping(value = "/list", consumes = "application/json;charset=UTF-8")
    public PageInfo<UserVO> userList(@RequestBody Map<String, Object> requestMap) {
        int pageNum = (int) requestMap.get("pageNum");
        int pageSize = (int) requestMap.get("pageSize");
        return userService.getAllUsers(pageNum, pageSize);
    }

    @SignatureAuth
    @Operation(summary = "更新使用者资料")
    // @PutMapping("/")
    @PutMapping(value = "/", consumes = "application/json;charset=UTF-8")

    public void updateUser(@Valid @RequestBody UpdateUserQry updateUserQry) {
        userService.updateUser(updateUserQry);
    }

    /**
     * 新增profile图片
     */
    @SignatureAuth
    @Operation(summary = "新增个人头像图片")
    @PostMapping(value = "/profile/img/upload", consumes =
            MediaType.MULTIPART_FORM_DATA_VALUE)
    public  String createProfileImg(@RequestParam("file")  MultipartFile file) {
        return userService.createProfileImg(file);
    }
    @SignatureAuth
    @Operation(summary = "删除账号（用户主动注销）")
    @DeleteMapping("/")

    public void deleteAccount() {
        Long userId = StpUtil.getLoginIdAsLong();
        userService.deleteAccount(userId);
        log.info("DELETE /user/ triggered by userId: {}", userId);
        StpUtil.logout();
        log.info("Account deleted and user logged out: userId={}", userId);
    }
    @SignatureAuth
    @Operation(summary = "验证旧密码")
    // @PostMapping("/verify-password")
    @PostMapping(value = "/verify-password", consumes = "application/json;charset=UTF-8")
    public void verifyPassword(@Valid @RequestBody VerifyPasswordQry verifyPasswordQry) {
        userService.verifyPassword(verifyPasswordQry);
    }

    @SignatureAuth
    @Operation(summary = "修改密码（登录状态）")
    // @PutMapping("/change-password")
    @PutMapping(value = "/change-password", consumes = "application/json;charset=UTF-8")
    public void changePassword(@Valid @RequestBody ChangePasswordQry changePasswordQry) {
        userService.changePassword(changePasswordQry);
    }
    //User Profile Help Center
    @SignatureAuth
    @Operation(summary = "提交 Help Center 请求")
    @PostMapping(value = "/profile/help/send", consumes = "application/json;charset=UTF-8")
    public void sendHelp(@RequestBody HelpCenterRequestQry qry) {
        Long userId = StpUtil.getLoginIdAsLong();
        UserDO user = userService.findUserById(userId);
        if (user == null) {
            throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), "用户不存在");
        }
        userService.saveHelpRequest(userId, qry);
    }
    
}
