package de.ff.jf.bftag.alarmmonitor.gui;

import de.ff.jf.bftag.alarmmonitor.models.Alarm;
import de.ff.jf.bftag.alarmmonitor.models.Car;
import de.ff.jf.bftag.alarmmonitor.models.CustomWaypoint;
import de.ff.jf.bftag.alarmmonitor.models.ZipCodeToTownName;
import de.ff.jf.bftag.alarmmonitor.workflow.ExtractedInformationPOJO;
import de.ff.jf.bftag.alarmmonitor.workflow.TextToSpeech;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Monitor extends JFrame {
    private final int blinkingRate = 1000;
    JPanel detailPanel;
    JPanel addressPanel;
    int callCount = 0;
    WaypointPainter<CustomWaypoint> waypointPainter;
    List<org.jxmapviewer.painter.Painter<JXMapViewer>> painters;
    private boolean blinkingOn = true;
    private List<JLabel> carLabels;
    private JLabel address, detail, time, image;
    private JXMapViewer mapViewer;
    private Timer flasher;
    private FlashListener listener;
    private TextToSpeech textToSpeechEngine;
    private JPanel alarmMonitorPanel;
    private JPanel normalPanel;
    private JPanel fullPanel, timePanel;
    private JLabel driveTime, timeIcon, distanceIcon, distance;
    private List<JLabel> instructionImages;
    private CardLayout cardLayout;
    private CompoundPainter<JXMapViewer> painter;
    private Set<CustomWaypoint> waypoints;
    private List<GeoPosition> routeTrack;
    private FMSListModel fmsListModel;
    private ConcurrentMap<String, CustomWaypoint> carPositions = new ConcurrentHashMap<>();


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
        List<Car> carFMS = new ArrayList<>();
        fmsListModel = new FMSListModel();
        fmsListModel.addElement(new Car("MTW", 2));
        fmsListModel.addElement(new Car("LF16-12", 2));
        fmsListModel.addElement(new Car("RW", 2));
        fmsListModel.addElement(new Car("LF16-TS", 2));
        JList fmsList = new JList(fmsListModel);
        fmsList.setCellRenderer(new FMSListCellRenderer());
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> fmsList.ensureIndexIsVisible(fmsListModel.getSize()), 0, 2, TimeUnit.SECONDS);
        fmsList.setFont(new Font("Arial", Font.ITALIC, 30));
        JPanel mapViewerPanel = new JPanel(new BorderLayout());
        mapViewerPanel.add(mapViewer, BorderLayout.CENTER);
        mapViewerPanel.add(fmsList, BorderLayout.WEST);
        tempPanel.add(mapViewerPanel, BorderLayout.NORTH);
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
        timePanel.add(distanceIcon);

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
        cardLayout.show(fullPanel, "ALARM");
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
        Set<CustomWaypoint> waypoints = new HashSet<CustomWaypoint>(Arrays.asList());
        // Create a waypoint painter that takes all the waypoints
        waypointPainter = new WaypointPainter<CustomWaypoint>();
        waypointPainter.setWaypoints(waypoints);

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<org.jxmapviewer.painter.Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(routePainter);
        painters.add(waypointPainter);

        painter = new CompoundPainter<JXMapViewer>(painters);
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
        routeTrack = extractedInformationPOJO.getWaypointTracks();

        timePanel.removeAll();
        timePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        timePanel.add(timeIcon);
        timePanel.add(distanceIcon);
        int min = (int) ((distDur.getLongitude() * 0.75) % 3600) / 60;
        int sec = (int) ((distDur.getLongitude() * 0.75) % 60);
        String secWithLeadingZero;
        if (sec < 10) {
            secWithLeadingZero = "0" + sec;
        } else {
            secWithLeadingZero = Integer.toString(sec);
        }
        String timeDistDetails = min + ":" + secWithLeadingZero;
        timeIcon.setText(timeDistDetails);
        String distanceString;

        double d = distDur.getLatitude();
        if (d < 1000) {
            distanceString = String.format("%.2f", d) + " Meter";
        } else {
            distanceString = String.format("%.2f", d / 1000) + " Kilometer";
        }
        distanceIcon.setText(distanceString);
        instructionImages = new LinkedList<>();
        extractedInformationPOJO.getInstructionsImageList().getImages().forEach((key, value) -> {
            JLabel l = new JLabel(key);
            l.setIcon(new ImageIcon(value));
            l.setFont(new Font("Arial", Font.BOLD, 40));
            instructionImages.add(l);
        });
        instructionImages.forEach(timePanel::add);
        RoutePainter routePainter = new RoutePainter(extractedInformationPOJO.getWaypointTracks());
        waypoints = new HashSet<>(Arrays.asList(
                new CustomWaypoint("Feuerwehrhaus", Color.RED, start),
                new CustomWaypoint("Ziel", Color.GREEN.GREEN, end)));
        // Create a waypoint painter that takes all the waypoints
        waypointPainter = new WaypointPainter<>();
        waypointPainter.setRenderer(new CustomWaypointRenderer());
        waypointPainter.setWaypoints(waypoints);
        mapViewer.zoomToBestFit(new HashSet<>(extractedInformationPOJO.getWaypointTracks()), 0.6);

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        painters = new ArrayList<>();
        painters.add(routePainter);
        painters.add(waypointPainter);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    public void addWaypoints(CustomWaypoint customWaypointList) {
        if (carPositions.get(customWaypointList.getLabel()) == null) {
            carPositions.put(customWaypointList.getLabel(), customWaypointList);
            routeTrack.add(customWaypointList.getPosition());
            waypoints.add(customWaypointList);
            waypointPainter.setWaypoints(waypoints);
        } else {
            waypoints.remove(carPositions.get(customWaypointList.getLabel()));
            waypoints.add(customWaypointList);
            carPositions.put(customWaypointList.getLabel(), customWaypointList);
            routeTrack.add(customWaypointList.getPosition());
            painters.remove(waypointPainter);
            waypointPainter = new WaypointPainter<>();
            waypointPainter.setRenderer(new CustomWaypointRenderer());
            waypointPainter.setWaypoints(waypoints);
            painters.add(waypointPainter);
            CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(painters);
            mapViewer.setOverlayPainter(compoundPainter);
        }

        mapViewer.zoomToBestFit(new HashSet<>(routeTrack), 0.7);
    }

    public int updateFMS(String carName, int newState) {
        return fmsListModel.updateFMSState(carName, newState);
    }


    private String getImageString() {
        return "fire.jpg";
    }

}
