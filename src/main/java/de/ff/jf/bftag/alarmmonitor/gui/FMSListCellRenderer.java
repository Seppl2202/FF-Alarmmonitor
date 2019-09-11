package de.ff.jf.bftag.alarmmonitor.gui;

import de.ff.jf.bftag.alarmmonitor.models.Car;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FMSListCellRenderer extends DefaultListCellRenderer {
    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
    JLabel lt = new JLabel();
    JLabel l = new JLabel();
    String pre = "<html><body style='width: 250px;'>";

    private Image fms3, fms4;


    public FMSListCellRenderer() {
        lt.setFont(new Font("Arial", Font.BOLD, 30));
        Image one = null;
        try {
            one = ImageIO.read(new File("C:\\Users\\SchweglerS\\IdeaProjects\\Alarmmonitor\\src\\main\\resources\\images\\fms3.png"));
            fms3 = one.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            Image two = ImageIO.read(new File("C:\\Users\\SchweglerS\\IdeaProjects\\Alarmmonitor\\src\\main\\resources\\images\\fms4.png"));
            fms4 = two.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        final Car text = (Car) value;
        if (text.getFms() == 3) {
            System.err.println("Icon 3");
            l.setIcon(new ImageIcon(fms3));
        }
        if (text.getFms() == 4) {
            l.setIcon(new ImageIcon(fms4));
        }
        lt.setText(pre + ((Car) value).getName() + " <br/>");
        setOpaque(false);
        p.add(l, BorderLayout.WEST);
        p.add(lt, BorderLayout.CENTER);
        return p;
    }
}
