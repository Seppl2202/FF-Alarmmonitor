package de.ff.jf.bftag.alarmmonitor.models;

import java.util.Date;

public class AlarmDispatchDetails {
    private Date alarmCreatedDate;
    private Date alarmDispatchedDate;

    public AlarmDispatchDetails() {
    }

    public Date getAlarmCreatedDate() {
        return alarmCreatedDate;
    }

    public void setAlarmCreatedDate(Date alarmCreatedDate) {
        this.alarmCreatedDate = alarmCreatedDate;
    }

    public Date getAlarmDispatchedDate() {
        return alarmDispatchedDate;
    }

    public void setAlarmDispatchedDate(Date alarmDispatchedDate) {
        this.alarmDispatchedDate = alarmDispatchedDate;
    }
}
