package com.weindependent.app.database.dataobject;
import lombok.Data;

@Data
public class NotificationSettingsDO {
     private Long userId;

    // Updates from We Independent
    private Boolean updatesEnabled;
    private Boolean generalAnnouncements;
    private Boolean newPrograms;
    private Boolean holidayMessages;

    // Donation and payments
    private Boolean donationsEnabled;
    private Boolean donationConfirmations;
    private Boolean donationUpdates;
    private Boolean billingIssues;

    // Event confirmation and reminders
    private Boolean eventsEnabled;
    private Boolean rsvpConfirmations;
    private Boolean eventUpdates;

    // Help center
    private Boolean helpCenterEnabled;
    // private Boolean responseAndUpdate;

}
