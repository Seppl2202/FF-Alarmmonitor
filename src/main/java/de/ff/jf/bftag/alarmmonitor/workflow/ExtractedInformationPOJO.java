package de.ff.jf.bftag.alarmmonitor.workflow;

import de.ff.jf.bftag.alarmmonitor.OpenRouteService.GeoPosition.GeoPositionRequester;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.List;

public class ExtractedInformationPOJO {
    private List<GeoPosition> waypointTracks;
    private InstructionsImageList instructionsImageList;

    public ExtractedInformationPOJO(List<GeoPosition> waypointTracks, InstructionsImageList instructionsImageList) {
        this.waypointTracks = waypointTracks;
        this.instructionsImageList = instructionsImageList;
    }

    public List<GeoPosition> getWaypointTracks() {
        return waypointTracks;
    }

    public InstructionsImageList getInstructionsImageList() {
        return instructionsImageList;
    }
}
