package rasterizer;

import raster.Raster;
import raster.ZBuffer;
import shader.Shader;
import structure.Vertex;

public class LineRasterizer extends Rasterizer {
    public LineRasterizer(ZBuffer zBuffer, Raster raster) {
        super(zBuffer, raster);
    }

    public void rasterize(Vertex a, Vertex b, Shader shader) {
        a = a.dehomog();
        a.setVec3D(transformToWindow(a));
        int x1 = (int) a.getVec3D().getX();
        int y1 = (int) a.getVec3D().getY();

        b = b.dehomog();
        b.setVec3D(transformToWindow(b));
        int x2 = (int) Math.round(b.getVec3D().getX());
        int y2 = (int) Math.round(b.getVec3D().getY());

        double k = (y2 - y1) / (double) (x2 - x1);
        double q = y1 - k * x1;

        if (Math.abs(y2 - y1) < Math.abs(x2 - x1)) {
            if (x2 < x1) {
                int tmp = x1;
                x1 = x2;
                x2 = tmp;

                Vertex temp = a;
                a = b;
                b = temp;
            }
            for (int x = x1; x <= x2; x++) {
                double t = (x - x1) / (double) (x2 - x1);
                Vertex v = lerp.lerp(a, b, t);

                int y = (int) Math.round(k * (double) x + q);

                zBuffer.setPixelWithZTest(x, y, v.getPosition().getZ(), shader.getColor(v));
            }
        }
        if (Math.abs(y2 - y1) >= Math.abs(x2 - x1)) {
            if (y2 < y1) {
                int tmp = y1;
                y1 = y2;
                y2 = tmp;

                Vertex temp = a;
                a = b;
                b = temp;
            }
            for (int y = y1; y <= y2; y++) {
                double t = (y - y1) / (double) (y2 - y1);
                Vertex v = lerp.lerp(a, b, t);

                zBuffer.setPixelWithZTest(x1 == x2 ? x1 : (int) Math.round((y - q) / (double) k), y, v.getPosition().getZ(), shader.getColor(v));
            }
        }
    }
}