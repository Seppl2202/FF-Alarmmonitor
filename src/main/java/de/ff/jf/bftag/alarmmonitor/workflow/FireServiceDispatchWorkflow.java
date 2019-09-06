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
                .thenAccept(s -> System.out.println("Got coordinates"))
                .thenRunAsync(() -> new DisplayAlarmDetails().executeStep(), executorService)
                .thenAccept(s -> System.out.println("Displayed details on monitor"))
                .thenRunAsync(() -> new PlaySounds().executeStep(), executorService)
                .thenAccept(s -> System.out.println("Played sounds"));

//        CompletableFuture<WorkflowStep> workflowStepCompletableFuture = CompletableFuture.supplyAsync(() -> new GetCoordinatesForEndpoints(), executorService);
//        CompletableFuture<WorkflowStep> alarmCompletableFuture = CompletableFuture.supplyAsync(() -> new DisplayAlarmDetails(), executorService);
//        CompletableFuture<WorkflowStep> soundCompletableFuture = CompletableFuture.supplyAsync(() -> new PlaySounds(), executorService);
//
//        workflowStepCompletableFuture.thenAcceptAsync((a -> System.err.println("Coordinates received")), executorService);
//        alarmCompletableFuture.thenAcceptAsync((a -> System.err.println("Alarm details set")), executorService);
//        soundCompletableFuture.thenAcceptAsync((a -> System.err.println("Sounds played")), executorService);
//        workflowSteps.add(new GetCoordinatesForEndpoints());
//        workflowSteps.add(new DisplayAlarmDetails());
//        workflowSteps.add(new PlaySounds());
//        workflowSteps.forEach(WorkflowStep::executeStep);
    }
}
