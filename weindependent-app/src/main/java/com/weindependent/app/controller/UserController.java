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
import com.weindependent.app.service.UserService;
import com.weindependent.app.vo.LoginVO;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import com.weindependent.app.vo.UserVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
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

//    @ApiOperation(value = "登录")
    @Operation(summary = "登录")
    @SignatureAuth
    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public LoginVO login(@Validated @RequestBody LoginQry loginQry){
        UserDO user = userService.queryByUsernameAndPassword(loginQry.getUsername(), loginQry.getPassword());
        // Token挂载的扩展参数 （此方法只有在集成jwt插件时才会生效）
        SaLoginModel loginModel = new SaLoginModel();
        loginModel.setExtra("username", user.getAccount());
        log.info("user.getId() is {}", user.getId());
        StpUtil.login(user.getId(), loginModel);

        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        LoginVO loginVO = new LoginVO();
        loginVO.setSaTokenInfo(saTokenInfo);
        return loginVO;
    }

    @SignatureAuth
    @PostMapping("/demo")
    @CrossOrigin(origins = "*")
    public String demo() {
        return "This is just for demo";
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
