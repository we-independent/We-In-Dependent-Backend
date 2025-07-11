package com.weindependent.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.weindependent.app.convertor.UserConvertor;
import com.weindependent.app.service.UserService;
import com.weindependent.app.service.GoogleOAuthService;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.vo.user.GoogleUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Tag(name = "谷歌验证管理")
@Slf4j
@RestController
@RequestMapping(value = "/auth", produces = "application/json;charset=UTF-8")
public class GoogleOAuthController {

//  public static final String GOOGLE_PASSWORD = "googlegoogle";
  @Resource
  private UserService userService;

  @Resource
  private GoogleOAuthService googleOAuthService;

  @Value("${google.client.id}")
  private String clientId;

  @Value("${google.redirect.uri}")
  private String redirectUri;

  @Value("${google.scope}")
  private String scope;

  @Operation(summary = "Google Login")
  @GetMapping("/google-login")
  public GoogleUserVO googleLogin(@RequestParam("code") String code) {
    Map<String, String> userInfo = googleOAuthService.exchangeCodeForUserInfo(code);
    if (!userInfo.containsKey("email")) {
      throw new RuntimeException("Failed to retrieve user info from Google");
    }

    // Extract user details from Google
    String email = userInfo.get("email");
    String name = userInfo.get("name");

    UserDO foundUser = userService.findUserByAccount(email);
    if (foundUser == null || foundUser.getId() == null) {
        GoogleUserVO googleUserDTO = new GoogleUserVO();
        googleUserDTO.setUsername(name);
        googleUserDTO.setEmail(email);
        googleUserDTO.setNewUser(true);
      return googleUserDTO;
    }

    StpUtil.login(foundUser.getId());
    log.info("User {} google logged in successfully using email {}", name, email);
    return UserConvertor.toGoogleUserVO(foundUser,false);
  }

  /**
   This is a temp function for simulating getting google oAuth code at frontend side,
   Taking this code and call auth/google-login to test google login flow.
   TODO: remove this function after frontend integration is done or making admin authorization to this endpoint.
   */
  @Operation(summary = "Google Login Get Code")
  @GetMapping("temp/google-login-callback")
  private String googleCallBackTemp(@RequestParam String code) {
    System.out.println("Code from google oAuth: "+ code);
    return code;
  }

  /**
   This is a temp function for simulating connecting google oAuth from frontend side,
   This function would redirect to google oAuth page and trigger callback function above
   TODO: remove this function after frontend integration is done or making admin authorization to this endpoint.
   */
  @Operation(summary = "Google Login Simulation")
  @GetMapping("temp/google-login")
  private void onGoogleBtnClicked(HttpServletResponse response) throws IOException {
    String googleOAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth"
            + "?client_id=" + clientId
            + "&redirect_uri=" + redirectUri
            + "&response_type=code"
            + "&scope=" + scope
            + "&access_type=offline";

    response.sendRedirect(googleOAuthUrl);
  }
}