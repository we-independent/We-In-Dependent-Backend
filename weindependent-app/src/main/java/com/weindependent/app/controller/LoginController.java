package com.weindependent.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import cn.dev33.satoken.stp.StpUtil;
import com.weindependent.app.vo.LoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.weindependent.app.service.UserService;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.enums.ErrorCode;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/login", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    /**
     * 登录接口
     * @param loginVO 包含用户名和密码
     * @return 登录成功或失败的消息
     */
    @PostMapping
    public Map<String, Object> login(@Valid @RequestBody LoginVO loginVO) {
        String username = loginVO.getUsername();

        UserDO user = userService.queryByUsernameAndPassword(loginVO.getUsername(), loginVO.getPassword());

        logger.info("Login attempt by user: {}", username);

        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            StpUtil.login(username);

            response.put("code", ErrorCode.SUCCESS.getCode());
            response.put("msg", ErrorCode.SUCCESS.getTitle());
            Map<String, Object> data = new HashMap<>();
            data.put("token", StpUtil.getTokenValue());
            response.put("data", data);

            logger.info("Login successful for user: {}", username);
            return response;
        }

        // 登录失败
        response.put("code", ErrorCode.INVALID_PARAM.getCode());
        response.put("msg", ErrorCode.INVALID_PARAM.getTitle());
        response.put("data", null);

        logger.warn("Login failed for user: {}", username);
        return response;
    }

    /**
     * 登出接口
     * @return 登出结果
     */
    @GetMapping("/logout")
    public Map<String, Object> logout() {
        StpUtil.logout();

        Map<String, Object> result = new HashMap<>();
        result.put("code", ErrorCode.SUCCESS.getCode());
        result.put("msg", ErrorCode.SUCCESS.getTitle());
        result.put("data", null);

        logger.info("User logged out");
        return result;
    }

    /**
     * 查询是否登录
     * @return 登录状态
     */
    @GetMapping("/isLogin")
    public Map<String, Object> isLogin() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", ErrorCode.SUCCESS.getCode());
        result.put("msg", ErrorCode.SUCCESS.getTitle());
        Map<String, Object> data = new HashMap<>();
        data.put("isLogin", StpUtil.isLogin());
        result.put("data", data);

        logger.info("Login status checked: isLogin={}", StpUtil.isLogin());
        return result;
    }
}