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

    private Image fms1, fms2, fms3, fms4, fms5;


    public FMSListCellRenderer() {

        Image one = null;
        try {
            one = ImageIO.read(new File("C:\\Users\\SchweglerS\\IdeaProjects\\Alarmmonitor\\src\\main\\resources\\images\\fms3.png"));
            fms3 = one.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            Image two = ImageIO.read(new File("C:\\Users\\SchweglerS\\IdeaProjects\\Alarmmonitor\\src\\main\\resources\\images\\fms4.png"));
            fms4 = two.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            Image three = ImageIO.read(new File("C:\\Users\\SchweglerS\\IdeaProjects\\Alarmmonitor\\src\\main\\resources\\images\\fms2.png"));
            fms2 = three.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            Image four = ImageIO.read(new File("C:\\Users\\SchweglerS\\IdeaProjects\\Alarmmonitor\\src\\main\\resources\\images\\fms5.png"));
            fms5 = four.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        final Car text = (Car) value;
        lt.setText(pre + ((Car) value).getName() + " <br/>");

        if (text.getFms() == 2) {
            l.setIcon(new ImageIcon(fms2));
        }
        if (text.getFms() == 3) {
            lt.setForeground(Color.GREEN);
            lt.setFont(new Font("Arial", Font.ITALIC, 30));
            System.err.println("Icon 3");
            l.setIcon(new ImageIcon(fms3));
            setOpaque(false);
            p.add(l, BorderLayout.WEST);
            p.add(lt, BorderLayout.CENTER);
            return p;
        }
        if (text.getFms() == 4) {
            l.setIcon(new ImageIcon(fms4));
            lt.setForeground(Color.GREEN);
            lt.setFont(new Font("Arial", Font.PLAIN, 30));
            setOpaque(false);
            p.add(l, BorderLayout.WEST);
            p.add(lt, BorderLayout.CENTER);
            return p;
        }

        if (text.getFms() == 5) {
            l.setIcon(new ImageIcon(fms5));
            lt.setFont(new Font("Arial", Font.ITALIC, 30));
            lt.setForeground(Color.RED);
            p.add(l, BorderLayout.WEST);
            p.add(lt, BorderLayout.CENTER);
            return p;
        }

        lt.setFont(new Font("Arial", Font.BOLD, 30));
        lt.setForeground(Color.BLACK);
        setOpaque(false);
        p.add(l, BorderLayout.WEST);
        p.add(lt, BorderLayout.CENTER);
        return p;
    }
}
