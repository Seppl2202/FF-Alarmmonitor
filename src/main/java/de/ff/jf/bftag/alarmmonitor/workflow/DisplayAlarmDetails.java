package de.ff.jf.bftag.alarmmonitor.workflow;

import de.ff.jf.bftag.alarmmonitor.models.Alarm;
import de.ff.jf.bftag.alarmmonitor.Main;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DisplayAlarmDetails implements WorkflowStep {

    private final Logger logger = Logger.getLogger(DisplayAlarmDetails.class.getName());

    public DisplayAlarmDetails() {
    }

    @Override
    public void executeStep() {
        Alarm alarm = FireServiceDispatchWorkflow.currentAlarm;
        Main.getMonitor().setAlarmDetails(alarm);
        logger.log(Level.INFO, "Displayed alarm details on monitor");
    }
}
