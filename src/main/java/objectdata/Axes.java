package objectdata;

import structure.Vertex;
import transforms.Col;
import transforms.Point3D;

public class Axes extends Object3D {
    public Axes() {
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(0);
        indexBuffer.add(3);

        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(0xff0000))); //0
        vertexBuffer.add(new Vertex(new Point3D(1, 0, 0), new Col(0xff0000))); //1
        vertexBuffer.add(new Vertex(new Point3D(0, 1, 0), new Col(0xff0000))); //2
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 1), new Col(0xff0000))); //3

        colors.add(new Col(0xff0000));
        colors.add(new Col(0x00ff00));
        colors.add(new Col(0x0000ff));
    }
}