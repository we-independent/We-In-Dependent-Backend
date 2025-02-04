//package com.weindependent.app.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//
//@Configuration
//public class OAuth2ClientConfig {
//
//    private static String CLIENT_ID = "your_client_id";
//    private static String CLIENT_SECRET = "your_client_secret";
//    private static String AUTHORIZATION_URI = "http://localhost:8080/oauth/authorize";
//    private static String TOKEN_URI = "http://localhost:8080/oauth/token";
//    private static String REDIRECT_URI = "{baseUrl}/login/oauth2/code/{registrationId}";
//
//    @Autowired
//    public void configure(ClientRegistrationRepository clients) throws Exception {
//        ClientRegistration registration = ClientRegistration.withRegistrationId("custom")
//                .clientId(CLIENT_ID)
//                .clientSecret(CLIENT_SECRET)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUriTemplate(REDIRECT_URI)
//                .authorizationUri(AUTHORIZATION_URI)
//                .tokenUri(TOKEN_URI)
//                .scope("read", "write")
//                .build();
//
//        clients.addRegistration("custom", registration);
//    }
//}
