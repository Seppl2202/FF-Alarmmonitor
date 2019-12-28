package de.ff.jf.bftag.alarmmonitor;


import org.jxmapviewer.viewer.GeoPosition;

public class ApplicationConfiguration {
    private double centerLatitude;
    private double centerLongitude;
    private double fireServiceHomeLatitude;
    private double fireServiceHomeLongitude;

    public ApplicationConfiguration() {
    }

    public double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public double getFireServiceHomeLatitude() {
        return fireServiceHomeLatitude;
    }

    public void setFireServiceHomeLatitude(double fireServiceHomeLatitude) {
        this.fireServiceHomeLatitude = fireServiceHomeLatitude;
    }

    public double getFireServiceHomeLongitude() {
        return fireServiceHomeLongitude;
    }

    public void setFireServiceHomeLongitude(double fireServiceHomeLongitude) {
        this.fireServiceHomeLongitude = fireServiceHomeLongitude;
    }

    public GeoPosition getMapCenter() {
        return new GeoPosition(this.centerLatitude, this.centerLongitude);
    }

    public GeoPosition getFireServiceHome() {
        return new GeoPosition(this.fireServiceHomeLatitude, this.fireServiceHomeLongitude);
    }
}
