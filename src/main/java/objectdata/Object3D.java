package objectdata;

import transforms.*;

import java.util.ArrayList;
import java.util.List;


public class Object3D {

    final ArrayList<Point3D> vertexBuffer = new ArrayList<>();
    final ArrayList<Integer> indexBuffer = new ArrayList<>();
    private final Mat4 modelMat = new Mat4();
    private boolean transferable = true;
    private Mat4 transMat = new Mat4Identity();

    private int color = 0xFF0000;

    public List<Point3D> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public Mat4 getModelMat() {
        return modelMat;
    }

    public boolean isTransferable() {
        return transferable;
    }

    public Mat4 getTransMat() {
        return transMat;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}