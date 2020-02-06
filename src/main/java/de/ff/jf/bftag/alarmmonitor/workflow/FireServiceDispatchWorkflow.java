package de.ff.jf.bftag.alarmmonitor.workflow;

import de.ff.jf.bftag.alarmmonitor.models.Alarm;
import de.ff.jf.bftag.alarmmonitor.pdf.FireServicePdfExporter;
import de.ff.jf.bftag.alarmmonitor.pdf.PdfExporter;

import java.io.IOException;
import java.nio.file.Paths;
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
        PdfExporter exporter = new FireServicePdfExporter(Paths.get("/"));
        try {
            exporter.generatePdf(alarm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
