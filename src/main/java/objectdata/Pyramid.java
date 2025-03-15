package objectdata;

import transforms.Col;
import transforms.Point3D;

public class Pyramid extends Object3D {
    public Pyramid() {
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(3);
        indexBuffer.add(4);
        indexBuffer.add(1);
        indexBuffer.add(4);
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(0);
        indexBuffer.add(3);
        indexBuffer.add(0);
        indexBuffer.add(4);

        vertexBuffer.add(new Point3D(0, 0, 5));     //0
        vertexBuffer.add(new Point3D(-2, -2, 0));   //1
        vertexBuffer.add(new Point3D(-2, 2, 0));    //2
        vertexBuffer.add(new Point3D(2, 2, 0));     //3
        vertexBuffer.add(new Point3D(2, -2, 0));    //4


        colors.add(new Col(0x00FFFF));
    }
}