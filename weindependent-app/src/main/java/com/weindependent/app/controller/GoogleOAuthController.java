package com.weindependent.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.weindependent.app.service.UserService;
import com.weindependent.app.vo.GoogleUserVO;
import com.weindependent.app.database.dataobject.UserDO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletResponse;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "Google OAuth")
@Slf4j
@RestController
@RequestMapping(value = "/auth", produces = "application/json;charset=UTF-8")
public class GoogleOAuthController {

  @Resource
  private UserService userService;

  @Value("${google.client.id}")
  private String clientId;

  @Value("${google.client.secret}")
  private String clientSecret;

  @Value("${google.redirect.uri}")
  private String redirectUri;

  @Value("${google.scope}")
  private String scope;

  @Value("${google.token.uri}")
  private String tokenUri;

  @Value("${google.user.info.uri}")
  private String userInfoUri;

  /**
   * Step 2: Handle Callback from Google
   */
  @Operation(summary = "Google Login")
  @GetMapping("/google-login")
  @CrossOrigin(origins = "*")
  public GoogleUserVO googleLogin(@RequestParam("code") String code) {
    // Step 1: Exchange Authorization Code for Access Token
    String accessToken = getAccessToken(code);
    if (accessToken == null) {
      throw new RuntimeException("Failed to obtain access token");
    }

    // Step 2: Fetch User Info from Google
    Map<String, String> userInfo = getUserInfo(accessToken);
    if ( !userInfo.containsKey("email")) {
      throw new RuntimeException("Failed to retrieve user info from Google");
    }

    // Extract user details
    String email = userInfo.get("email");
    String name = userInfo.get("name");


    // Step 3: Create or Retrieve User
    UserDO user = new UserDO();
    user.setEmail(email);
    user.setAccount(email);
    user.setRealName(name);
    user.setLoginProvider("google");
    user.setPassword("googlegoogle");

    GoogleUserVO foundUser;
    try {
      foundUser = userService.findOrCreateGoogleUser(user);
      if (foundUser == null || foundUser.getId() == null) {
        throw new RuntimeException("User creation or retrieval failed: user ID is null");
      }
    } catch (Exception e) {
      log.error("Error in findOrCreateUser: {}", e.getMessage(), e);
      throw new RuntimeException("Google login failed: " + e.getMessage());
    }

    // Step 4: Authenticate with Sa-Token
    StpUtil.login(foundUser.getId());

    log.info("User {} google logged in successfully using email {}", name, email);
    return foundUser;
  }

  /**
   * Step 2.1: Exchange Authorization Code for Access Token
   */
  private String getAccessToken(String code) {
    RestTemplate restTemplate = new RestTemplate();

    Map<String, String> params = new HashMap<>();
    params.put("code", code);
    params.put("client_id", clientId);
    params.put("client_secret", clientSecret);
    params.put("redirect_uri", redirectUri);
    params.put("grant_type", "authorization_code");

    try {
      String response = restTemplate.postForObject(tokenUri, params, String.class);

      // Check if response is null
      if (response == null) {
        log.error("Failed to get access token: Response is null");
        throw new RuntimeException("Failed to get access token: Response is null");
      }

      JSONObject json = JSONObject.parseObject(response);

      // Check if access_token is present
      if (!json.containsKey("access_token")) {
        log.error("Failed to get access token: " + json.toJSONString());
        throw new RuntimeException("Failed to get access token " + json.toJSONString());
      }

      return json.getString("access_token");

    } catch (org.springframework.web.client.HttpClientErrorException e) {
      // Handles HTTP errors (e.g., 400, 401, 500)
      log.error("HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
      throw new RuntimeException("Failed to get access token, this may happened if the token is used more than 1 time or the token is expired, or the GCP oAuth account is not matched" +
              " try using new google oAuth code and try again: " + e.getMessage());
    } catch (org.springframework.web.client.ResourceAccessException e) {
      // Handles connection timeout or network issues
      log.error("Network Error: " + e.getMessage());
      throw new RuntimeException("Network Error: " + e.getMessage());
    } catch (Exception e) {
      // Catches any other unexpected errors
      log.error("Unexpected Error: " + e.getMessage());
      throw new RuntimeException("Unexpected Error: " + e.getMessage());
    }
  }

  /**
    Fetch User Info from Google
   */
  private Map<String, String> getUserInfo(String accessToken) {
    RestTemplate restTemplate = new RestTemplate();

    String requestUri = userInfoUri + "?access_token=" + accessToken;
    String response = restTemplate.getForObject(requestUri, String.class);

    JSONObject json = JSONObject.parseObject(response);

    Map<String, String> userInfo = new HashMap<>();
    userInfo.put("email", json.getString("email"));
    userInfo.put("name", json.getString("name"));
    userInfo.put("picture", json.getString("picture"));

    return userInfo;
  }


  /**
   This is a temp function for simulating getting google oAuth code at frontend side,
   Taking this code and call auth/google-login to test google login flow.
   TODO: remove this function after frontend integration is done or making admin authorization to this endpoint.
   */
  @Operation(summary = "Google Login Get Code")
  @GetMapping("temp/google-login-callback")
  @CrossOrigin(origins = "*")
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
  @CrossOrigin(origins = "*")
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