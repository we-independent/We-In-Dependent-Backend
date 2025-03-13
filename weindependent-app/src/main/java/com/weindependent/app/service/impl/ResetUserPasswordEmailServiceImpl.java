package com.weindependent.app.service.impl;

import com.weindependent.app.config.EmailConfig;
import com.weindependent.app.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Base64;
import java.util.Properties;

@Slf4j
@Service
public class ResetUserPasswordEmailServiceImpl implements EmailService {
    @Resource
    private EmailConfig emailConfig;

    private final static String subject = "Reset Password";
    private final static String text = "<font color=\"green\">This is test email.</font>";

    public boolean send(String email) {
        try {
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setUsername(emailConfig.getUsername());
            byte[] decoded = Base64.getDecoder().decode(emailConfig.getPassword());
            String password = new String(decoded);
            javaMailSender.setPassword(password);
            javaMailSender.setHost(emailConfig.getHost());
            javaMailSender.setPort(emailConfig.getPort());
            javaMailSender.setProtocol(emailConfig.getProtocol());
            javaMailSender.setDefaultEncoding(emailConfig.getEncoding());
            Properties mailProperties = new Properties();
            mailProperties.put("mail.smtp.auth", true);
            mailProperties.put("mail.smtp.starttls.enable", true);
            javaMailSender.setJavaMailProperties(mailProperties);
            MimeMessage mimeMessage = getMimeMessage(email, subject, text, javaMailSender);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("发往 {} 重置密码邮件发送异常", email, e);
            return false;
        }
        return true;
    }

    private MimeMessage getMimeMessage(String toEmail, String subject, String text, JavaMailSenderImpl javaMailSender) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, emailConfig.getEncoding());
        mimeMessageHelper.setFrom(emailConfig.getSenderName());
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, true);
        return mimeMessage;
    }
}
