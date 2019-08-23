package de.ff.jf.bftag.alarmmonitor.workflow;

import de.ff.jf.bftag.alarmmonitor.Alarm;
import de.ff.jf.bftag.alarmmonitor.Main;
import de.ff.jf.bftag.alarmmonitor.Monitor;
import de.ff.jf.bftag.alarmmonitor.OpenRouteService.Direction.DirectionWaypointRequester;
import de.ff.jf.bftag.alarmmonitor.OpenRouteService.GeoPosition.GeoPositionRequester;
import marytts.MaryInterface;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GetCoordinatesForEndpoints implements WorkflowStep {
    @Override
    public void executeStep() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Alarm alarm = FireServiceDispatchWorkflow.currentAlarm;
                Monitor m = Main.getMonitor();
                List<GeoPosition> waypointTracks = null;
                GeoPosition start = null;
                GeoPosition end = null;
                try {
                    GeoPositionRequester requester = new GeoPositionRequester();
                    if (alarm.getAddress().getNumber() == null || alarm.getAddress().getNumber().isEmpty()) {
                        end = requester.getCoordinates(requester.buildURL(alarm.getAddress().getStreet() , String.valueOf(alarm.getAddress().getZipCode()), alarm.getAddress().getLocation()));
                    } else {
                        end = requester.getCoordinates(requester.buildURL(alarm.getAddress().getStreet() + " " + alarm.getAddress().getNumber(), String.valueOf(alarm.getAddress().getZipCode()), alarm.getAddress().getLocation()));
                    }
                    //coordinates are fixed, because Feuerwehr Hambrücken has no reliable house number
                    start = new GeoPosition(49.186792, 8.549914);
                    DirectionWaypointRequester waypointRequester = new DirectionWaypointRequester(start, end);
                    URL waypointsURL = waypointRequester.buildURL();
                    waypointTracks = waypointRequester.getWaypoints(waypointsURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                m.setAlarmMArkers(waypointTracks, start, end);
            }
        }).start();

    }
}
