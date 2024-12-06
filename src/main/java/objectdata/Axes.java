package objectdata;

import transforms.Point3D;

public class Axes extends Object3D {
    public Axes() {
        transferable = false;

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(0);
        indexBuffer.add(3);


        vertexBuffer.add(new Point3D(0, 0, 0)); //0
        vertexBuffer.add(new Point3D(1, 0, 0)); //1
        vertexBuffer.add(new Point3D(0, 1, 0)); //2
        vertexBuffer.add(new Point3D(0, 0, 1)); //3

        colors.add(0xff0000);
        colors.add(0x00ff00);
        colors.add(0x0000ff);
    }
}