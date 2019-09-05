package de.ff.jf.bftag.alarmmonitor.server;

import de.ff.jf.bftag.alarmmonitor.Alarm;
import de.ff.jf.bftag.alarmmonitor.Main;
import de.ff.jf.bftag.alarmmonitor.Monitor;
import de.ff.jf.bftag.alarmmonitor.workflow.AlarmDispatchWorkflow;
import de.ff.jf.bftag.alarmmonitor.workflow.FireServiceDispatchWorkflow;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
public class DispatchController {
    private boolean hasAlarm = false;

    @RequestMapping(value = "/alarm/dispatch", method = RequestMethod.POST)
    public ResponseEntity<String> processDispatch(@RequestBody Alarm alarm) throws IOException, InterruptedException {
        Objects.requireNonNull(alarm, "Alarm cannot be empty");
        if (alarm.getAlarmedCars().size() == 0) {
            return new ResponseEntity<>("You must at least select one car", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        if (hasAlarm) {
            return new ResponseEntity<>("An alarm is already in progress", HttpStatus.CONFLICT);
        }
        hasAlarm = true;
        System.err.println("Got alarm: " + alarm.getAddress().getStreet() + " " + alarm.getKeyword().getStage());
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> dispatchAlarm(alarm));
        completableFuture.thenAccept(s -> System.out.println("Alarm was displayed, waiting for flasher to stop..."));
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Monitor m = Main.getMonitor();
        executorService.schedule(() -> {
            m.stopFlasher();
            hasAlarm = false;
        }, 2, TimeUnit.MINUTES);
        return new ResponseEntity<>("Alarm dispatched successfully", HttpStatus.OK);

    }

    private String dispatchAlarm(Alarm alarm) {
        Monitor m = Main.getMonitor();
        AlarmDispatchWorkflow workflow = new FireServiceDispatchWorkflow();
        workflow.start(alarm);
        return "Alarm dispatched and displayed";
    }
}
