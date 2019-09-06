package de.ff.jf.bftag.alarmmonitor.OpenRouteService.GeoPosition;

import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.json.internal.IO;
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

/**
 * This class requests data from the PELIAS dev portal.
 * !ATTENTION!: Use this class only for fallback pruposes, since it requests from a dev portal relying on the Google Maps Geocoding API which is chargeable
 */
public class PeliasDirectRequester implements GeoCoordinatesRequester {
    private final String baseURL = "https://api.geocode.earth/v1/search?api_key=ge-5673e2c135b93a30&text=";


    public URL buildURL(String address, String postal, String location) {
        try {
            String add = URLEncoder.encode(address, "UTF-8");
            String loc = URLEncoder.encode(location, "UTF-8");
            String post = URLEncoder.encode(postal, "UTF-8");
            StringBuilder builder = new StringBuilder(baseURL);
            return buildURL(add, post, "Germany", loc, builder);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public URL buildURL(String address, String postal, String conutry, String location, StringBuilder builder) throws IOException {
        builder.append(address);
        builder.append(",");
        builder.append(postal);
        builder.append(URLEncoder.encode(" ", "UTF-8"));
        builder.append(location);
        return new URL(builder.toString());
    }

    public GeoPosition getCoordinates(URL url) throws IOException {
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
        GeoPoistionResponse response = new ObjectMapper().readValue(url, GeoPoistionResponse.class);
        Map<String, Map> geo = (LinkedHashMap) response.getFeatures().get(0);
        Map<String, Map> geometry = (LinkedHashMap) geo.get("geometry");
        List<Double> coord = (ArrayList) geometry.get("coordinates");
        return new GeoPosition(coord.get(1), coord.get(0));
    }
}
