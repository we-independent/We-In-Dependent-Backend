package com.weindependent.app.service.impl;

import com.weindependent.app.config.EmailConfig;
import com.weindependent.app.enums.MailTypeEnum;
import com.weindependent.app.service.IEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
public class EmailServiceImpl implements IEmailService {
    private final String CONTACT_EMAIL = "weindependentweb@gmail.com";
    private final String LOGO = "https://static1.squarespace.com/static/66e0b29d4524895c4ed21106/t/679af0f2c11f19074b350598/1737400680356/";

    @Autowired
    private EmailConfig emailConfig;


    @Autowired
    private ResourceLoader resourceLoader;


    public boolean send(String receiverEmail, MailTypeEnum mailTypeEnum, Map<String,String> sendMailParams) {
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
            sendMailParams.put("contactEmail",CONTACT_EMAIL);
            sendMailParams.put("logo",LOGO);
            String emailHtml;
            try{
                emailHtml = getEmailHtml(mailTypeEnum, sendMailParams);
            } catch(IOException e){
                log.error("Error loading email HTML template", e);
                return false;
            }
            MimeMessage mimeMessage = getMimeMessage(receiverEmail, mailTypeEnum.getSubject(), emailHtml, javaMailSender);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("发往 {} 重置密码邮件发送异常", receiverEmail, e);
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

    private String getEmailHtml(MailTypeEnum mailTypeEnum,Map<String, String> sendMailParams) throws IOException {
        Resource resource = resourceLoader.getResource(mailTypeEnum.getPath());
        try (InputStream inputStream = resource.getInputStream()) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024]; // 或者其他合适的缓冲区大小
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

            String html = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
            // 替換模板中的 {{key}} 為參數值
            for (Map.Entry<String, String> entry : sendMailParams.entrySet()) {
                html = html.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            return html;
        }
    }
}
