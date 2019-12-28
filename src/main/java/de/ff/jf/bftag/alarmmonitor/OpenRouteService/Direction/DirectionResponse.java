package de.ff.jf.bftag.alarmmonitor.OpenRouteService.Direction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectionResponse {
    private List<Object> features;

    public DirectionResponse() {
    }

    public List<Object> getFeatures() {
        return features;
    }

    public void setFeatures(List<Object> features) {
        this.features = features;
    }
}
