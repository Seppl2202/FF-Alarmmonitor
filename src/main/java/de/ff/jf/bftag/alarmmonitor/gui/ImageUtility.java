package de.ff.jf.bftag.alarmmonitor.gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtility {

    public static Image FMS1, FMS2, FMS3, FMS4, FMS5, FMS6, FMS7;
    public static Image MTWImage, LF1612Image, RWImage, LF16TSImage, StartImage, EndImage;

    public ImageUtility() {
        try {
            BufferedImage s = ImageIO.read(new File("/Users/sesc4/Desktop/Java/Alarmmonitor/src/main/resources/images/navstart.png"));
            BufferedImage e = ImageIO.read(new File("/Users/sesc4/Desktop/Java/Alarmmonitor/src/main/resources/images/navfinish.png"));
            BufferedImage m = ImageIO.read(new File("/Users/sesc4/Desktop/Java/Alarmmonitor/src/main/resources/images/lf16-12.png"));
            BufferedImage r = ImageIO.read(new File("/Users/sesc4/Desktop/Java/Alarmmonitor/src/main/resources/images/rw.png"));
            LF1612Image = m.getScaledInstance(150, 100, Image.SCALE_DEFAULT);
            RWImage = r.getScaledInstance(150, 50, Image.SCALE_DEFAULT);
            StartImage = s.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            EndImage = e.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            Image one = ImageIO.read(new File("/Users/sesc4/Desktop/Java/Alarmmonitor/src/main/resources/images/fms3.png"));
            FMS3 = one.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            Image two = ImageIO.read(new File("/Users/sesc4/Desktop/Java/Alarmmonitor/src/main/resources/images/fms4.png"));
            FMS4 = two.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            Image three = ImageIO.read(new File("/Users/sesc4/Desktop/Java/Alarmmonitor/src/main/resources/images/fms2.png"));
            FMS2 = three.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            Image four = ImageIO.read(new File("/Users/sesc4/Desktop/Java/Alarmmonitor/src/main/resources/images/fms5.png"));
            FMS5 = four.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
