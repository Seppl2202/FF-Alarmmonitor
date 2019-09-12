package de.ff.jf.bftag.alarmmonitor.workflow;

import de.ff.jf.bftag.alarmmonitor.Main;
import de.ff.jf.bftag.alarmmonitor.models.Alarm;
import de.ff.jf.bftag.alarmmonitor.models.CustomWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
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
        System.err.println("Adding car trace");
//        add();

        logger.log(Level.INFO, "Displayed alarm details on monitor");
    }

    private void add() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Main.getMonitor().addWaypoints((new CustomWaypoint("LF16-12", Color.BLUE, new GeoPosition(49.186099, 8.550212))));
                    Thread.sleep(5000);
                    Main.getMonitor().addWaypoints((new CustomWaypoint("RW", Color.BLUE, new GeoPosition(49.186099, 8.550212))));
                    Thread.sleep(5000);
                    Main.getMonitor().addWaypoints((new CustomWaypoint("LF16-12", Color.BLUE, new GeoPosition(49.186015, 8.545148))));
                    Thread.sleep(5000);
                    Main.getMonitor().addWaypoints((new CustomWaypoint("RW", Color.BLUE, new GeoPosition(49.186015, 8.545148))));
                    Thread.sleep(5000);
                    Main.getMonitor().addWaypoints((new CustomWaypoint("LF16-12", Color.BLUE, new GeoPosition(49.185798, 8.541414))));
                    Thread.sleep(5000);
                    Main.getMonitor().addWaypoints((new CustomWaypoint("RW", Color.BLUE, new GeoPosition(49.185798, 8.541414))));
                    Thread.sleep(5000);
                    Main.getMonitor().addWaypoints((new CustomWaypoint("LF16-12", Color.BLUE, new GeoPosition(49.187614, 8.541425))));
                    Thread.sleep(5000);
                    Main.getMonitor().addWaypoints((new CustomWaypoint("RW", Color.BLUE, new GeoPosition(49.187614, 8.541425))));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
