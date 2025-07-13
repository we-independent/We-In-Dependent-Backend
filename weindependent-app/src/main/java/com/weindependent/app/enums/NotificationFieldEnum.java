
package com.weindependent.app.enums;

import com.weindependent.app.database.dataobject.NotificationSettingsDO;
import com.weindependent.app.exception.ResponseException;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum NotificationFieldEnum {

    // Updates from We Independent managed in dashboard
    UPDATES_ENABLED("updatesEnabled", NotificationSettingsDO::getUpdatesEnabled),
    UPDATES_GENERAL_ANNOUNCEMENTS("updatesGeneralAnnouncements", NotificationSettingsDO::getUpdatesGeneralAnnouncements),
    UPDATES_NEW_PROGRAMS_OR_FEATURES("updatesNewProgramsOrFeatures", NotificationSettingsDO::getUpdatesNewProgramsOrFeatures),
    UPDATES_MONTHLY_HIGHLIGHT("updatesMonthlyHighlight", NotificationSettingsDO::getUpdatesMonthlyHighlight),
    UPDATES_HOLIDAY_MESSAGES("updatesHolidayMessages", NotificationSettingsDO::getUpdatesHolidayMessages),
    
    // Updates for Donation
    DONATION_ENABLED("donationsEnabled", NotificationSettingsDO::getDonationsEnabled),
    
    
    // Events
    EVENTS_ENABLED("eventsEnabled", NotificationSettingsDO::getEventsEnabled),
    EVENTS_EVENT_REMINDER("eventsEventReminder", NotificationSettingsDO::getEventsEventReminder),
    EVENTS_EVENT_FOLLOW_UP("eventsEventFollowUp", NotificationSettingsDO::getEventsEventFollowUp);

    private final String fieldName;
    private final Function<NotificationSettingsDO, Boolean> getter;

    public boolean isEnabled(NotificationSettingsDO settings) {
        return Boolean.TRUE.equals(getter.apply(settings));
    }

    // public static boolean isEnabledByFieldName(String fieldName, NotificationSettingsDO settingsDO) {
    //     if (fieldName == null || settingsDO == null) return false;
    //     try {
    //         NotificationFieldEnum fieldEnum = NotificationFieldEnum.valueOf(fieldName);
    //         return fieldEnum.isEnabled(settingsDO);
    //     } catch (IllegalArgumentException e) {
    //         // 枚举中没有这个字段名
    //         return false;
    //     }
    // }

    public static boolean isValidField(String input) {
        for (NotificationFieldEnum field : values()) {
            if (field.getFieldName().equals(input)) {
                return true;
            }
        }
        return false;
    }


}
