package de.ff.jf.bftag.alarmmonitor.workflow;

import de.ff.jf.bftag.alarmmonitor.models.Alarm;

public interface AlarmDispatchWorkflow {

    public void start(Alarm alarm);
}
