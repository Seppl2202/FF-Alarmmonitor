package de.ff.jf.bftag.alarmmonitor.workflow;

import de.ff.jf.bftag.alarmmonitor.OpenRouteService.GeoPosition.GeoCoordinatesRequester;
import de.ff.jf.bftag.alarmmonitor.OpenRouteService.GeoPosition.PeliasDirectRequester;
import de.ff.jf.bftag.alarmmonitor.models.Alarm;
import de.ff.jf.bftag.alarmmonitor.Main;
import de.ff.jf.bftag.alarmmonitor.gui.Monitor;
import de.ff.jf.bftag.alarmmonitor.OpenRouteService.Direction.DirectionWaypointRequester;
import de.ff.jf.bftag.alarmmonitor.OpenRouteService.GeoPosition.GeoPositionRequester;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GetCoordinatesForEndpoints implements WorkflowStep {
    @Override
    public void executeStep() {
        Alarm alarm = FireServiceDispatchWorkflow.currentAlarm;
        Monitor m = Main.getMonitor();
        ExtractedInformationPOJO extractedInformationPOJO = null;
        GeoPosition start = null;
        GeoPosition end = null;
        try {
            GeoCoordinatesRequester requester = new GeoPositionRequester();
            if (alarm.getAddress().getNumber() == null || alarm.getAddress().getNumber().isEmpty()) {
                end = requester.getCoordinates(requester.buildURL(alarm.getAddress().getStreet(), String.valueOf(alarm.getAddress().getZipCode()), alarm.getAddress().getLocation()));
            } else {
                end = requester.getCoordinates(requester.buildURL(alarm.getAddress().getStreet() + " " + alarm.getAddress().getNumber(), String.valueOf(alarm.getAddress().getZipCode()), alarm.getAddress().getLocation()));
            }

            if ((end.getLatitude() == 49.19) && (end.getLongitude() == 8.54056)) {
                System.err.println("Probably received a fallback response from OpenrouteService, cross-checking Pelias dev portal...");
                requester = new PeliasDirectRequester();
                URL u = requester.buildURL((alarm.getAddress().getStreet() + " " + alarm.getAddress().getNumber()), Integer.toString(alarm.getAddress().getZipCode()), alarm.getAddress().getLocation());
                end = requester.getCoordinates(u);
            }
            //coordinates are fixed, because Feuerwehr Hambrücken has no reliable house number
            start = new GeoPosition(49.186792, 8.549914);
            DirectionWaypointRequester waypointRequester = new DirectionWaypointRequester(start, end);
            URL waypointsURL = waypointRequester.buildURL();
            extractedInformationPOJO = waypointRequester.getWaypoints(waypointsURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        m.setAlarmMArkers(extractedInformationPOJO, start, end);
    }
}
