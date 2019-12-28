package de.ff.jf.bftag.alarmmonitor.OpenRouteService.GeoPosition;

import org.jxmapviewer.viewer.GeoPosition;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public interface GeoCoordinatesRequester {
    public URL buildURL(String address, String postal, String location);

    public GeoPosition getCoordinates(URL url) throws IOException;

    default URL buildURL(String address, String postal, String conutry, String location, StringBuilder builder) throws IOException {
        builder.append("address=");
        builder.append(address);
        builder.append("&country=Germany");
        builder.append("&postalcode=");
        builder.append(postal);
        builder.append("&locality=");
        builder.append(location);
        return new URL(builder.toString());
    }
}
