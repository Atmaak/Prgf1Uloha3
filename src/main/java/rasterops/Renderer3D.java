package rasterops;

import objectdata.Object3D;
import objectdata.Scene;
import raster.Raster;
import transforms.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
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
                    g.setColor(new Color(object.getColor(i / 2).getRGB()));
                    g.drawLine(x1, y1, x2, y2);
    
                    if (object.isFilled()) {
                        drawFilledSides(g, object, finalTransform, img);
                    }
                }
            }
        }
    }
    
    private void drawFilledSides(Graphics g, Object3D object, Mat4 finalTransform, BufferedImage img) {
        List<Integer> indexs = object.getIndexBuffer();
        double[][] zBuffer = this.zBuffer.getZBuffer();
    
        for (int i = 0; i < indexs.size(); i += 6) {
            // First triangle
            int ibA = indexs.get(i);
            int ibB = indexs.get(i + 1);
            int ibC = indexs.get(i + 2);
    
            Point3D vbA = object.getVertexBuffer().get(ibA).mul(finalTransform);
            Point3D vbB = object.getVertexBuffer().get(ibB).mul(finalTransform);
            Point3D vbC = object.getVertexBuffer().get(ibC).mul(finalTransform);
    
            Vec3D vectorA = vbA.dehomog().orElse(null);
            Vec3D vectorB = vbB.dehomog().orElse(null);
            Vec3D vectorC = vbC.dehomog().orElse(null);
    
            if (vectorA != null && vectorB != null && vectorC != null) {
                rasterizeTriangle(g, vectorA, vectorB, vectorC, img, zBuffer, object.getColor(0));
            }
    
            // Second triangle
            ibA = indexs.get(i + 3);
            ibB = indexs.get(i + 4);
            ibC = indexs.get(i + 5);
    
            vbA = object.getVertexBuffer().get(ibA).mul(finalTransform);
            vbB = object.getVertexBuffer().get(ibB).mul(finalTransform);
            vbC = object.getVertexBuffer().get(ibC).mul(finalTransform);
    
            vectorA = vbA.dehomog().orElse(null);
            vectorB = vbB.dehomog().orElse(null);
            vectorC = vbC.dehomog().orElse(null);
    
            if (vectorA != null && vectorB != null && vectorC != null) {
                rasterizeTriangle(g, vectorA, vectorB, vectorC, img, zBuffer, object.getColor(0));
            }
        }
    }
    
    // private void rasterizeTriangle(Graphics g, Vec3D a, Vec3D b, Vec3D c, BufferedImage img, double[][] zBuffer, Col color) {
    //     int imgWidth = img.getWidth();
    //     int imgHeight = img.getHeight();
    
    //     int aX = (int) ((1 + a.getX()) * (imgWidth - 1) / 2);
    //     int aY = (int) ((1 - a.getY()) * (imgHeight - 1) / 2);
    //     double aZ = a.getZ();
    
    //     int bX = (int) ((1 + b.getX()) * (imgWidth - 1) / 2);
    //     int bY = (int) ((1 - b.getY()) * (imgHeight - 1) / 2);
    //     double bZ = b.getZ();
    
    //     int cX = (int) ((1 + c.getX()) * (imgWidth - 1) / 2);
    //     int cY = (int) ((1 - c.getY()) * (imgHeight - 1) / 2);
    //     double cZ = c.getZ();
    
    //     // Sort vertices by y-coordinate
    //     if (aY > bY) { int tempX = aX; aX = bX; bX = tempX; int tempY = aY; aY = bY; bY = tempY; double tempZ = aZ; aZ = bZ; bZ = tempZ; }
    //     if (aY > cY) { int tempX = aX; aX = cX; cX = tempX; int tempY = aY; aY = cY; cY = tempY; double tempZ = aZ; aZ = cZ; cZ = tempZ; }
    //     if (bY > cY) { int tempX = bX; bX = cX; cX = tempX; int tempY = bY; bY = cY; cY = tempY; double tempZ = bZ; bZ = cZ; cZ = tempZ; }
    
    //     int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    
    //     for (int y = aY; y <= cY; y++) {
    //         if (y < 0 || y >= imgHeight) continue; // Boundary check
    
    //         double tAB = (y - aY) / (double)(bY - aY);
    //         int xAB = (int) Math.round((1 - tAB) * aX + tAB * bX);
    //         double zAB = (1 - tAB) * aZ + tAB * bZ;
    
    //         double tAC = (y - aY) / (double)(cY - aY);
    //         int xAC = (int) Math.round((1 - tAC) * aX + tAC * cX);
    //         double zAC = (1 - tAC) * aZ + tAC * cZ;
    
    //         double tBC = (y - bY) / (double)(cY - bY);
    //         int xBC = (int) Math.round((1 - tBC) * bX + tBC * cX);
    //         double zBC = (1 - tBC) * bZ + tBC * cZ;
    
    //         int xStart, xEnd;
    //         double zStart, zEnd;
    
    //         if (y < bY) {
    //             xStart = xAB;
    //             zStart = zAB;
    //             xEnd = xAC;
    //             zEnd = zAC;
    //         } else {
    //             xStart = xBC;
    //             zStart = zBC;
    //             xEnd = xAC;
    //             zEnd = zAC;
    //         }
    
    //         if (xStart > xEnd) {
    //             int tempX = xStart;
    //             double tempZ = zStart;
    //             xStart = xEnd;
    //             zStart = zEnd;
    //             xEnd = tempX;
    //             zEnd = tempZ;
    //         }
    
    //         for (int x = xStart; x <= xEnd; x++) {
    //             if (x < 0 || x >= imgWidth) continue; // Boundary check
    
    //             double t = (x - xStart) / (double)(xEnd - xStart);
    //             double z = (1 - t) * zStart + t * zEnd;
    //             if (z < zBuffer[x][y]) {
    //                 zBuffer[x][y] = z;
    //                 pixels[y * imgWidth + x] = color.getRGB(); // Set pixel color
    //             }
    //         }
    //     }
    // }
    private void rasterizeTriangle(Graphics g, Vec3D a, Vec3D b, Vec3D c, BufferedImage img, double[][] zBuffer, Col color) {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
    
        int aX = (int) ((1 + a.getX()) * (imgWidth - 1) / 2);
        int aY = (int) ((1 - a.getY()) * (imgHeight - 1) / 2);
        double aZ = a.getZ();
    
        int bX = (int) ((1 + b.getX()) * (imgWidth - 1) / 2);
        int bY = (int) ((1 - b.getY()) * (imgHeight - 1) / 2);
        double bZ = b.getZ();
    
        int cX = (int) ((1 + c.getX()) * (imgWidth - 1) / 2);
        int cY = (int) ((1 - c.getY()) * (imgHeight - 1) / 2);
        double cZ = c.getZ();
    
        // Sort vertices by y-coordinate
        if (aY > bY) { int tempX = aX; aX = bX; bX = tempX; int tempY = aY; aY = bY; bY = tempY; double tempZ = aZ; aZ = bZ; bZ = tempZ; }
        if (aY > cY) { int tempX = aX; aX = cX; cX = tempX; int tempY = aY; aY = cY; cY = tempY; double tempZ = aZ; aZ = cZ; cZ = tempZ; }
        if (bY > cY) { int tempX = bX; bX = cX; cX = tempX; int tempY = bY; bY = cY; cY = tempY; double tempZ = bZ; bZ = cZ; cZ = tempZ; }
    
        int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    
        for (int y = Math.max(aY, 0); y <= Math.min(cY, imgHeight - 1); y++) {
            double tAB = (y - aY) / (double)(bY - aY);
            int xAB = (int) Math.round((1 - tAB) * aX + tAB * bX);
            double zAB = (1 - tAB) * aZ + tAB * bZ;
    
            double tAC = (y - aY) / (double)(cY - aY);
            int xAC = (int) Math.round((1 - tAC) * aX + tAC * cX);
            double zAC = (1 - tAC) * aZ + tAC * cZ;
    
            double tBC = (y - bY) / (double)(cY - bY);
            int xBC = (int) Math.round((1 - tBC) * bX + tBC * cX);
            double zBC = (1 - tBC) * bZ + tBC * cZ;
    
            int xStart, xEnd;
            double zStart, zEnd;
    
            if (y < bY) {
                xStart = xAB;
                zStart = zAB;
                xEnd = xAC;
                zEnd = zAC;
            } else {
                xStart = xBC;
                zStart = zBC;
                xEnd = xAC;
                zEnd = zAC;
            }
    
            if (xStart > xEnd) {
                int tempX = xStart;
                double tempZ = zStart;
                xStart = xEnd;
                zStart = zEnd;
                xEnd = tempX;
                zEnd = tempZ;
            }
    
            for (int x = Math.max(xStart, 0); x <= Math.min(xEnd, imgWidth - 1); x++) {
                double t = (x - xStart) / (double)(xEnd - xStart);
                double z = (1 - t) * zStart + t * zEnd;
                if (z < zBuffer[x][y]) {
                    zBuffer[x][y] = z;
                    if (x < imgWidth - 1 && y < imgHeight - 1) { // Boundary check
                        pixels[y * imgWidth + x] = color.getRGB(); // Set pixel color
                    }
                }
            }
        }
    }
}