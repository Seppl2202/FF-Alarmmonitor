package de.ff.jf.bftag.alarmmonitor.workflow;

import de.ff.jf.bftag.alarmmonitor.Alarm;

public interface AlarmDispatchWorkflow {

    public void start(Alarm alarm);
}
