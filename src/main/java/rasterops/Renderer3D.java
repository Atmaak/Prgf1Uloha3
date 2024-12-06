package rasterops;

import objectdata.Object3D;
import objectdata.Scene;
import raster.Raster;
import rasterize.Liner;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec2D;

import java.util.List;


public class Renderer3D {
    public void renderScene(Raster raster, Scene scene, Mat4 matView, Mat4 matProjection, Liner liner, int color){
        for (Object3D object : scene.getObjects()) {
            final Mat4 objectTransformation = new Mat4();
            renderObject(raster, object, new Mat4(), liner, color);
        }
    }

    private void renderObject(Raster raster, Object3D object, Mat4 transformation, Liner liner, int color){
        final List<Point3D> transformedVertices = object.getVertexBuffer().stream().map(p -> p.mul(transformation)).toList();

        for (int i = 0; i < object.getIndexBuffer().size(); i += 2) {
            final Point3D first = transformedVertices.get(i);
            final Point3D second = transformedVertices.get(i + 1);

            if( first.getX() < -first.getW() || first.getX() > first.getW() || second.getX() < -second.getW() || second.getX() > second.getW() ||
                first.getY() < -first.getW() || first.getY() > first.getW() || second.getY() < -second.getW() || second.getY() > second.getW() ||
                first.getZ() < 0 || first.getZ() > first.getW()
            ){
                continue;
            }

            if( second.getX() < -second.getW() || second.getX() > second.getW() || second.getX() < -second.getW() || second.getX() > second.getW() ||
                    second.getY() < -second.getW() || second.getY() > second.getW() || second.getY() < -second.getW() || second.getY() > second.getW() ||
                    second.getZ() < 0 || second.getZ() > second.getW()
            ){
                continue;
            }

            first.dehomog().ifPresent(p1 -> {
                second.dehomog().ifPresent(p2 -> {

                    Vec2D first2D = p1.ignoreZ();
                    Vec2D second2D = p2.ignoreZ();

                    Vec2D firstInViewSpace = toViewSpace(raster, first2D);
                    Vec2D secondInViewSpace = toViewSpace(raster, second2D);

                    liner.draw(raster, firstInViewSpace, secondInViewSpace, color);
                });
            });
        }

    }

    private Vec2D toViewSpace(Raster raster, Vec2D point){
        return new Vec2D(point.getX() + raster.getWidth() / 2, -point.getY() + raster.getHeight() / 2);
    }


}

