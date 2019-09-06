package de.ff.jf.bftag.alarmmonitor.OpenRouteService.GeoPosition;

import com.fasterxml.jackson.databind.ObjectMapper;
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

public class GeoPositionRequester {

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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private URL buildURL(String address, String postal, String conutry, String location, StringBuilder builder) throws MalformedURLException {
        builder.append("address=");
        builder.append(address);
        builder.append("&country=Germany");
        builder.append("&postalcode=");
        builder.append(postal);
        builder.append("&locality=");
        builder.append(location);
        return new URL(builder.toString());
    }

    public GeoPosition getCoordinates(URL url) throws IOException {
        GeoPoistionResponse response = new ObjectMapper().readValue(url, GeoPoistionResponse.class);
        Map<String, Map> geo = (LinkedHashMap) response.getFeatures().get(0);
        Map<String, Map> geometry = (LinkedHashMap) geo.get("geometry");
        List<Double> coord = (ArrayList) geometry.get("coordinates");
        return new GeoPosition(coord.get(1), coord.get(0));
    }

}
