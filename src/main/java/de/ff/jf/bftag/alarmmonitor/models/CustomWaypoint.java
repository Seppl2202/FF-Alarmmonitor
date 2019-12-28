package de.ff.jf.bftag.alarmmonitor.models;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;

public class CustomWaypoint extends DefaultWaypoint {
    private final String label;
    private final Color color;

    public CustomWaypoint(String label, Color color, GeoPosition geoPosition) {
        super(geoPosition);
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        CustomWaypoint w = (CustomWaypoint) obj;
        return getPosition().equals(w.getPosition());
    }


    @Override
    public String toString() {
        return this.label + ", " + super.toString();
    }

}
