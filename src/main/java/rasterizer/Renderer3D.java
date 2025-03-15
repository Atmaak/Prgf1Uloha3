package rasterizer;

import objectdata.Object3D;
import objectdata.Scene;
import raster.Raster;
import raster.ZBuffer;
import shader.Shader;
import shader.ShaderConstant;
import structure.Part;
import structure.Vertex;
import transforms.*;
import utils.Lerp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.List;

public class Renderer3D {
    private Mat4 model;
    private ZBuffer zBuffer;
    private Shader shader;
    private final Lerp<Vertex> lerp = new Lerp<>();
    private LineRasterizer lineRasterizer;
    private PointRasterizer pointRasterizer;
    private Raster raster;
    public Renderer3D(ZBuffer zBuffer, Raster raster) {
        this.zBuffer = zBuffer;
        this.raster = raster;
        lineRasterizer = new LineRasterizer(zBuffer, raster);
        pointRasterizer = new PointRasterizer(zBuffer, raster);
    }

    public void renderScene(Raster raster, Scene scene, Mat4 matView, Mat4 matProjection, Mat4 model, boolean shadered) {
        final Mat4 finalTransform = this.model.mul(matView).mul(matProjection);
        if(shadered){
            for (Object3D object : scene.getObjects()) {
                rasterizeWithShader(object, finalTransform);
            }
        }else {
            for (Object3D object : scene.getObjects()) {
                draw(object, model, raster, finalTransform);
            }
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

    public void draw(Object3D object, Mat4 model, Raster raster, Mat4 finalTransform) {
        BufferedImage img = raster.getImg();

        if (object.isTransferable()) {
            this.model = object.getTransMat().mul(model);
        } else {
            this.model = new Mat4Identity();
        }

        List<Integer> indexs = object.getIndexBuffer();
        double[][] zBuffer = this.zBuffer.getZBuffer();

        for (int i = 0; i < indexs.size(); i += 2) {
            int ibA = object.getIndexBuffer().get(i);
            int ibB = object.getIndexBuffer().get(i + 1);

            Point3D vbA = object.getVertexBuffer().get(ibA).getPosition();
            Point3D vbB = object.getVertexBuffer().get(ibB).getPosition();

            vbA = vbA.mul(finalTransform);
            vbB = vbB.mul(finalTransform);

            Vec3D vectorA = vbA.dehomog().get();

            Vec3D vectorB = vbB.dehomog().get();

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
                rasterizeTriangle(vectorA, vectorB, vectorC, img, zBuffer, object.getColor(0));
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
                rasterizeTriangle(vectorA, vectorB, vectorC, img, zBuffer, object.getColor(0));
            }
        }
    }

    private void rasterizeTriangle(Vec3D a, Vec3D b, Vec3D c, BufferedImage img, double[][] zBuffer, Col color) {
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

    public void rasterizeWithShader(Object3D object, Mat4 finalTransform){
        if (object.getIndexBuffer().isEmpty()) {
            return;
        }

        this.shader = object.getShader();

        // Pokud rendrujeme pomocí ConstatShader, tak použi barvu daného solidu
        if (shader instanceof ShaderConstant) {
            ((ShaderConstant) shader).setCol(object.getColor(0));
        }

        for (Part part : object.getPartBuffer()) {
            int start;
            switch (part.getTopologyType()) {
                case TRIANGLE_LIST:
                    start = part.getStartIndex();
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = object.getVertexBuffer().get(object.getIndexBuffer().get(start));
                        Vertex b = object.getVertexBuffer().get(object.getIndexBuffer().get(start + 1));
                        Vertex c = object.getVertexBuffer().get(object.getIndexBuffer().get(start + 2));
                        start += 3;

                        a = new Vertex(a.getPosition().mul(finalTransform), a.getCol(), a.getUv());
                        b = new Vertex(b.getPosition().mul(finalTransform), b.getCol(), b.getUv());
                        c = new Vertex(c.getPosition().mul(finalTransform), c.getCol(), c.getUv());

                        triangleShaderRasterizer(a, b, c, shader);
                    }
                    break;
                case TRIANGLE_STRIP:
                    start = part.getStartIndex();
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = object.getVertexBuffer().get(object.getIndexBuffer().get(start));
                        Vertex b = object.getVertexBuffer().get(object.getIndexBuffer().get(start + 1));
                        Vertex c = object.getVertexBuffer().get(object.getIndexBuffer().get(start + 2));
                        start += 1;

                        a = new Vertex(a.getPosition().mul(finalTransform), a.getCol(), a.getUv());
                        b = new Vertex(b.getPosition().mul(finalTransform), b.getCol(), b.getUv());
                        c = new Vertex(c.getPosition().mul(finalTransform), c.getCol(), c.getUv());

                        triangleShaderRasterizer(a, b, c, shader);
                    }
                    break;
                case LINE_LIST:
                    start = part.getStartIndex();
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = object.getVertexBuffer().get(object.getIndexBuffer().get(start));
                        Vertex b = object.getVertexBuffer().get(object.getIndexBuffer().get(start + 1));
                        start += 2;

                        a = new Vertex(a.getPosition().mul(finalTransform), a.getCol());
                        b = new Vertex(b.getPosition().mul(finalTransform), b.getCol());


                        if (!isLineInView(a, b)) {
                            continue;
                        }

                        rasterizeLineWithZClip(a, b);
                    }
                    break;
                case LINE_STRIP:
                    start = part.getStartIndex();
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = object.getVertexBuffer().get(object.getIndexBuffer().get(start));
                        Vertex b = object.getVertexBuffer().get(object.getIndexBuffer().get(start + 1));
                        start += 1;

                        a = new Vertex(a.getPosition().mul(finalTransform), a.getCol());
                        b = new Vertex(b.getPosition().mul(finalTransform), b.getCol());


                        if (!isLineInView(a, b)) {
                            continue;
                        }

                        rasterizeLineWithZClip(a, b);
                    }
                    break;
                case POINT:
                    start = part.getStartIndex();
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = object.getVertexBuffer().get(object.getIndexBuffer().get(start));
                        start++;

                        a = new Vertex(a.getPosition().mul(finalTransform), a.getCol());

                        if (!isPointInView(a)) {
                            continue;
                        }

                        pointRasterizer.rasterize(a, shader);
                    }
                    break;
            }

            // pronásobení pozic a ostatní atributy by měli být ve Vertexu
            // lepší si udělat jednu matici MVP a tou potom násobit všechny ostatní
        }
    }
    private Vec3D transformToWindow(Vertex v) {
        return v.getPosition()
                .ignoreW()
                .mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((raster.getWidth() - 1) / 2d, (raster.getHeight() - 1) / 2d, 1d)
                );
    }

    private boolean isPointInView(Vertex v) {
        Point3D point3D = v.getPosition();

        // obracena logika
        //−𝑤 ≤ 𝑥 ≤ 𝑤,
        //−𝑤 ≤ 𝑦 ≤ 𝑤 a 0 ≤ 𝑧 ≤ 𝑤
        return !((point3D.getX() < -point3D.getW()) ||
                (point3D.getX() > point3D.getW()) ||
                (point3D.getY() < -point3D.getW()) ||
                (point3D.getY() > point3D.getW()) ||
                (point3D.getZ() < 0 ||
                        (point3D.getZ() > point3D.getW())));
    }

    private boolean isLineInView(Vertex v1, Vertex v2) {
        Point3D pA = v1.getPosition();
        Point3D pB = v2.getPosition();

        // obracena logika
        //−𝑤 ≤ 𝑥 ≤ 𝑤,
        //−𝑤 ≤ 𝑦 ≤ 𝑤 a 0 ≤ 𝑧 ≤ 𝑤
        return !((pA.getX() < -pA.getW() && pB.getX() < -pB.getW()) ||
                (pA.getX() > pA.getW() && pB.getX() > pB.getW()) ||
                (pA.getY() < -pA.getW() && pB.getY() < -pB.getW()) ||
                (pA.getY() > pA.getW() && pB.getY() > pB.getW()) ||
                (pA.getZ() < 0 && pB.getZ() < 0) ||
                (pA.getZ() > pA.getW() && pB.getZ() > pB.getW()));

    }

    private void rasterizeLineWithZClip(Vertex a, Vertex b) {

        if (a.getVec3D().getZ() > b.getVec3D().getZ()) {
            Vertex tmp = a;
            a = b;
            b = tmp;
        }

        if (a.getPosition().getZ() < 0)
            return;

        if (b.getPosition().getZ() < 0) {
            double t1 = a.getPosition().getZ() / (a.getPosition().getZ() - b.getPosition().getZ());
            Vertex ab = a.mul(1 - t1).add(b.mul(t1));
            lineRasterizer.rasterize(a, ab, shader);
            return;
        }

        lineRasterizer.rasterize(a, b, shader);
    }

    public void triangleShaderRasterizer(Vertex a, Vertex b, Vertex c, Shader shader) {
        a = a.dehomog();
        b = b.dehomog();
        c = c.dehomog();

        a.setVec3D(transformToWindow(a));
        b.setVec3D(transformToWindow(b));
        c.setVec3D(transformToWindow(c));

        if (a.getVec3D().getY() > b.getVec3D().getY()) {
            Vertex tmp = a;
            a = b;
            b = tmp;
        }

        if (a.getVec3D().getY() > c.getVec3D().getY()) {
            Vertex tmp = a;
            a = c;
            c = tmp;
        }

        if (b.getVec3D().getY() > c.getVec3D().getY()) {
            Vertex tmp = b;
            b = c;
            c = tmp;
        }

        int aY = (int) Math.round(a.getVec3D().getY());
        int bY = (int) Math.round(b.getVec3D().getY());
        int cY = (int) Math.round(c.getVec3D().getY());

        for (int y = aY; y <= bY; y++) {
            double tAB = (y - aY) / (double) (bY - aY);
            double tAC = (y - aY) / (double) (cY - aY);

            Vertex v1 = lerp.lerp(a, b, tAB);
            Vertex v2 = lerp.lerp(a, c, tAC);

            int x1 = (int) Math.round(transformToWindow(v1).getX());
            int x2 = (int) Math.round(transformToWindow(v2).getX());

            if (x2 < x1) {
                int temp = x1;
                x1 = x2;
                x2 = temp;

                Vertex vertexTemp = v2;
                v2 = v1;
                v1 = vertexTemp;
            }
            for (int x = x1; x <= x2; x++) {
                double t = (x - x1) / (double) (x2 - x1);
                Vertex v = lerp.lerp(v1, v2, t);

                zBuffer.setPixelWithZTest(x, y, v.getPosition().getZ(), shader.getColor(v));
            }
        }
        for (int y = bY; y <= cY; y++) {
            double tBC = (y - bY) / (double) (cY - bY);
            double tAC = (y - aY) / (double) (cY - aY);

            Vertex v1 = lerp.lerp(b,c,tBC);
            Vertex v2 = lerp.lerp(a,c,tAC);

            int x1 = (int) Math.round(transformToWindow(v1).getX());
            int x2 = (int) Math.round(transformToWindow(v2).getX());

            if (x2 < x1) {
                int temp = x1;
                x1 = x2;
                x2 = temp;

                Vertex vertexTemp = v2;
                v2 = v1;
                v1 = vertexTemp;
            }
            for (int x = x1; x <= x2; x++) {
                double t = (x - x1) / (double) (x2 - x1);
                Vertex v = lerp.lerp(v1, v2, t);

                zBuffer.setPixelWithZTest(x, y, v.getPosition().getZ(), shader.getColor(v));
            }
        }
    }
}