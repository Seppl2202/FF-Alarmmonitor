package de.ff.jf.bftag.alarmmonitor.gui;

import de.ff.jf.bftag.alarmmonitor.models.CustomWaypoint;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

import java.awt.*;
import java.awt.geom.Point2D;

public class CustomWaypointRenderer implements WaypointRenderer<CustomWaypoint> {


    private Image startImage, endImage, lf1612Image, rwImage;

    public CustomWaypointRenderer() {
        startImage = ImageUtility.StartImage;
        endImage = ImageUtility.EndImage;
        lf1612Image = ImageUtility.LF1612Image;
        rwImage = ImageUtility.RWImage;
    }


    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer viewer, CustomWaypoint w) {

        if (startImage == null || endImage == null) {
            return;

        }
        if (w.getLabel().equalsIgnoreCase("Feuerwehrhaus")) {
            g = (Graphics2D) g.create();
            Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());

            int x = (int) point.getX();
            int y = (int) point.getY();


            g.drawImage(startImage, x - startImage.getWidth(null) / 2, y - startImage.getHeight(null), null);

            String label = w.getLabel();

            FontMetrics metrics = g.getFontMetrics();
            int tw = metrics.stringWidth(label);
            int th = 1 + metrics.getAscent();

            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.setColor(Color.RED);
            g.drawString(label, x - tw / 2, y + th - 25 - startImage.getHeight(null));

            g.dispose();
            return;
        }

        if (w.getLabel().equalsIgnoreCase("Ziel")) {
            g = (Graphics2D) g.create();
            Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());
            int x = (int) point.getX();
            int y = (int) point.getY();

            g.drawImage(endImage, x - endImage.getWidth(null) / 2, y - endImage.getHeight(null), null);

            String label = w.getLabel();

            FontMetrics metrics = g.getFontMetrics();
            int tw = metrics.stringWidth(label);
            int th = 1 + metrics.getAscent();

//            g.setFont(new Font("Arial", Font.BOLD, 15));
//            g.setColor(Color.GREEN);
//            g.drawString(label, x - tw / 2, y + th - 25 - endImage.getHeight(null));

            g.dispose();
            return;
        }
        if (w.getLabel().equalsIgnoreCase("LF16-12")) {
            g = (Graphics2D) g.create();
            Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());
            //lf 16/12 is displayed NORTH, above the current pos
            int x = (int) point.getX();
            int y = (int) point.getY();

            g.drawImage(lf1612Image, x - lf1612Image.getWidth(null) / 2, y - lf1612Image.getHeight(null), null);
            g.setColor(Color.GREEN);
            g.dispose();
            return;
        }
        if (w.getLabel().equalsIgnoreCase("RW")) {
            g = (Graphics2D) g.create();
            Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());
            int x = (int) point.getX();
            int y = (int) point.getY();

            g.drawImage(rwImage, x - (rwImage.getWidth(null) / 2) + 150, y - rwImage.getHeight(null), null);
            g.dispose();
            return;
        }
    }
}
