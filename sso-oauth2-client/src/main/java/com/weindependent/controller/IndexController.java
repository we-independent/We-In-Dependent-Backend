package com.weindependent.controller;

import com.weindependent.commons.PaasConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhang
 */
@Slf4j
@Controller
public class IndexController {
    @Value("${HOST.FRONT_URL}")
    private String front_url;

    @RequestMapping("/")
    public String index(Authentication authentication, Model model) {
        OAuth2AuthenticationDetails detail = (OAuth2AuthenticationDetails) authentication.getDetails();
        log.info("【登录成功】username:{}, sessonId:{}, {}", authentication.getPrincipal(), detail.getSessionId(), detail.getTokenValue());
        if (StringUtils.isNotBlank(front_url)) {
            return "redirect:" + front_url + PaasConstant.PRE_FIX + detail.getTokenValue();
        } else {
            model.addAttribute("token", detail.getTokenValue());
            return "index";
        }
    }
}
