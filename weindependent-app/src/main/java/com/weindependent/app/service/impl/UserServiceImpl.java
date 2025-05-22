package com.weindependent.app.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.database.mapper.weindependent.UserMapper;
import com.weindependent.app.dto.ChangePasswordQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.enums.GoogleDriveFileCategoryEnum;
import com.weindependent.app.enums.MailTypeEnum;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.UserService;
import com.weindependent.app.utils.PageInfoUtil;
import com.weindependent.app.utils.PasswordUtil;
import com.weindependent.app.vo.user.UserVO;
import com.weindependent.app.dto.UpdateUserQry;
import com.weindependent.app.convertor.UserConvertor;

import cn.dev33.satoken.temp.SaTempUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.dto.HelpCenterRequestQry;
import com.weindependent.app.dto.RegisterQry;
import cn.dev33.satoken.stp.StpUtil;

import com.weindependent.app.database.dataobject.HelpCenterRequestDO;
import com.weindependent.app.database.dataobject.ImageDO;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.weindependent.app.database.mapper.weindependent.UserHelpCenterMapper;
import com.weindependent.app.database.mapper.weindependent.UserImageMapper;
import com.weindependent.app.service.IFileService;
import com.weindependent.app.vo.UploadedFileVO;

import javax.annotation.Resource;

import java.time.LocalDateTime;
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
    private EmailServiceImpl resetUserPasswordEmailService;

        @Autowired
    private UserHelpCenterMapper userHelpCenterMapper;

    @Autowired
    private EmailServiceImpl emailServiceImpl;


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

        return userMapper.insert(user) > 0;
    }

    @Override
    public UserDO findUserByAccount(String account) {
        return userMapper.findByAccount(account);
    }

    @Override
    public int resetPassword(String token, String newPassword){
        //temp token see reference https://sa-token.cc/doc.html#/plugin/temp-token?id=临时token令牌认证
        String userId = SaTempUtil.parseToken(token, String.class);

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
        if (userMapper.updatePassword(Integer.parseInt(userId), hashedPassword) <= 0) {
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
            UserDO userDO = UserConvertor.toUserDO(userId, updateUserQry);
            userMapper.updateUser(userDO);
        }

        @Override
        public ImageDO createProfileImg(MultipartFile file) {
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

            ImageDO imageDo = new ImageDO();
            imageDo.setFileName(uploadedFileVO.getFileName());
            imageDo.setFileKey(uploadedFileVO.getFileKey());
            imageDo.setFileType(resizedFile.getContentType());
            imageDo.setFilePath(uploadedFileVO.getFilePath());
            int affectedRows = profileImageMapper.create(imageDo);
            if (affectedRows != 1) {
                throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Fail to add image to db");
            }
            return imageDo;
        }

        @Override
        public void saveHelpRequest(Long userId, HelpCenterRequestQry qry){
            UserDO user = userMapper.findById(userId);
            if(user == null){
                throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), "User is not exist.");
            }
            String userName = Optional.ofNullable(user.getRealName()).orElse("User");
            String userEmail = Optional.ofNullable(user.getAccount()).orElse("unknown@noemail.com");

            HelpCenterRequestDO request = new HelpCenterRequestDO();
            request.setUserId(userId);
            request.setName(userName);
            request.setEmail(userEmail);
            request.setSubject(qry.getSubject());
            request.setMessage(qry.getMessage());
            request.setCreateTime(LocalDateTime.now());

            userHelpCenterMapper.insert(request);

            // Confirmation email send to user
            Map<String, String> emailParams = new HashMap<>();
            emailParams.put("name", userName);
            emailParams.put("message", qry.getMessage());

            emailServiceImpl.send(userEmail, MailTypeEnum.HELP_CENTER, emailParams);

            // 4. 抄送客服邮箱
            Map<String, String> adminMailParams = new HashMap<>();
            adminMailParams.put("name", userName);
            adminMailParams.put("email", userEmail);
            adminMailParams.put("subject", qry.getSubject());
            adminMailParams.put("message", qry.getMessage());
            adminMailParams.put("replyTo", userEmail);

            emailServiceImpl.send("info@weindependent.org", MailTypeEnum.HELP_CENTER_NOTIFY, adminMailParams);
        }

        @Override
        public void changePassword(ChangePasswordQry changePasswordQry) {
            Long userId = StpUtil.getLoginIdAsLong();

            // Step 1: validate user password
            UserDO user = userMapper.findById(userId);
            if (ObjectUtils.isEmpty(user) || !PasswordUtil.verifyPassword(changePasswordQry.getOldPassword(), user.getPassword())) {
                throw new ResponseException(ErrorCode.USERNAME_PASSWORD_ERROR.getCode(), "Invalid old password");
            }

            // Step 2: change user password

            String hashedNewPassword = PasswordUtil.hashPassword(changePasswordQry.getNewPassword());

            if (userMapper.changePassword(userId,hashedNewPassword)==0) {
                log.error("Failed to change password because user does not exist for userId: {}.", userId);
                throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), ErrorCode.USER_NOT_EXIST.getTitle());
            }


            log.info("Change password successful for userId {}", userId);
        }
}
