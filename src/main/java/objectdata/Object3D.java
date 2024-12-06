package objectdata;

import transforms.*;

import java.util.List;


//represents a general object in 3D space
public class Object3D {

    private final List<Point3D> vertexBuffer;
    private final List<Integer> indexBuffer;
    private final Mat4 modelMat;

    public Object3D(List<Point3D> vertexBuffer, List<Integer> indexBuffer, Mat4 modelMat) {
        this.vertexBuffer = vertexBuffer;
        this.indexBuffer = indexBuffer;
        this.modelMat = modelMat;
    }

    public List<Point3D> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public Mat4 getModelMat() {
        return modelMat;
    }
}