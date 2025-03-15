package rasterizer;

import raster.Raster;
import raster.ZBuffer;
import shader.Shader;
import structure.Vertex;

public class PointRasterizer extends Rasterizer {
    public PointRasterizer(ZBuffer zBuffer, Raster raster) {
        super(zBuffer, raster);
    }

    public void rasterize(Vertex a, Shader shader) {
        a = a.dehomog();
        a.setVec3D(transformToWindow(a));

        zBuffer.setPixelWithZTest(
                (int) Math.round(a.getPosition().getX()),
                (int) Math.round(a.getPosition().getY()),
                a.getVec3D().getZ(),
                shader.getColor(a)
        );
    }
}
