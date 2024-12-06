package rasterize;

import raster.Raster;
import transforms.Vec2D;

import static java.lang.Math.abs;

public class Liner {

    public void draw(Raster raster, int x1, int y1, int x2, int y2, int color) {
        double dx = (x2 - x1);
        double dy = (y2 - y1);

        if (abs(dx) > abs(dy)) {
            if (x1 > x2) {
                int tempX = x1;
                int tempY = y1;
                x1 = x2;
                y1 = y2;
                x2 = tempX;
                y2 = tempY;
            }
            double k = dy / dx;
            double q = y1 - k * x1;
            for (int x = x1; x <= x2; x++) {
                int y = (int) (k * x + q);
                raster.setRGB(x, y, color);
            }
        } else {
            if (y2 < y1) {
                int tempX = x1;
                int tempY = y1;
                x1 = x2;
                y1 = y2;
                x2 = tempX;
                y2 = tempY;
            }
            double k = dx / dy;
            double q = x1 - k * y1;
            for (int y = y1; y <= y2; y++) {
                int x = (int) (k * y + q);
                raster.setRGB(x, y, color);
            }
        }
    }

    public void draw(Raster raster, Vec2D first, Vec2D second, int color) {
        double dx = second.getX() - first.getX();
        double dy = second.getY() - first.getY();

        Vec2D tmp1 = new Vec2D(0, 0);
        Vec2D tmp2 = new Vec2D(0, 0);

        // Check if the line is more horizontal than vertical
        if (Math.abs(dx) > Math.abs(dy)) {
            // Ensure the first point is to the left of the second point
            if (first.getX() > second.getX()) {
                tmp1 = new Vec2D(second.getX(), second.getY());
                tmp2 = new Vec2D(first.getX(), first.getY());
            } else {
                tmp1 = new Vec2D(first.getX(), first.getY());
                tmp2 = new Vec2D(second.getX(), second.getY());
            }

            double k = dy / dx;
            double q = tmp1.getY() - k * tmp1.getX();

            // Iterate over x values from tmp1 to tmp2
            for (int x = (int) tmp1.getX(); x <= tmp2.getX(); x++) {
                int y = (int) (k * x + q);
                raster.setRGB(x, y, color);
            }

        } else {
            // Ensure the first point is lower than the second point in terms of y
            if (first.getY() > second.getY()) {
                tmp1 = new Vec2D(second.getX(), second.getY());
                tmp2 = new Vec2D(first.getX(), first.getY());
            } else {
                tmp1 = new Vec2D(first.getX(), first.getY());
                tmp2 = new Vec2D(second.getX(), second.getY());
            }

            double k = dx / dy;
            double q = tmp1.getX() - k * tmp1.getY();

            // Iterate over y values from tmp1 to tmp2
            for (int y = (int) tmp1.getY(); y <= tmp2.getY(); y++) {
                int x = (int) (k * y + q);
                raster.setRGB(x, y, color);
            }
        }
    }




    public void drawThick(Raster raster, int x1, int y1, int x2, int y2, int color) {
        double dx = x2-x1;
        double dy = y2-y1;
        if (abs(dx)>abs(dy)) {
            draw(raster, x1, y1, x2, y2, color);
            draw(raster, x1, y1 + 1, x2, y2 + 1, color);
            draw(raster, x1, y1 - 1, x2, y2 - 1, color);
        } else {
            draw(raster, x1, y1, x2, y2, color);
            draw(raster, x1 + 1, y1, x2 + 1, y2, color);
            draw(raster, x1 - 1, y1, x2 - 1, y2, color);
        }
    }
}