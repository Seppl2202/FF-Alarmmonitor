package de.ff.jf.bftag.alarmmonitor.OpenRouteService.GeoPosition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoPoistionResponse {
    private List<Object> features;

    public GeoPoistionResponse() {
    }

    public List<Object> getFeatures() {
        return features;
    }

    public void setFeatures(List<Object> features) {
        this.features = features;
    }
}
