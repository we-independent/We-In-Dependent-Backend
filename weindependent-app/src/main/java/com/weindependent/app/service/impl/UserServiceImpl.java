package com.weindependent.app.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.api.client.googleapis.notifications.NotificationUtils;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.database.mapper.weindependent.UserMapper;
import com.weindependent.app.database.mapper.weindependent.UserNotificationMapper;
import com.weindependent.app.dto.ChangePasswordQry;
import com.weindependent.app.dto.HelpCenterRequestQry;
import com.weindependent.app.dto.NotificationFieldUpdateQry;
import com.weindependent.app.dto.VerifyPasswordQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.enums.GoogleDriveFileCategoryEnum;
import com.weindependent.app.enums.MailTypeEnum;
import com.weindependent.app.enums.NotificationFieldEnum;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.UserService;
import com.weindependent.app.utils.PageInfoUtil;
import com.weindependent.app.utils.PasswordUtil;
import com.weindependent.app.vo.user.UserVO;
import com.weindependent.app.dto.UpdateUserQry;
import com.weindependent.app.config.EmailConfig;
import com.weindependent.app.convertor.UserConvertor;
import cn.dev33.satoken.temp.SaTempUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.weindependent.app.dto.RegisterQry;
import com.weindependent.app.dto.UpdateNotificationFieldQry;

import cn.dev33.satoken.stp.StpUtil;

import com.weindependent.app.database.dataobject.HelpCenterRequestDO;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.dataobject.NotificationSettingsDO;

import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.weindependent.app.database.mapper.weindependent.UserHelpCenterMapper;
import com.weindependent.app.database.mapper.weindependent.UserImageMapper;
import com.weindependent.app.service.IEmailService;
import com.weindependent.app.service.IFileService;
import com.weindependent.app.vo.UploadedFileVO;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.weindependent.app.utils.ImageResizeUtil;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final Integer RESIZE_WIDTH = 200;
    private final Integer RESIZE_HEIGHT = 200;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserImageMapper profileImageMapper;

    @Autowired
    private IFileService fileService;

    @Autowired
    private UserHelpCenterMapper userHelpCenterMapper;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private IEmailService iEmailService;

    @Autowired
    private UserNotificationMapper userNotificationMapper;

    @Resource
    private EmailConfig emailConfig;

//    @Override
//    public UserDO queryByUsernameAndPassword(String username, String password) {
//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("username", username);
//        paramMap.put("password", password);
//        return userMapper.query(paramMap);
//    }

//    @Override
//    public UserDO queryByEmailAndPassword(String email, String password) {
//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("email", email);
//        paramMap.put("password", password);
//        return userMapper.query(paramMap);
//    }

    @Override
    public UserDO queryByEmailAndPassword(String email, String password) {
        UserDO user = userMapper.findByAccount(email);
        if (user == null || !PasswordUtil.verifyPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
    }

    @Override
    public PageInfo<UserVO> getAllUsers(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserDO> userDOList = userMapper.getAllUsers();
        PageInfo<UserDO> userDOPageInfo = new PageInfo<>(userDOList);
        return PageInfoUtil.pageInfo2DTO(userDOPageInfo, UserVO.class);
    }

    @Override
    public boolean registerUser(RegisterQry dto) {
        UserDO existingUser = userMapper.findByAccount(dto.getAccount());
        if (existingUser != null) {
            throw new RuntimeException("Email already registered");
        }

        String hashedPassword = PasswordUtil.hashPassword(dto.getPassword());

        UserDO user = new UserDO();
        user.setAccount(dto.getAccount());
        user.setPassword(hashedPassword);
        user.setRealName(dto.getRealName());
        user.setLanguage(dto.getLanguage());
        user.setVisaType(dto.getVisaType());
        user.setLoginProvider("local");
        user.setSubscription(dto.isSubscription());
        boolean inserted = userMapper.insert(user) > 0;
        // initialize notification after register
        if(inserted){
            initializeNotificationSettings(user.getId(), dto.isSubscription());
        }
        return inserted;
    }

    @Override
    public UserDO findUserByAccount(String account) {
        return userMapper.findByAccount(account);
    }

    @Override
    public int resetPassword(String token, String newPassword){
        //temp token see reference https://sa-token.cc/doc.html#/plugin/temp-token?id=临时token令牌认证
        // String userId = SaTempUtil.parseToken(token, String.class);
        Long userId = SaTempUtil.parseToken(token, Long.class);

        long timeout = SaTempUtil.getTimeout(token);
        if (timeout < 0){
            log.info("Reset password failed because token does not exist for userId: {}.", userId);
            return ErrorCode.TOKEN_NOT_EXIST_OR_EXPIRED.getCode(); //Token does not exist or expired.
        }

        UserDO user = userMapper.findById(userId);
        if (user == null) {
            log.info("Reset password failed because user does not exist for userId: {}.", userId);
            return ErrorCode.USER_NOT_EXIST.getCode(); //user does not exist
        }

        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        user.setPassword(hashedPassword);
        // if (userMapper.updatePassword(Integer.parseInt(userId), hashedPassword) <= 0) {
        if (userMapper.updatePassword(userId, hashedPassword) <= 0) {
            log.info("Reset password failed because cannot update in DB for userId: {}.", userId);
            return ErrorCode.UPDATE_DB_FAILED.getCode(); //database update failed
        }

        SaTempUtil.deleteToken(token);
        log.info("Reset password successful for userId: {}.", userId);
        return 0; //success
    }

    //Hurely add for pdf download
    @Override
    public UserDO findUserById(Long userId) {
        return userMapper.findById(userId); 
    }

    @Override
    public void updateUser(UpdateUserQry updateUserQry) {
        Long userId = StpUtil.getLoginIdAsLong();

        // Integer profileImageId = updateUserQry.getProfileImageId();
        // if (profileImageId == null || profileImageId <= 0) {
        //     updateUserQry.setProfileImageId(null); // 不更新头像字段，保持现状或交由前端渲染默认图
        // }

        UserDO userDO = UserConvertor.toUserDO(userId, updateUserQry);
        userMapper.updateUser(userDO);
    }

    @Override
    public String createProfileImg(MultipartFile file) {
        MultipartFile resizedFile;
        try {
            resizedFile = ImageResizeUtil.resizeImage(file,
                RESIZE_WIDTH, RESIZE_HEIGHT);
        } catch (Exception e) {
            log.error("Failed to resize image before uploading: {}", e.getMessage());
            throw new RuntimeException("Failed to resize image");
        }

        // Then upload
        UploadedFileVO uploadedFileVO =
            fileService.uploadFile(resizedFile, null, GoogleDriveFileCategoryEnum.USER_PROFILE_IMAGE);


        return uploadedFileVO.getFilePath();
    }


    @Override
    public void deleteAccount(Long userId) {
        UserDO user = userMapper.findById(userId);

        int result = userMapper.deleteUserById(userId);
        log.info("Deleted rows: {}", result);
        if (result == 0) {
            log.error("Failed to delete user from database. User ID: {}", userId);
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Failed to delete account");
        }

        log.info("User account deleted successfully. User ID: {}", userId);
    }


    @Override
    public void verifyPassword(VerifyPasswordQry verifyPasswordQry) {
        Long userId = StpUtil.getLoginIdAsLong();

        UserDO user = userMapper.findById(userId);
        if (ObjectUtils.isEmpty(user) || !PasswordUtil.verifyPassword(verifyPasswordQry.getOldPassword(), user.getPassword())) {
            throw new ResponseException(ErrorCode.USERNAME_PASSWORD_ERROR.getCode(), "Invalid old password");
        }

    }

    @Override
    public void changePassword(ChangePasswordQry changePasswordQry) {
        Long userId = StpUtil.getLoginIdAsLong();
        String hashedNewPassword = PasswordUtil.hashPassword(changePasswordQry.getNewPassword());

        if (userMapper.changePassword(userId,hashedNewPassword)==0) {
            log.error("Failed to change password because user does not exist for userId: {}.", userId);
            throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), ErrorCode.USER_NOT_EXIST.getTitle());
        }

        log.info("Change password successful for userId {}", userId);
    }

    @Override
    public void saveHelpRequest(Long userId, HelpCenterRequestQry qry){
        UserDO user = userMapper.findById(userId);
        if(user == null){
            throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), "User is not exist.");
        }
        String userName = Optional.ofNullable(user.getRealName()).orElse("User");
        String userEmail = Optional.ofNullable(user.getAccount()).orElse("unknown@noemail.com");
        String year = String.valueOf(LocalDate.now().getYear());
        Integer maxSeq = userHelpCenterMapper.getMaxReferenceSequenceThisYear(year);
        int nextSeq = (maxSeq == null ? 1 : maxSeq + 1);

        String referenceId = generateReferenceId(nextSeq);

        HelpCenterRequestDO request = new HelpCenterRequestDO();
        request.setUserId(userId);
        request.setName(userName);
        request.setEmail(userEmail);
        request.setSubject(qry.getSubject());
        request.setReferenceId(referenceId);
        request.setMessage(qry.getMessage());
        request.setCreateTime(LocalDateTime.now());

        userHelpCenterMapper.insert(request);

        // admin 需要的info
        // Confirmation email send to user
        Map<String, String> emailParams = new HashMap<>();
        emailParams.put("subject", qry.getSubject());
        emailParams.put("name", userName);
        emailParams.put("message", qry.getMessage());
        emailParams.put("referenceId", request.getReferenceId());
        emailParams.put("date", LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
        
        emailServiceImpl.send(userEmail, MailTypeEnum.HELP_CENTER, emailParams);

        // 4. 抄送客服邮箱
        Map<String, String> adminMailParams = new HashMap<>();
        adminMailParams.put("name", userName);
        adminMailParams.put("email", userEmail);
        adminMailParams.put("subject", qry.getSubject());
        adminMailParams.put("question-type", qry.getQuestionType() != null ? qry.getQuestionType() : "General Inquiry");
        adminMailParams.put("referenceId", request.getReferenceId()); 
        adminMailParams.put("message", qry.getMessage());
        adminMailParams.put("replyTo", userEmail);
        String subject = "[Help Center] New Request - " + request.getSubject() + " (Ref: " + request.getReferenceId() + ")";
        adminMailParams.put("subject", subject);

        // user info 
        Map<String, String> userParams = new HashMap<>();
        userParams.put("name", userName);
        userParams.put("message", qry.getMessage());
        userParams.put("email", userEmail);             
        userParams.put("subject", qry.getSubject());

        log.info(userEmail, adminMailParams, userParams);   
        log.info("是否启用通知 helpCenterEnabled: {}", userNotificationMapper.findByUserId(userId).getHelpCenterEnabled()); 
        String adminEmail = emailConfig.getUsername(); 

        // 发送confirmation email到用户邮箱
        iEmailService.send(userEmail, MailTypeEnum.HELP_CENTER, adminMailParams);
        // log.info("📨 给客服 info@weindependent.org 抄送通知邮件");
        iEmailService.send(adminEmail, MailTypeEnum.HELP_CENTER_NOTIFY, userParams);
    }


    // Profile - Notification Settings
    @Override
    public NotificationSettingsDO getSettingsByUserId(Long userId) {
        NotificationSettingsDO notificationDO = userNotificationMapper.findByUserId(userId);
        if (notificationDO == null){ //TODO: throw exception
            throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), "Invalid user: " + userId);
        }
        return notificationDO;
    }

    // @Override //TODO: delete
    // public void saveSettingsByUserId(NotificationSettingsDO settingsDO) {
    //     if (userNotificationMapper.findByUserId(settingsDO.getUserId()) != null) {

    //         userNotificationMapper.update(settingsDO);
    //     } else {
    //         userNotificationMapper.insert(settingsDO);
    //     }
    // }

    @Override
    public void updateNotificationField(Long userId, String fieldName, Boolean fieldValue) {
 
        if (!NotificationFieldEnum.isValidField(fieldName)) {
            throw new ResponseException(ErrorCode.INVALID_PARAM.getCode(), "Invalid field name: " + fieldName);
        }
        NotificationSettingsDO notificationDO = userNotificationMapper.findByUserId(userId);
        if (notificationDO == null) {
            Boolean notificationEnable = userMapper.findNotificationEnabledByUserId(userId);
            initializeNotificationSettings(userId, Boolean.TRUE.equals(notificationEnable));
        }
        else {
            NotificationFieldUpdateQry qry = new NotificationFieldUpdateQry();
            qry.setUserId(userId);
            qry.setFieldName(fieldName);
            qry.setFieldValue(fieldValue);
            userNotificationMapper.updateField(qry);
        }
    }

    @Override
    public void initializeNotificationSettings(Long userId, Boolean subscribe){
        NotificationSettingsDO settingsDO = new NotificationSettingsDO();
        settingsDO.setUserId(userId);
        if (!Boolean.TRUE.equals(subscribe)) {
            settingsDO.setUpdatesEnabled(false);
            settingsDO.setUpdatesGeneralAnnouncements(false);
            settingsDO.setUpdatesNewProgramsOrFeatures(false);
            settingsDO.setUpdatesMonthlyHighlight(false);
            settingsDO.setUpdatesHolidayMessages(false);
        }
        userNotificationMapper.insert(settingsDO);

    }

    public static String generateReferenceId(int nextSeq) {
        String year = String.valueOf(LocalDate.now().getYear());
        return String.format("MSG-%s-%06d", year, nextSeq);
    }
}

