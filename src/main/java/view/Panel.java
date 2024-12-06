package view;

import raster.Raster;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Panel extends JPanel {
    Raster raster;
    public Panel(Raster raster) {
        this.raster = raster;
        setPreferredSize(new Dimension(raster.getWidth(), raster.getHeight()));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.repaint(g);
    }
}
