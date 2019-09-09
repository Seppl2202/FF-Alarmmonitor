package de.ff.jf.bftag.alarmmonitor.gui;

import de.ff.jf.bftag.alarmmonitor.OpenRouteService.GeoPosition.GeoPositionRequester;
import de.ff.jf.bftag.alarmmonitor.models.Alarm;
import de.ff.jf.bftag.alarmmonitor.workflow.ExtractedInformationPOJO;
import de.ff.jf.bftag.alarmmonitor.workflow.TextToSpeech;
import de.ff.jf.bftag.alarmmonitor.models.ZipCodeToTownName;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Monitor extends JFrame {
    private final int blinkingRate = 1000;
    private boolean blinkingOn = true;
    private List<JLabel> carLabels;
    private JLabel address, detail, time, image;
    private JXMapViewer mapViewer;
    private Timer flasher;
    private FlashListener listener;
    JPanel detailPanel;
    JPanel addressPanel;
    private TextToSpeech textToSpeechEngine;
    private JPanel alarmMonitorPanel;
    private JPanel normalPanel;
    private JPanel fullPanel, timePanel;
    private JLabel driveTime, timeIcon, distanceIcon, distance;
    private List<JLabel> instructionImages;
    private CardLayout cardLayout;
    int callCount = 0;


    public Monitor() throws InterruptedException {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    initialize();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initialize() throws InterruptedException, IOException {
        textToSpeechEngine = new TextToSpeech();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        alarmMonitorPanel = new JPanel();
        cardLayout = new CardLayout();
        fullPanel = new JPanel(cardLayout);
        this.add(fullPanel);
        alarmMonitorPanel.setLayout(new BorderLayout());
        JPanel carPanel = new JPanel();
        addressPanel = new JPanel();
        detailPanel = new JPanel();
        image = new JLabel();
        detailPanel.add(image);
        JPanel combinedAdressDetailPanel = new JPanel();
        combinedAdressDetailPanel.setLayout(new FlowLayout());
        combinedAdressDetailPanel.add(detailPanel);
        combinedAdressDetailPanel.add(addressPanel);
        address = new JLabel("Adresse");
        detail = new JLabel("Kategorie: Stichwort");
        detail.setVisible(false);
        address.setVisible(false);
        address.setFont(new Font("Arial", Font.PLAIN, 25));
        detail.setFont(new Font("Arial", Font.PLAIN, 25));
        detailPanel.add(detail);
        addressPanel.add(address);
        detailPanel.setVisible(false);
        addressPanel.setVisible(false);
        address.setOpaque(false);
        detail.setOpaque(false);
        combinedAdressDetailPanel.add(detailPanel);
        combinedAdressDetailPanel.add(addressPanel);
        combinedAdressDetailPanel.setBackground(new Color(255, 255, 255));

        alarmMonitorPanel.add(combinedAdressDetailPanel, BorderLayout.NORTH);

        JPanel mapPanel = new JPanel();
        mapViewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, true));

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);


        // Set the focus
        GeoPosition hambrücken1 = new GeoPosition(49.188201, 8.549774);


        mapViewer.setZoom(4);
        mapViewer.setAddressLocation(hambrücken1);
        mapViewer.setPreferredSize(new Dimension(500, 750));
        JPanel tempPanel = new JPanel(new BorderLayout());
        tempPanel.add(mapViewer, BorderLayout.NORTH);
//        driveTime = new JLabel("Geschätzte Fahrtzeit: ", SwingConstants.CENTER);
//        driveTime.setFont(new Font("Arial", Font.BOLD, 35));
//        tempPanel.add(driveTime, BorderLayout.CENTER);
        timePanel = new JPanel();
        timePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        timeIcon = new JLabel("Fahrtzeit");
        distanceIcon = new JLabel("Distanz");
        BufferedImage timeIc = ImageIO.read(new File("C:\\Users\\SchweglerS\\IdeaProjects\\Alarmmonitor\\src\\main\\resources\\images\\clock.jpg"));
        timeIcon.setIcon(new ImageIcon(timeIc.getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        BufferedImage distanceIco = ImageIO.read(new File("C:\\Users\\SchweglerS\\IdeaProjects\\Alarmmonitor\\src\\main\\resources\\images\\distance.png"));
        distanceIcon.setIcon(new ImageIcon(distanceIco.getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        distance = new JLabel("Distanz:");
        distance.setFont(new Font("Arial", Font.BOLD, 35));
        time = new JLabel("Fahrtzeit:");
        time.setFont(new Font("Arial", Font.BOLD, 35));
        instructionImages = new LinkedList<>();
        tempPanel.add(timePanel, BorderLayout.CENTER);
        timePanel.add(timeIcon);
        timeIcon.setFont(new Font("Arial", Font.BOLD, 40));
        distanceIcon.setFont(new Font("Arial", Font.BOLD, 40));
//        timePanel.add(time);
        timePanel.add(distanceIcon);
//        timePanel.add(distance);

        alarmMonitorPanel.add(tempPanel, BorderLayout.SOUTH);
        List<String> cars = new ArrayList<>();
        cars.add("MTW");
        cars.add("LF16-12");
        cars.add("RW");
        cars.add("LF16-TS");
        carLabels = new ArrayList<>();
        carPanel.setLayout(new GridLayout(1, cars.size(), 25, 0));
        cars.forEach(s -> {
            JLabel l = new JLabel(s);
            l.setVerticalAlignment(SwingConstants.CENTER);
            l.setHorizontalAlignment(SwingConstants.CENTER);
            carPanel.add(l);
            l.setBackground(new Color(255, 0, 0));
            l.setOpaque(true);
            l.setFont(new Font("Arial", Font.PLAIN, 25));
            carLabels.add(l);
        });
        carPanel.setPreferredSize(new Dimension(500, 250));
        alarmMonitorPanel.add(carPanel, BorderLayout.CENTER);
        this.setVisible(true);
        fullPanel.add(alarmMonitorPanel, "ALARM");
        normalPanel = new NormalBackGroundPanel();
        fullPanel.add(normalPanel, "NORMAL");
        cardLayout.show(fullPanel, "NORMAL");
        this.pack();
    }


    public synchronized void setAlarmDetails(Alarm alarm) {
        System.err.println("Called setdetails");
        cardLayout.show(fullPanel, "ALARM");
        System.err.println("Called PANEL");
        String imageName = getImageString();
        try {
            BufferedImage imageIcon = ImageIO.read(new File("C:\\Users\\SchweglerS\\IdeaProjects\\Alarmmonitor\\src\\main\\resources\\images\\fire.png"));
            image.setIcon(new ImageIcon(imageIcon.getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
            image.setPreferredSize(new Dimension(25, 25));
        } catch (IOException e) {
            e.printStackTrace();
        }
        detail.setVisible(true);
        address.setVisible(true);
        address.setOpaque(true);
        detail.setOpaque(true);
        detailPanel.setVisible(true);
        addressPanel.setVisible(true);

        address.setText(ZipCodeToTownName.zipToName(alarm.getAddress().getZipCode()) + ", " + alarm.getAddress().getStreet() + " " + alarm.getAddress().getNumber());
        detail.setText(alarm.getKeyword().getStage() + ": " + alarm.getKeyword().getKeyword());
        List<JLabel> list = carLabels.stream().filter(label -> alarm.getAlarmedCars().contains(label.getText())).collect(Collectors.toList());
        listener = new FlashListener(list, new Color(255, 0, 0), new Color(0, 255, 0));
        flasher = new Timer(1000, listener);
        flasher.setInitialDelay(0);
        flasher.start();
    }

    public void stopFlasher() {
        cardLayout.show(fullPanel, "NORMAL");
        detail.setVisible(false);
        address.setVisible(false);
        address.setOpaque(false);
        detail.setOpaque(false);
        image.setVisible(false);
        addressPanel.setVisible(false);
        addressPanel.setOpaque(false);
        detailPanel.setVisible(false);
        detailPanel.setOpaque(false);
        flasher.stop();
        carLabels.forEach(l -> l.setBackground(new Color(255, 0, 0)));
        address.setText("Adresse");
        detail.setText("Kategorie: Stichwort");
        RoutePainter routePainter = new RoutePainter(new ArrayList<GeoPosition>());
        Set<Waypoint> waypoints = new HashSet<Waypoint>(Arrays.asList());
        // Create a waypoint painter that takes all the waypoints
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        waypointPainter.setWaypoints(waypoints);

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<org.jxmapviewer.painter.Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(routePainter);
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
        GeoPosition hambrücken1 = new GeoPosition(49.188201, 8.549774);
        mapViewer.setZoom(4);
        mapViewer.setAddressLocation(hambrücken1);
    }

    private void startTimer() {

    }

    public void setAlarmMArkers(ExtractedInformationPOJO extractedInformationPOJO, GeoPosition start, GeoPosition end) {
        //get the distance and duration dummy GeoPosition and remove it
        GeoPosition distDur = extractedInformationPOJO.getWaypointTracks().get(extractedInformationPOJO.getWaypointTracks().size() - 1);
        extractedInformationPOJO.getWaypointTracks().remove(extractedInformationPOJO.getWaypointTracks().size() - 1);

        timePanel.removeAll();
        timePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        timePanel.add(timeIcon);
        timePanel.add(distanceIcon);
        int min = (int) ((distDur.getLongitude() * 0.75) % 3600) / 60;
        System.err.println("Calc min");
        int sec = (int) ((distDur.getLongitude() * 0.75) % 60);
        String secWithLeadingZero;
        if (sec < 10) {
            secWithLeadingZero = "0" + sec;
        } else {
            secWithLeadingZero = Integer.toString(sec);
        }
        String timeDistDetails = min + ":" + secWithLeadingZero;
        timeIcon.setText(timeDistDetails);
        distanceIcon.setText(distDur.getLatitude() + " Meter");
        instructionImages = new LinkedList<>();
        extractedInformationPOJO.getInstructionsImageList().getImages().forEach((key, value) -> {
            JLabel l = new JLabel(key);
            l.setIcon(new ImageIcon(value));
            l.setFont(new Font("Arial", Font.BOLD, 40));
            instructionImages.add(l);
        });
        instructionImages.forEach(timePanel::add);
        RoutePainter routePainter = new RoutePainter(extractedInformationPOJO.getWaypointTracks());
        Set<Waypoint> waypoints = new HashSet<Waypoint>(Arrays.asList(
                new DefaultWaypoint(start),
                new DefaultWaypoint(end)));
        // Create a waypoint painter that takes all the waypoints
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        waypointPainter.setWaypoints(waypoints);
        mapViewer.zoomToBestFit(new HashSet<>(extractedInformationPOJO.getWaypointTracks()), 0.6);

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<org.jxmapviewer.painter.Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(routePainter);
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    private String getImageString() {
        return "fire.jpg";
    }

}
