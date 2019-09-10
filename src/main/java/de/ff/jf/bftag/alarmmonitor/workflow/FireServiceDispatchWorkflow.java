package de.ff.jf.bftag.alarmmonitor.workflow;

import de.ff.jf.bftag.alarmmonitor.models.Alarm;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FireServiceDispatchWorkflow implements AlarmDispatchWorkflow {
    private List<WorkflowStep> workflowSteps;
    public static Alarm currentAlarm;

    public FireServiceDispatchWorkflow() {
        workflowSteps = new LinkedList<>();
    }

    @Override
    public void start(Alarm alarm) {
        currentAlarm = alarm;
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> new GetCoordinatesForEndpoints().executeStep(), executorService)
                .thenRunAsync(() -> new DisplayAlarmDetails().executeStep(), executorService)
                .thenRunAsync(() -> new PlaySounds().executeStep(), executorService);
    }
}
