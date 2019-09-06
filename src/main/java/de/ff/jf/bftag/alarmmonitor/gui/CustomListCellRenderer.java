package de.ff.jf.bftag.alarmmonitor.gui;

import de.ff.jf.bftag.alarmmonitor.models.Meeting;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class CustomListCellRenderer extends DefaultListCellRenderer {
    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
    JLabel lt = new JLabel();
    String pre = "<html><body style='width: 300px;'>";


    CustomListCellRenderer() {
        lt.setFont(new Font("Arial", Font.PLAIN, 40));
        JLabel l = new JLabel();//<-- this will be an icon instead of a text
        try {
            BufferedImage imageIcon = ImageIO.read(new File("C:\\Users\\SchweglerS\\IdeaProjects\\Alarmmonitor\\src\\main\\resources\\images\\icon_burned.png"));
            l.setIcon(new ImageIcon(imageIcon.getScaledInstance(75, 75, Image.SCALE_SMOOTH)));
            l.setPreferredSize(new Dimension(75, 75));
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.add(l, BorderLayout.WEST);

        p.add(lt, BorderLayout.CENTER);
        p.setOpaque(false);
    }

    @Override
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean hasFocus) {
        final Meeting text = (Meeting) value;
        lt.setText(pre + value.toString());
        checkMarkupNeeded(text);
        setOpaque(false);
        return p;
    }

    private void checkMarkupNeeded(Meeting text) {
        if (LocalDateTime.now().isAfter(text.getStartDate()) && LocalDateTime.now().isBefore(text.getEndDate())) {
            lt.setForeground(new Color(0, 255, 0));
            lt.setFont(new Font("Arial", Font.ITALIC, 50));
        } else {
            lt.setForeground(new Color(255, 0, 0));
            lt.setFont(new Font("Arial", Font.PLAIN, 40));
        }
    }
}
