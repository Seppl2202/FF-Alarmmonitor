package de.ff.jf.bftag.alarmmonitor.OpenRouteService.GeoPosition;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jxmapviewer.viewer.GeoPosition;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GeoPositionRequester implements GeoCoordinatesRequester {

    private String baseURL = "https://api.openrouteservice.org/geocode/search/structured?api_key=";
    private final String apiKey = "5b3ce3597851110001cf62488a1c1746e34a467e9fd25f4e893096c2";

    public GeoPositionRequester() {

    }

    public URL buildURL(String address, String postal, String location) {
        try {
            String add = URLEncoder.encode(address, "UTF-8");
            String loc = URLEncoder.encode(location, "UTF-8");
            String post = URLEncoder.encode(postal, "UTF-8");
            String api = URLEncoder.encode(apiKey, "UTF-8");
            StringBuilder builder = new StringBuilder(baseURL);
            builder.append(api);
            builder.append("&");
            return buildURL(add, post, "Germany", loc, builder);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
