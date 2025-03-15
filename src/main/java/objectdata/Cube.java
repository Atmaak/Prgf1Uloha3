package objectdata;
import transforms.Point3D;
public class Cube extends Object3D {
    public Cube(Point3D p1, Point3D p2, Point3D p3, Point3D p4, Point3D p5, Point3D p6, Point3D p7, Point3D p8) {
        vertexBuffer.add(p1);
        vertexBuffer.add(p2);
        vertexBuffer.add(p3);
        vertexBuffer.add(p4);
        vertexBuffer.add(p5);
        vertexBuffer.add(p6);
        vertexBuffer.add(p7);
        vertexBuffer.add(p8);

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(3);
        indexBuffer.add(0);
        indexBuffer.add(4);
        indexBuffer.add(5);
        indexBuffer.add(5);
        indexBuffer.add(6);
        indexBuffer.add(6);
        indexBuffer.add(7);
        indexBuffer.add(7);
        indexBuffer.add(4);
        indexBuffer.add(0);
        indexBuffer.add(4);
        indexBuffer.add(1);
        indexBuffer.add(5);
        indexBuffer.add(2);
        indexBuffer.add(6);
        indexBuffer.add(3);
        indexBuffer.add(7);

        colors.add(0x00FF00);
    }

    public Cube() {
        vertexBuffer.add(new Point3D(1, 0, 0));
        vertexBuffer.add(new Point3D(1, 1, 0));
        vertexBuffer.add(new Point3D(0, 1, 0));
        vertexBuffer.add(new Point3D(0, 0, 0));
        vertexBuffer.add(new Point3D(1, 0, 1));
        vertexBuffer.add(new Point3D(1, 1, 1));
        vertexBuffer.add(new Point3D(0, 1, 1));
        vertexBuffer.add(new Point3D(0, 0, 1));

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(3);
        indexBuffer.add(0);
        indexBuffer.add(4);
        indexBuffer.add(5);
        indexBuffer.add(5);
        indexBuffer.add(6);
        indexBuffer.add(6);
        indexBuffer.add(7);
        indexBuffer.add(7);
        indexBuffer.add(4);
        indexBuffer.add(0);
        indexBuffer.add(4);
        indexBuffer.add(1);
        indexBuffer.add(5);
        indexBuffer.add(2);
        indexBuffer.add(6);
        indexBuffer.add(3);
        indexBuffer.add(7);

        colors.add(0x00FF00);
    }
}