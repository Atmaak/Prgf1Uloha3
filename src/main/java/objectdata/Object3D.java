package objectdata;

import transforms.*;

import java.util.ArrayList;
import java.util.List;


public class Object3D {
    final ArrayList<Point3D> vertexBuffer = new ArrayList<>();
    final ArrayList<Integer> indexBuffer = new ArrayList<>();
    final Mat4 modelMat = new Mat4();
    boolean transferable = true;
    Mat4 transMat = new Mat4Identity();

    public Object3D(Mat4 model, List<Integer> vertexBuffer, Mat4 transformation ) {

    }

    ArrayList<Integer> colors = new ArrayList<>();

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

    public int getColor(int index) {
        if (colors.size() > index) {
            return colors.get(index);
        }
        return colors.getLast();
    }
}