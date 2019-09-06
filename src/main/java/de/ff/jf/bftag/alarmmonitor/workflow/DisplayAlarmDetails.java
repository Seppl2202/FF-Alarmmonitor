package de.ff.jf.bftag.alarmmonitor.workflow;

import de.ff.jf.bftag.alarmmonitor.models.Alarm;
import de.ff.jf.bftag.alarmmonitor.Main;

public class DisplayAlarmDetails implements WorkflowStep {

    public DisplayAlarmDetails() {
    }

    @Override
    public void executeStep() {
        Alarm alarm = FireServiceDispatchWorkflow.currentAlarm;
        Main.getMonitor().setAlarmDetails(alarm);
    }
}
