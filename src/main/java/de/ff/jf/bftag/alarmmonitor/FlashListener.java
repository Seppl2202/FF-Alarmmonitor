package de.ff.jf.bftag.alarmmonitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FlashListener implements ActionListener {
    private List<JLabel> labelsToFlash;
    private Color oldColor, newColor;

    public FlashListener(List<JLabel> labelsToFlash, Color oldColor, Color newColor) {
        this.labelsToFlash = labelsToFlash;
        this.oldColor = oldColor;
        this.newColor = newColor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        labelsToFlash.forEach(label -> {
            if (label.getBackground().equals(oldColor)) {
                label.setBackground(newColor);
            } else {
                label.setBackground(oldColor);
            }
        });
    }

}
