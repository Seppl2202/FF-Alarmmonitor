package de.ff.jf.bftag.alarmmonitor;

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
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
    private JPanel fullPanel;
    private CardLayout cardLayout;
    private Image scaled, i = null;

    public Monitor() throws InterruptedException {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    initialize();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initialize() throws InterruptedException {
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
        time = new JLabel("Aktuelle Zeit");
        address.setFont(new Font("Arial", Font.PLAIN, 25));
        detail.setFont(new Font("Arial", Font.PLAIN, 25));
        time.setFont(new Font("Arial", Font.PLAIN, 25));
        detailPanel.add(detail);
        addressPanel.add(address);
        detailPanel.setVisible(false);
        addressPanel.setVisible(false);
        address.setOpaque(false);
        detail.setOpaque(false);
        combinedAdressDetailPanel.add(detailPanel);
        combinedAdressDetailPanel.add(addressPanel);
        combinedAdressDetailPanel.add(time);
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
        alarmMonitorPanel.add(mapViewer, BorderLayout.SOUTH);

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
        displayCurrentTime(time);
        fullPanel.add(alarmMonitorPanel, "ALARM");
        initializeNormalPanel();
        fullPanel.add(normalPanel, "NORMAL");
        cardLayout.show(fullPanel, "NORMAL");
        this.pack();
    }

    public void triggerAlarm(Alarm alarm) throws IOException {
//        this.removeAll();
//        this.revalidate();
//        this.add(normalPanel);
    }

    public synchronized void setAlarmDetails(Alarm alarm) {
//        this.removeAll();
//        this.revalidate();
//        this.add(alarmMonitorPanel);
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
        time.setVisible(false);
        List<JLabel> list = carLabels.stream().filter(label -> alarm.getAlarmedCars().contains(label.getText())).collect(Collectors.toList());
        listener = new FlashListener(list, new Color(255, 0, 0), new Color(0, 255, 0));
        flasher = new Timer(1000, listener);
        flasher.setInitialDelay(0);
        flasher.start();
    }

    public void stopFlasher() {
        cardLayout.show(fullPanel, "NORMAL");
        time.setVisible(true);
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

    private void displayCurrentTime(JLabel label) {
//        final DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
        //time zone must be set when using LocalDateTime: see @https://bugs.openjdk.java.net/browse/JDK-8085887
        DateTimeFormatter f = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.FULL).withZone(ZoneId.systemDefault());
        new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar now = Calendar.getInstance();
                label.setText(f.format(LocalDateTime.now()));
            }
        }).start();
    }


    public void setAlarmMArkers(List<GeoPosition> track, GeoPosition start, GeoPosition end) {
        RoutePainter routePainter = new RoutePainter(track);
        Set<Waypoint> waypoints = new HashSet<Waypoint>(Arrays.asList(
                new DefaultWaypoint(start),
                new DefaultWaypoint(end)));
        // Create a waypoint painter that takes all the waypoints
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        waypointPainter.setWaypoints(waypoints);
        mapViewer.zoomToBestFit(new HashSet<>(track), 0.6);

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


    private void initializeNormalPanel() {
        try {
            i = ImageIO.read(new URL("http://test.ff-hambruecken.de/images/Feuerwehr-1-2.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        normalPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                scaled = i.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                super.paintComponent(g);

                g.drawImage(scaled, 0, 0, null);
            }
        };
        CustomListModel listModel = new CustomListModel();
        Meeting m1 = new Meeting("Theorie FwDv 3", " im Schulungsraum", LocalDateTime.of(2019, 9, 05, 16, 00)
                , LocalDateTime.of(2019, 9, 05, 16, 05));

        Meeting m4 = new Meeting("Theorie FwDv 3", " im Schulungsraum", LocalDateTime.of(2019, 9, 05, 16, 15)
                , LocalDateTime.of(2019, 9, 05, 16, 20));

        Meeting m2 = new Meeting("Übung", " in der Fahrzeughalle", LocalDateTime.of(2019, 9, 05, 16, 30)
                , LocalDateTime.of(2019, 9, 05, 16, 35));

        Meeting m3 = new Meeting("Theorie FwDv 3", " im Schulungsraum", LocalDateTime.of(2019, 9, 05, 16, 45)
                , LocalDateTime.of(2019, 9, 05, 16, 50));

        Meeting m5 = new Meeting("Theorie FwDv 3", " im Schulungsraum", LocalDateTime.of(2019, 9, 05, 17, 00)
                , LocalDateTime.of(2019, 9, 05, 17, 10));

        Meeting m6 = new Meeting("Theorie FwDv 3", " im Schulungsraum", LocalDateTime.of(2019, 9, 05, 17, 15)
                , LocalDateTime.of(2019, 9, 05, 17, 18));

        Meeting m7 = new Meeting("Theorie FwDv 3", " im Schulungsraum", LocalDateTime.of(2019, 9, 05, 17, 20)
                , LocalDateTime.of(2019, 9, 05, 17, 25));

        Meeting m8 = new Meeting("Theorie FwDv 3", " im Schulungsraum", LocalDateTime.of(2019, 9, 05, 17, 30)
                , LocalDateTime.of(2019, 9, 05, 17, 35));

        Meeting m9 = new Meeting("Theorie FwDv 3", " im Schulungsraum", LocalDateTime.of(2019, 9, 05, 17, 45)
                , LocalDateTime.of(2019, 9, 05, 17, 47));

        Meeting m10 = new Meeting("Theorie FwDv 3", " im Schulungsraum", LocalDateTime.of(2019, 9, 05, 17, 50)
                , LocalDateTime.of(2019, 9, 05, 17, 55));

        listModel.addElement(m1);
        listModel.addElement(m2);
        listModel.addElement(m3);
        listModel.addElement(m4);
        listModel.addElement(m5);
        listModel.addElement(m6);
        listModel.addElement(m7);
        listModel.addElement(m8);
        listModel.addElement(m9);
        listModel.addElement(m10);

        JList list = new JList(listModel) {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };
        list.setCellRenderer(new CustomListCellRenderer());

        ComponentListener l = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                list.setFixedCellHeight(10);
                list.setFixedCellHeight(-1);
            }
        };

        list.addComponentListener(l);
        normalPanel.setLayout(new BorderLayout());
        JPanel componentsPanel = new JPanel(new BorderLayout());
        JLabel timeLabel = new JLabel("Aktuelle Zeit", SwingConstants.CENTER);
//        timeLabel.setOpaque(true);
        displayCurrentTime(timeLabel);
        normalPanel.add(list, BorderLayout.WEST);
        normalPanel.add(timeLabel, BorderLayout.NORTH);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        normalPanel.setOpaque(false);
        list.setOpaque(false);
//        normalPanel.add(componentsPanel);
    }
}
