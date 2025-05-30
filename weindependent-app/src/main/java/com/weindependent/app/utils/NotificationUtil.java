package com.weindependent.app.utils;

import java.util.Map;

import com.weindependent.app.database.dataobject.NotificationSettingsDO;
import com.weindependent.app.enums.MailTypeEnum;
import com.weindependent.app.service.IEmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationUtil {
    
    public static void sendNotificationIfEnabled(
        NotificationSettingsDO settingsDO,
        String fieldName,
        String userEmail,
        MailTypeEnum userMailType,
        Map<String, String> userParam,
        IEmailService emailService,
        boolean nofifyAdminIfDisabled,
        String adminEmail, 
        MailTypeEnum adminMailType,
        Map<String, String> adminParam,
        Long userId,
        org.slf4j.Logger logger
    ){
        boolean enabled = isNotificationEnabled(settingsDO, fieldName);
        if(enabled){
            log.info("开始向 {} 发送用户确认邮件", userEmail);
            emailService.send(userEmail, userMailType, userParam);
        } 
        if(nofifyAdminIfDisabled && adminEmail != null && adminMailType != null){
                emailService.send(adminEmail, adminMailType, adminParam);
        }
        
    }

    private static boolean isNotificationEnabled(NotificationSettingsDO settings, String fieldName){
        if(settings == null) return false;
        switch(fieldName){
            case "updatesEnabled": return Boolean.TRUE.equals(settings.getUpdatesEnabled());
            case "updatesGeneralAnnouncements": return Boolean.TRUE.equals(settings.getUpdatesGeneralAnnouncements());
            case "updatesNewPrograms": return Boolean.TRUE.equals(settings.getUpdatesNewPrograms());
            case "updatesHolidayMessages": return Boolean.TRUE.equals(settings.getUpdatesHolidayMessages());case "donationsEnabled": return Boolean.TRUE.equals(settings.getDonationsEnabled());
            
            case "donationsDonationConfirmations": return Boolean.TRUE.equals(settings.getDonationsDonationConfirmations());
            case "donationsDonationUpdates": return Boolean.TRUE.equals(settings.getDonationsDonationUpdates());
            case "donationsBillingIssues": return Boolean.TRUE.equals(settings.getDonationsBillingIssues());

            case "eventsEnabled": return Boolean.TRUE.equals(settings.getEventsEnabled());
            case "eventsRsvpConfirmations": return Boolean.TRUE.equals(settings.getEventsRsvpConfirmations());
            case "eventsEventUpdates": return Boolean.TRUE.equals(settings.getEventsEventUpdates());

            case "helpCenterEnabled": return Boolean.TRUE.equals(settings.getHelpCenterEnabled());

            default:
                log.warn("Invalid notification field: {}", fieldName);
                return false;
        }        
    }
}
