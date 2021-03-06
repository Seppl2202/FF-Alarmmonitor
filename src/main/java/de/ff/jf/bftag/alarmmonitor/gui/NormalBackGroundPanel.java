package de.ff.jf.bftag.alarmmonitor.gui;

import de.ff.jf.bftag.alarmmonitor.models.Meeting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.concurrent.*;

public class NormalBackGroundPanel extends JPanel {
    public static final long RUNNING_TIME = 1000;
    private BufferedImage scaledIn, in, scaledOut, out = null;
    private float alphaValue = 0f;
    private long startTime = -1;
    final Timer timer = new Timer(25, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (startTime < 0) {
                startTime = System.currentTimeMillis();
            } else {

                long now = System.currentTimeMillis();
                long duration = now - startTime;
                if (duration >= RUNNING_TIME) {
                    startTime = -1;
                    ((Timer) e.getSource()).stop();
                    alphaValue = 0f;
                } else {
                    alphaValue = 1f - ((float) duration / (float) RUNNING_TIME);
                }
                repaint();
            }
        }
    });
    private boolean isFirstStart = true;
    private JLabel timeLabel;
    private java.util.List<BufferedImage> imagesToDisplay;
    private UserHomeImageList userHomeImageList;
    private int listCount = 1;

    public NormalBackGroundPanel() {
        userHomeImageList = new UserHomeImageList();
        this.setSize(500, 500);
        imagesToDisplay = new LinkedList<>();
        initialize();
    }

    private void initialize() {

/*        try {
            in = ImageIO.read(new File("C:\\Users\\SchweglerS\\IdeaProjects\\Alarmmonitor\\src\\main\\resources\\images\\pic1.jpg"));
            out = ImageIO.read(new File("C:\\Users\\SchweglerS\\IdeaProjects\\Alarmmonitor\\src\\main\\resources\\images\\pic2.jpg"));
        } catch (
                IOException e) {
            e.printStackTrace();
        }*/

        CompletableFuture<java.util.List<BufferedImage>> compleatableFuture = CompletableFuture.supplyAsync(() -> userHomeImageList.getImageList());
        try {
            imagesToDisplay = compleatableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        in = imagesToDisplay.get(1);
        out = imagesToDisplay.get(0);
        CustomListModel listModel = new CustomListModel();
        Meeting m1 = new Meeting("Theorie FwDv 3", " im Schulungsraum", LocalDateTime.of(2019, 9, 12, 9, 00)
                , LocalDateTime.of(2019, 9, 12, 9, 35));

        Meeting m4 = new Meeting("Übung", " in der Fahrzeughalle", LocalDateTime.of(2019, 9, 12, 9, 40)
                , LocalDateTime.of(2019, 9, 12, 9, 45));

        Meeting m2 = new Meeting("Mittagessen", " im Schulungsraum", LocalDateTime.of(2019, 9, 12, 12, 00)
                , LocalDateTime.of(2019, 9, 12, 13, 00));

        Meeting m3 = new Meeting("Sport", " im Kraftraum", LocalDateTime.of(2019, 9, 12, 13, 30)
                , LocalDateTime.of(2019, 9, 12, 15, 00));

        Meeting m5 = new Meeting("Freizeit", " im Schulungsraum", LocalDateTime.of(2019, 9, 12, 15, 00)
                , LocalDateTime.of(2019, 9, 12, 18, 00));

        Meeting m6 = new Meeting("Abendessen", " im Schulungsraum", LocalDateTime.of(2019, 9, 16, 18, 00)
                , LocalDateTime.of(2019, 9, 16, 19, 00));

        Meeting m7 = new Meeting("Filmabend", " im Schulungsraum", LocalDateTime.of(2019, 9, 16, 19, 30)
                , LocalDateTime.of(2019, 9, 16, 21, 30));

        Meeting m8 = new Meeting("Nachtruhe", " im Schulungsraum", LocalDateTime.of(2019, 9, 16, 22, 00)
                , LocalDateTime.of(2019, 9, 17, 07, 00));

        Meeting m9 = new Meeting("Aufstehen", " im Schulungsraum", LocalDateTime.of(2019, 9, 13, 07, 00)
                , LocalDateTime.of(2019, 9, 13, 07, 30));

        Meeting m10 = new Meeting("Frühstück mit Eltern", " im Schulungsraum", LocalDateTime.of(2019, 9, 13, 9, 00)
                , LocalDateTime.of(2019, 9, 13, 10, 30));

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

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(() -> list.ensureIndexIsVisible(listModel.getSize()), 0, 5, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(() -> imagesToDisplay = userHomeImageList.getImageList(), 0, 1, TimeUnit.MINUTES);
        list.addComponentListener(l);
        this.setLayout(new BorderLayout());

        JPanel componentsPanel = new JPanel(new BorderLayout());
        timeLabel = new JLabel("Aktuelle Zeit", SwingConstants.CENTER);

        displayCurrentTime(timeLabel);
        this.add(list, BorderLayout.WEST);
        this.add(timeLabel, BorderLayout.NORTH);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        this.setOpaque(false);
        list.setOpaque(false);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                getScaledImageForWindowSize();
            }
        });
        this.setOpaque(false);

        ScheduledExecutorService executorService1 = Executors.newScheduledThreadPool(5);
        executorService.scheduleAtFixedRate(() -> {
            alphaValue = 0f;
            scaledIn = resize(imagesToDisplay.get((listCount + 1) % imagesToDisplay.size()), getWidth(), getHeight());
            listCount++;
            BufferedImage tmp = scaledIn;
            scaledIn = scaledOut;
            scaledOut = tmp;
            timer.start();
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void getScaledImageForWindowSize() {
        scaledIn = resize(in, getWidth(), getHeight());
        scaledOut = resize(out, getWidth(), getHeight());
    }

    @Override
    public void paintComponent(Graphics g) {
        if (isFirstStart) {
            getScaledImageForWindowSize();
            isFirstStart = false;
        }

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.SrcOver.derive(alphaValue));
        int x = (getWidth() - scaledIn.getWidth(this)) / 2;
        int y = (getHeight() - scaledIn.getHeight(this)) / 2;
        g2d.drawImage(scaledIn, x, y, this);

        super.paintComponent(g);
        g2d.setComposite(AlphaComposite.SrcOver.derive(1f - alphaValue));
        x = (getWidth() - scaledOut.getWidth(this)) / 2;
        y = (getHeight() - scaledOut.getHeight(this)) / 2;
        g2d.drawImage(scaledOut, x, y, this);
        g2d.dispose();
    }

    private void displayCurrentTime(JLabel label) {
        //time zone must be set when using LocalDateTime: see @https://bugs.openjdk.java.net/browse/DK-8085887
        DateTimeFormatter f = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT).withZone(ZoneId.systemDefault());
        new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar now = Calendar.getInstance();
                timeLabel.setText(f.format(LocalDateTime.now()));
            }
        }).start();
    }

    public BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
