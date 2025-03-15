package rasterizer;

import raster.Raster;
import raster.ZBuffer;
import transforms.Vec3D;
import structure.Vertex;
import utils.Lerp;

public abstract class Rasterizer {
    ZBuffer zBuffer;
    protected final Lerp<Vertex> lerp = new Lerp<>();
    Raster raster;
    public Rasterizer(ZBuffer zBuffer, Raster raster) {
        this.zBuffer = zBuffer;
        this.raster = raster;
    }

    protected Vec3D transformToWindow(Vertex v) {
        return v.getPosition()
            .ignoreW()
            .mul(new Vec3D(1, -1, 1))
            .add(new Vec3D(1, 1, 0))
            .mul(new Vec3D((raster.getWidth() - 1) / 2d, (raster.getHeight() - 1) / 2d, 1d)
        );
    }
}
