package de.ff.jf.bftag.alarmmonitor.workflow;

import de.ff.jf.bftag.alarmmonitor.Alarm;

import java.util.LinkedList;
import java.util.List;

public class FireServiceDispatchWorkflow implements AlarmDispatchWorkflow {
    private List<WorkflowStep> workflowSteps;
    public static Alarm currentAlarm;

    public FireServiceDispatchWorkflow() {
        workflowSteps = new LinkedList<>();
    }


    @Override
    public void start(Alarm alarm) {
        currentAlarm = alarm;
        workflowSteps.add(new GetCoordinatesForEndpoints());
        workflowSteps.add(new DisplayAlarmDetails());
        workflowSteps.add(new PlaySounds());
        workflowSteps.forEach(WorkflowStep::executeStep);
    }
}
