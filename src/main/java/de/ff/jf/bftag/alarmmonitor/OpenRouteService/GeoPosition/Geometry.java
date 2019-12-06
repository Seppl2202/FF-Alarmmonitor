package de.ff.jf.bftag.alarmmonitor.OpenRouteService.GeoPosition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {
    private List<String> coordinates;

    public Geometry() {
    }

    public List<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<String> coordinates) {
        this.coordinates = coordinates;
    }
}
