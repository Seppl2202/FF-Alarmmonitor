package de.ff.jf.bftag.alarmmonitor.gui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserHomeImageList {
    String userHomeFilePath;
    private Path userHomeDirectory;
    private List<BufferedImage> imageList;

    public UserHomeImageList() {
        imageList = new LinkedList<>();
        initialize();
    }

    private void initialize() {
        userHomeFilePath = System.getProperty("user.home") + File.separator + "alarmmonitor" + File.separator + "images";
//        userHomeFilePath = userHomeFilePath.replace("\\", "\\\\");
        registerTimerTask();
        readDirectory();
    }

    private void readDirectory() {
        System.err.println("Reading directory");
        File f = new File(userHomeFilePath);
        if (!f.exists()) {
            f.mkdirs();
        }

        List<BufferedImage> tempList = new LinkedList<>();
        for (final File entry : f.listFiles()) {
            BufferedImage i = null;
            try {
                i = ImageIO.read(entry);
                tempList.add(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.err.println("Found " + tempList.size() + " files");
        imageList = tempList;
    }

    private void registerTimerTask() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> readDirectory(), 0, 60, TimeUnit.SECONDS);
    }

    public List<BufferedImage> getImageList() {
        return imageList;
    }
}
