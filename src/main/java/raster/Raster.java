package raster;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Raster  {

    private BufferedImage img;
    private int color = 0xFF0000;

    public BufferedImage getImg() {
        return img;
    }

    public Raster(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void repaint(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }

    public int getRGB(int x, int y) {
        return img.getRGB(x, y);
    }

    public void setRGB(int x, int y, int color) {
        img.setRGB(x, y, color);
    }

    public void clear() {
        Graphics g = img.getGraphics();
        g.setColor(new Color(color));
        g.clearRect(0, 0, img.getWidth() - 1, img.getHeight() - 1);
    }

    public void setClearColor(int color) {
        this.color = color;
    }

    public int getWidth() {
        return img.getWidth();
    }

    public int getHeight() {
        return img.getHeight();
    }
}