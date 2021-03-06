package de.ff.jf.bftag.alarmmonitor.OpenRouteService.Direction;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.ff.jf.bftag.alarmmonitor.RessourceFolderURL;
import de.ff.jf.bftag.alarmmonitor.jsontojava.Engine;
import de.ff.jf.bftag.alarmmonitor.workflow.ExtractedInformationPOJO;
import de.ff.jf.bftag.alarmmonitor.workflow.FireServiceDispatchWorkflow;
import de.ff.jf.bftag.alarmmonitor.workflow.InstructionsImageList;
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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DirectionWaypointRequester {
    public static final String TURN_LEFT = RessourceFolderURL.ressourceFolderBaseURL + "/leftturn.png";
    public static final String TURN_RIGHT = RessourceFolderURL.ressourceFolderBaseURL + "/rightturn.png";
    public static final String FINISH = RessourceFolderURL.ressourceFolderBaseURL + "/finish.png";
    public static final String ROUNDABOUT_RIGHT = RessourceFolderURL.ressourceFolderBaseURL + "/roundright.png";
    public static final String ROUNDABOUT_STRAIGHT = RessourceFolderURL.ressourceFolderBaseURL + "/roundstraight.png";
    public static final String ROUNDABOUT_LEFT = RessourceFolderURL.ressourceFolderBaseURL + "/roundleft.png";
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
    public ExtractedInformationPOJO getWaypoints(URL url) throws IOException {
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

        LinkedHashMap<String, Object> features = (LinkedHashMap<String, Object>) ((ArrayList) response.getAdditionalProperties().get("features")).get(0);
        LinkedHashMap<String, Object> featuresSubMap = (LinkedHashMap) features.get("geometry");
        List<List<Double>> coordinates = (List<List<Double>>) featuresSubMap.get("coordinates");
        LinkedHashMap<String, Object> distDurSubMap = (LinkedHashMap) features.get("properties");
        LinkedHashMap<String, Object> featuresSubMap2 = (LinkedHashMap) distDurSubMap.get("summary");
        Double distanceMeters = (Double) featuresSubMap2.get("distance");
        Double durationSeconds = (Double) featuresSubMap2.get("duration");
        List<GeoPosition> waypoints = createWaypointObjects(coordinates, distanceMeters, durationSeconds);
        InstructionsImageList instructionsImageList = extractRoutingInstructionsFromResponse(response);
        return new ExtractedInformationPOJO(waypoints, instructionsImageList);
    }

    private List<GeoPosition> createWaypointObjects(List<List<Double>> coordinates, Double dist, Double dur) {
        List<GeoPosition> positions = coordinates.stream().map(e -> new GeoPosition(e.get(1), e.get(0))).collect(Collectors.toList());
        positions.add(new GeoPosition(dist, dur));
        return positions;
    }

    private InstructionsImageList extractRoutingInstructionsFromResponse(Engine engine) {
        LinkedHashMap<String, Object> features = (LinkedHashMap<String, Object>) ((ArrayList) engine.getAdditionalProperties().get("features")).get(0);
        LinkedHashMap<String, Object> properties = (LinkedHashMap) features.get("properties");
        ArrayList<Object> segments = (ArrayList<Object>) properties.get("segments");
        LinkedHashMap<String, Object> temp = (LinkedHashMap<String, Object>) segments.get(0);
        ArrayList<LinkedHashMap<String, Object>> instructions = (ArrayList<LinkedHashMap<String, Object>>) temp.get("steps");
        List<String> extractedInstructions = new LinkedList<>();
        instructions.forEach(listEntry -> {
            extractedInstructions.add((String) listEntry.get("instruction"));
        });
        return matchInstructionToImage(extractedInstructions);
    }

    private String getStreetInInstruction(String completeInstruction) {
        String[] splitArray = completeInstruction.split("(?<=onto) ");
        return splitArray.length == 2 ? splitArray[1] : "";
    }

    private InstructionsImageList matchInstructionToImage(List<String> instructions) {
        InstructionsImageList instructionsImageList = new InstructionsImageList();
        String instruction0 = instructions.get(0);
        if (instruction0.contains("Head north")) {
            instructionsImageList.addImage("Keitländerstraße", TURN_LEFT);
        } else {
            instructionsImageList.addImage("Keitländerstraße", TURN_RIGHT);
        }

        String instruction1 = instructions.get(1);

        if (instruction1.contains("Turn left")) {
            instructionsImageList.addImage(getStreetInInstruction(instruction1), TURN_LEFT);
        } else if (instruction1.contains("Turn right")) {
            instructionsImageList.addImage(getStreetInInstruction(instruction1), TURN_RIGHT);
        }
        if (instructions.size() == 3) {
            String finalInstruction = instructions.get(2);
            if (finalInstruction.contains("Arrive at")) {
                instructionsImageList.addImage(FireServiceDispatchWorkflow.currentAlarm.getAddress().getStreet() + " " + FireServiceDispatchWorkflow.currentAlarm.getAddress().getNumber(), FINISH);
            }
        }

        if (instructions.size() > 3) {
            String instruction3 = instructions.get(2);
            if (instruction3.contains("Enter the roundabout")) {
                if (instruction3.contains("1st exit")) {
                    instructionsImageList.addImage(getStreetInInstruction(instruction3), ROUNDABOUT_RIGHT);
                } else if (instruction3.contains("2nd exit")) {
                    instructionsImageList.addImage(getStreetInInstruction(instruction3), ROUNDABOUT_STRAIGHT);
                } else if (instruction3.contains("3rd exit")) {
                    instructionsImageList.addImage(getStreetInInstruction(instruction3), ROUNDABOUT_LEFT);
                }
            } else if (instruction3.contains("Turn left")) {
                instructionsImageList.addImage(getStreetInInstruction(instruction3), TURN_LEFT);
            } else if (instruction3.contains("Turn right")) {
                instructionsImageList.addImage(getStreetInInstruction(instruction3), TURN_RIGHT);
            }
        }
        return instructionsImageList;
    }
}
