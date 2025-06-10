
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
    UPDATES_NEW_PROGRAMS("updatesNewPrograms", NotificationSettingsDO::getUpdatesNewPrograms),
    UPDATES_HOLIDAY_MESSAGES("updatesHolidayMessages", NotificationSettingsDO::getUpdatesHolidayMessages),

    // Events
    EVENTS_ENABLED("eventsEnabled", NotificationSettingsDO::getEventsEnabled),
    EVENTS_RSVP_CONFIRMATIONS("eventsRsvpConfirmations", NotificationSettingsDO::getEventsRsvpConfirmations),
    EVENTS_EVENT_UPDATES("eventsEventUpdates", NotificationSettingsDO::getEventsEventUpdates);

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

    //for general announcement
    public static NotificationFieldEnum fromMessageType(String type) {
        if (type == null) return null;
        switch (type.toLowerCase()) {
            case "general":
                return UPDATES_GENERAL_ANNOUNCEMENTS;
            case "programs":
                return UPDATES_NEW_PROGRAMS;
            case "holiday":
                return UPDATES_HOLIDAY_MESSAGES;
            default:
                throw new ResponseException(ErrorCode.UNKONWN_CATEGORY.getCode(), "Unknown notification type: " + type);
        }
    }

}
