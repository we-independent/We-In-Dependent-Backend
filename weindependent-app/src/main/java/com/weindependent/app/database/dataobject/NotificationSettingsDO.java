package com.weindependent.app.database.dataobject;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class NotificationSettingsDO {
    private Long userId;

    //default 0-if not select when signup
    // Updates from We Independent
    private Boolean updatesEnabled;
    private Boolean updatesGeneralAnnouncements;
    private Boolean updatesNewProgramsOrFeatures;
    private Boolean updatesHolidayMessages;
    private Boolean updatesMonthlyHighlight;

    // //default 1-open
    // Donation and payments
    private Boolean donationsEnabled;
    private Boolean donationsDonationConfirmations;
    private Boolean donationsDonationUpdates;
    private Boolean donationsBillingIssues;

    //default 1-open
    // Event confirmation and reminders
    private Boolean eventsEnabled;
    private Boolean eventsEventReminder;
    private Boolean eventsEventFollowUp;

    //default 1-open
    // Help center
    private Boolean helpCenterEnabled;
    // private Boolean responseAndUpdate;

}
