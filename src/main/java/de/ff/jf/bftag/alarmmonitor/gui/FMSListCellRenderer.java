package de.ff.jf.bftag.alarmmonitor.gui;

import de.ff.jf.bftag.alarmmonitor.models.Car;

import javax.swing.*;
import java.awt.*;

public class FMSListCellRenderer extends DefaultListCellRenderer {
    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
    JLabel lt = new JLabel();
    JLabel l = new JLabel();
    String pre = "<html><body style='width: 250px;'>";

    private Image fms1, fms2, fms3, fms4, fms5;


    public FMSListCellRenderer() {
        fms1 = ImageUtility.FMS1;
        fms2 = ImageUtility.FMS2;
        fms3 = ImageUtility.FMS3;
        fms4 = ImageUtility.FMS4;
        fms5 = ImageUtility.FMS5;
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
            lt.setForeground(Color.YELLOW);
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
