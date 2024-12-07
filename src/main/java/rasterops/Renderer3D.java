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

    public void renderScene(Raster raster, Scene scene, Mat4 matView, Mat4 matProjection, Mat4 model) {
        for (Object3D object : scene.getObjects()) {
            draw(object, model, raster, matProjection, matView);
        }
    }

    private Boolean cutable(Point3D point){
        return -point.getW() <= point.getY() && -point.getW() <= point.getX() && point.getY() <= point.getW() && point.getX() <= point.getW() && point.getZ() >= 0 && point.getZ() <= point.getW();
    }

    public void draw(Object3D object, Mat4 model, Raster raster, Mat4 projection, Mat4 view) {
        BufferedImage img  = raster.getImg();

        if (object.isTransferable()) {
            this.model = object.getTransMat().mul(model);
        } else {
            this.model = new Mat4Identity();
        }

        final Mat4 finalTransform = this.model.mul(view).mul(projection);
        List<Integer> indexs = object.getIndexBuffer();

        for (int i = 0; i < indexs.size(); i += 2) {
            int ibA = object.getIndexBuffer().get(i);
            int ibB = object.getIndexBuffer().get(i + 1);

            Point3D vbA = object.getVertexBuffer().get(ibA);
            Point3D vbB = object.getVertexBuffer().get(ibB);

            vbA = vbA.mul(finalTransform);
            vbB = vbB.mul(finalTransform);

            Vec3D vectorA = null;
            Vec3D vectorB = null;

            if (cutable(vbA) && cutable(vbB)){
                if (vbA.dehomog().isPresent()) {
                    vectorA = vbA.dehomog().get();
                }

                if (vbA.dehomog().isPresent()) {
                    vectorB = vbB.dehomog().get();
                }

                int x1 = (int) ((1 + vectorA.getX()) * (img.getWidth() - 1) / 2);
                int y1 = (int) ((1 - vectorA.getY()) * (img.getHeight() - 1) / 2);

                int x2 = (int) ((1 + vectorB.getX()) * (img.getWidth() - 1) / 2);
                int y2 = (int) ((1 - vectorB.getY()) * (img.getHeight() - 1) / 2);

                Graphics g = img.getGraphics();
                g.setColor(new Color(object.getColor(i / 2)));
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }

}

