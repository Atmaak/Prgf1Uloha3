package rasterops;

import objectdata.Object3D;
import objectdata.Scene;
import raster.Raster;
import transforms.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Renderer3D {
    private Mat4 model;
    private ZBuffer zBuffer;

    public Renderer3D(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void renderScene(Raster raster, Scene scene, Mat4 matView, Mat4 matProjection, Mat4 model) {
        for (Object3D object : scene.getObjects()) {
            draw(object, model, raster, matProjection, matView);
        }
    }

    private static final int INSIDE = 0; // 0000
    private static final int LEFT = 1;   // 0001
    private static final int RIGHT = 2;  // 0010
    private static final int BOTTOM = 4; // 0100
    private static final int TOP = 8;    // 1000

    private int computeOutCode(double x, double y, int width, int height) {
        int code = INSIDE;

        if (x < 0) {
            code |= LEFT;
        } else if (x >= width) {
            code |= RIGHT;
        }
        if (y < 0) {
            code |= BOTTOM;
        } else if (y >= height) {
            code |= TOP;
        }

        return code;
    }

    private boolean cohenSutherlandClip(double[] x0, double[] y0, double[] x1, double[] y1, int width, int height) {
        int outcode0 = computeOutCode(x0[0], y0[0], width, height);
        int outcode1 = computeOutCode(x1[0], y1[0], width, height);
        boolean accept = false;

        while (true) {
            if ((outcode0 | outcode1) == 0) {
                accept = true;
                break;
            } else if ((outcode0 & outcode1) != 0) {
                break;
            } else {
                double x, y;
                int outcodeOut = (outcode0 != 0) ? outcode0 : outcode1;

                if ((outcodeOut & TOP) != 0) {
                    x = x0[0] + (x1[0] - x0[0]) * (height - 1 - y0[0]) / (y1[0] - y0[0]);
                    y = height - 1;
                } else if ((outcodeOut & BOTTOM) != 0) {
                    x = x0[0] + (x1[0] - x0[0]) * (0 - y0[0]) / (y1[0] - y0[0]);
                    y = 0;
                } else if ((outcodeOut & RIGHT) != 0) {
                    y = y0[0] + (y1[0] - y0[0]) * (width - 1 - x0[0]) / (x1[0] - x0[0]);
                    x = width - 1;
                } else {
                    y = y0[0] + (y1[0] - y0[0]) * (0 - x0[0]) / (x1[0] - x0[0]);
                    x = 0;
                }

                if (outcodeOut == outcode0) {
                    x0[0] = x;
                    y0[0] = y;
                    outcode0 = computeOutCode(x0[0], y0[0], width, height);
                } else {
                    x1[0] = x;
                    y1[0] = y;
                    outcode1 = computeOutCode(x1[0], y1[0], width, height);
                }
            }
        }
        return accept;
    }

    public void draw(Object3D object, Mat4 model, Raster raster, Mat4 projection, Mat4 view) {
        BufferedImage img = raster.getImg();
    
        if (object.isTransferable()) {
            this.model = object.getTransMat().mul(model);
        } else {
            this.model = new Mat4Identity();
        }
    
        final Mat4 finalTransform = this.model.mul(view).mul(projection);
        List<Integer> indexs = object.getIndexBuffer();
        double[][] zBuffer = this.zBuffer.getZBuffer();
    
        for (int i = 0; i < indexs.size(); i += 2) {
            int ibA = object.getIndexBuffer().get(i);
            int ibB = object.getIndexBuffer().get(i + 1);
    
            Point3D vbA = object.getVertexBuffer().get(ibA);
            Point3D vbB = object.getVertexBuffer().get(ibB);
    
            vbA = vbA.mul(finalTransform);
            vbB = vbB.mul(finalTransform);
    
            Vec3D vectorA = null;
            Vec3D vectorB = null;
    
            if (vbA.dehomog().isPresent()) {
                vectorA = vbA.dehomog().get();
            }
    
            if (vbB.dehomog().isPresent()) {
                vectorB = vbB.dehomog().get();
            }
    
            int x1 = (int) ((1 + vectorA.getX()) * (img.getWidth() - 1) / 2);
            int y1 = (int) ((1 - vectorA.getY()) * (img.getHeight() - 1) / 2);
            float z1 = (float) vectorA.getZ();
    
            int x2 = (int) ((1 + vectorB.getX()) * (img.getWidth() - 1) / 2);
            int y2 = (int) ((1 - vectorB.getY()) * (img.getHeight() - 1) / 2);
            float z2 = (float) vectorB.getZ();
    
            double[] x0 = {x1};
            double[] y0 = {y1};
            double[] x1Arr = {x2};
            double[] y1Arr = {y2};

            if (cohenSutherlandClip(x0, y0, x1Arr, y1Arr, img.getWidth() - 1, img.getHeight() - 1)) {
                x1 = (int) x0[0];
                y1 = (int) y0[0];
                x2 = (int) x1Arr[0];
                y2 = (int) y1Arr[0];

                if (z1 <= zBuffer[x1][y1] && z2 <= zBuffer[x2][y2] && z1 >= 0 && z1 <= 1 && z2 >= 0 && z2 <= 1) {
                    zBuffer[x1][y1] = z1;
                    zBuffer[x2][y2] = z2;
                
                    Graphics g = img.getGraphics();
                    g.setColor(new Color(object.getColor(i / 2)));
                    g.drawLine(x1, y1, x2, y2);
                }
            }
        }
    }
}