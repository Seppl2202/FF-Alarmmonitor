package de.ff.jf.bftag.alarmmonitor.OpenRouteService.Direction;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.ff.jf.bftag.alarmmonitor.jsontojava.Engine;
import org.jxmapviewer.viewer.GeoPosition;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DirectionWaypointRequester {
    private GeoPosition start, end;
    private String baseURL = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=";
    private final String apiKey = "5b3ce3597851110001cf62488a1c1746e34a467e9fd25f4e893096c2";

    public DirectionWaypointRequester(GeoPosition start, GeoPosition end) {
        this.start = start;
        this.end = end;
        flipCoordinates();
    }

    /**
     * The API requests the longitude to be first, so the coordinates have to be flipped
     */
    private void flipCoordinates() {
        this.start = new GeoPosition(start.getLongitude(), start.getLatitude());
        this.end = new GeoPosition(end.getLongitude(), end.getLatitude());
    }

    public URL buildURL() {
        try {
            String startLon = URLEncoder.encode(String.valueOf(start.getLatitude()), "UTF-8");
            String startLat = URLEncoder.encode(String.valueOf(start.getLongitude()), "UTF-8");
            String endLon = URLEncoder.encode(String.valueOf(end.getLatitude()), "UTF-8");
            String endLat = URLEncoder.encode(String.valueOf(end.getLongitude()), "UTF-8");
            String api = URLEncoder.encode(apiKey, "UTF-8");
            StringBuilder builder = new StringBuilder(baseURL);
            builder.append(api);
            builder.append("&");
            return buildURL(startLon, startLat, endLon, endLat, builder);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private URL buildURL(String startLon, String startLat, String endLon, String endLat, StringBuilder builder) throws MalformedURLException {
        builder.append("start=");
        builder.append(startLon + "," + startLat);
        builder.append("&end=");
        builder.append(endLon + "," + endLat);
        URL url = new URL(builder.toString());
        return url;
    }

    /**
     * @param url
     * @return a List of waypoint markers! The last waypoint represents the distance and the duration!
     * @throws IOException
     */
    public List<GeoPosition> getWaypoints(URL url) throws IOException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

// Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
        Engine response = new ObjectMapper().readValue(url, Engine.class);
//        Map<String, LinkedHashMap> features = (LinkedHashMap) response.getFeatures().get(0);
//        Map<String, LinkedHashMap> geometry = features.get("geometry");
//        List<List<Double>> coordinates = (List) geometry.get("coordinates");
        LinkedHashMap<String, Object> features = (LinkedHashMap<String, Object>) ((ArrayList) response.getAdditionalProperties().get("features")).get(0);
        LinkedHashMap<String, Object> featuresSubMap = (LinkedHashMap) features.get("geometry");
        List<List<Double>> coordinates = (List<List<Double>>) featuresSubMap.get("coordinates");
        LinkedHashMap<String, Object> distDurSubMap = (LinkedHashMap) features.get("properties");
        LinkedHashMap<String, Object> featuresSubMap2 = (LinkedHashMap) distDurSubMap.get("summary");
        Double distanceMeters = (Double) featuresSubMap2.get("distance");
        Double durationSeconds = (Double) featuresSubMap2.get("duration");
        return createWaypointObjects(coordinates, distanceMeters, durationSeconds);
    }

    private List<GeoPosition> createWaypointObjects(List<List<Double>> coordinates, Double dist, Double dur) {
        List<GeoPosition> positions = coordinates.stream().map(e -> new GeoPosition(e.get(1), e.get(0))).collect(Collectors.toList());
        positions.add(new GeoPosition(dist, dur));
        return positions;
    }
}
