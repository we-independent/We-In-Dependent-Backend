package com.weindependent.app.controller;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.service.UserService;
import com.weindependent.app.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户管理")
@Slf4j
@RestController
@RequestMapping(value = "/user", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "登录")
    @SignatureAuth
    @PostMapping("/login")
    public String login(@Validated LoginVO loginVo){
        UserDO user = userService.queryByUsernameAndPassword(loginVo.getUsername(), loginVo.getPassword());
        // Token挂载的扩展参数 （此方法只有在集成jwt插件时才会生效）
        SaLoginModel loginModel = new SaLoginModel();
        loginModel.setExtra("username", user.getUsername());
        StpUtil.login(user.getId(), loginModel);

        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("saTokenInfo", saTokenInfo);
        return JSON.toJSONString(map);
    }

    @SignatureAuth
    @PostMapping("/demo")
    public String demo() {
        return "";
    }
}
