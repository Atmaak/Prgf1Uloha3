package objectdata;

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

        vertexBuffer.add(new Point3D(0, 0, 4));     //0
        vertexBuffer.add(new Point3D(-2, -2, -1));   //1
        vertexBuffer.add(new Point3D(-2, 2, -1));    //2
        vertexBuffer.add(new Point3D(2, 2, -1));     //3
        vertexBuffer.add(new Point3D(2, -2, -1));    //4

        colors.add(0x00FFFF);
    }
}