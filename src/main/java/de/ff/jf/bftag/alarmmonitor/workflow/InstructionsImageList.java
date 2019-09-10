package de.ff.jf.bftag.alarmmonitor.workflow;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class InstructionsImageList {
    private Map<String, Image> images;

    public InstructionsImageList() {
        images = new LinkedHashMap<>();
    }

    public void addImage(String instructionToDisplay, String filePath) {
        try {
            Image i = ImageIO.read(new File(filePath));
            images.put(instructionToDisplay, i.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Image> getImages() {
        return images;
    }
}
