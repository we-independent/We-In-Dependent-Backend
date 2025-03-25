package com.weindependent.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.weindependent.app.service.EmailService;
import com.weindependent.app.service.GoogleOAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GoogleOAuthServiceImpl implements GoogleOAuthService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.token.uri}")
    private String tokenUri;

    @Value("${google.user.info.uri}")
    private String userInfoUri;

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

            if (response == null) {
                log.error("Failed to get access token: Response is null");
                throw new RuntimeException("Failed to get access token: Response is null");
            }

            JSONObject json = JSONObject.parseObject(response);

            if (!json.containsKey("access_token")) {
                log.error("Failed to get access token: {}", json.toJSONString());
                throw new RuntimeException("Failed to get access token " + json.toJSONString());
            }

            return json.getString("access_token");

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("HTTP Error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to get access token. Try using a new Google OAuth code and try again. " + e.getMessage());
        } catch (org.springframework.web.client.ResourceAccessException e) {
            log.error("Network Error: {}", e.getMessage());
            throw new RuntimeException("Network Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected Error: {}", e.getMessage());
            throw new RuntimeException("Unexpected Error: " + e.getMessage());
        }
    }

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
     * Combines access token retrieval and user info fetching
     */
    public Map<String, String> exchangeCodeForUserInfo(String code) {
        String accessToken = getAccessToken(code);
        return getUserInfo(accessToken);
    }
}