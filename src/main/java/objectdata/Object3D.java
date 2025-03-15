package objectdata;

import shader.Shader;
import structure.Part;
import structure.Vertex;
import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;


public class Object3D {
    final ArrayList<Vertex> vertexBuffer = new ArrayList<>();
    final ArrayList<Integer> indexBuffer = new ArrayList<>();
    final ArrayList<Part> partBuffer = new ArrayList<Part>();
    boolean fillable = false;
    boolean filled = false;
    Shader shader;

    final Mat4 modelMat = new Mat4();
    boolean transferable = true;
    Mat4 transMat = new Mat4Identity();

    ArrayList<Col> colors = new ArrayList<>();

    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public ArrayList<Part> getPartBuffer() {
        return partBuffer;
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

    public Col getColor(int index) {
        if (colors.size() > index) {
            return colors.get(index);
        }
        return colors.getLast();
    }

    public boolean isFillable() {
        return fillable;
    }

    public void setFillable(boolean fillable) {
        this.fillable = fillable;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public Shader getShader() {
        return shader;
    }
}