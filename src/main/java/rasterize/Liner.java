package rasterize;

import objectdata.Object3D;
import raster.Raster;
import transforms.Mat4;
import transforms.Vec2D;

import java.awt.*;

import static java.lang.Math.abs;

public class Liner {
    public void draw(Raster raster, Vec2D first, Vec2D second, int color) {
        double dx = second.getX() - first.getX();
        double dy = second.getY() - first.getY();

        Vec2D tmp2;
        Vec2D tmp1;

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
}