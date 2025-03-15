// package objectdata;
// import transforms.Col;
// import transforms.Point3D;
// public class Cube extends Object3D {
//     public Cube(Point3D p1, Point3D p2, Point3D p3, Point3D p4, Point3D p5, Point3D p6, Point3D p7, Point3D p8) {
//         this.fillable = true;

//         vertexBuffer.add(p1);
//         vertexBuffer.add(p2);
//         vertexBuffer.add(p3);
//         vertexBuffer.add(p4);
//         vertexBuffer.add(p5);
//         vertexBuffer.add(p6);
//         vertexBuffer.add(p7);
//         vertexBuffer.add(p8);

//         indexBuffer.add(0);
//         indexBuffer.add(1);
//         indexBuffer.add(1);
//         indexBuffer.add(2);
//         indexBuffer.add(2);
//         indexBuffer.add(3);
//         indexBuffer.add(3);
//         indexBuffer.add(0);
//         indexBuffer.add(4);
//         indexBuffer.add(5);
//         indexBuffer.add(5);
//         indexBuffer.add(6);
//         indexBuffer.add(6);
//         indexBuffer.add(7);
//         indexBuffer.add(7);
//         indexBuffer.add(4);
//         indexBuffer.add(0);
//         indexBuffer.add(4);
//         indexBuffer.add(1);
//         indexBuffer.add(5);
//         indexBuffer.add(2);
//         indexBuffer.add(6);
//         indexBuffer.add(3);
//         indexBuffer.add(7);

//         colors.add(new Col(0x00FF00));
//     }

//     public Cube() {
//         this.fillable = true;

//         vertexBuffer.add(new Point3D(1, 0, 0));
//         vertexBuffer.add(new Point3D(1, 1, 0));
//         vertexBuffer.add(new Point3D(0, 1, 0));
//         vertexBuffer.add(new Point3D(0, 0, 0));
//         vertexBuffer.add(new Point3D(1, 0, 1));
//         vertexBuffer.add(new Point3D(1, 1, 1));
//         vertexBuffer.add(new Point3D(0, 1, 1));
//         vertexBuffer.add(new Point3D(0, 0, 1));

//         indexBuffer.add(0);
//         indexBuffer.add(1);
//         indexBuffer.add(1);
//         indexBuffer.add(2);
//         indexBuffer.add(2);
//         indexBuffer.add(3);
//         indexBuffer.add(3);
//         indexBuffer.add(0);
//         indexBuffer.add(4);
//         indexBuffer.add(5);
//         indexBuffer.add(5);
//         indexBuffer.add(6);
//         indexBuffer.add(6);
//         indexBuffer.add(7);
//         indexBuffer.add(7);
//         indexBuffer.add(4);
//         indexBuffer.add(0);
//         indexBuffer.add(4);
//         indexBuffer.add(1);
//         indexBuffer.add(5);
//         indexBuffer.add(2);
//         indexBuffer.add(6);
//         indexBuffer.add(3);
//         indexBuffer.add(7);

//         colors.add(new Col(0x00FF00));
//     }
// }

package objectdata;
import transforms.Col;
import transforms.Point3D;

public class Cube extends Object3D {
    public Cube(Point3D p1, Point3D p2, Point3D p3, Point3D p4, Point3D p5, Point3D p6, Point3D p7, Point3D p8, boolean fillable) {
        this.fillable = fillable;

        vertexBuffer.add(p1);
        vertexBuffer.add(p2);
        vertexBuffer.add(p3);
        vertexBuffer.add(p4);
        vertexBuffer.add(p5);
        vertexBuffer.add(p6);
        vertexBuffer.add(p7);
        vertexBuffer.add(p8);

        if (this.fillable) {
            // Front face
            indexBuffer.add(0);
            indexBuffer.add(1);
            indexBuffer.add(2);
            indexBuffer.add(0);
            indexBuffer.add(2);
            indexBuffer.add(3);

            // Back face
            indexBuffer.add(4);
            indexBuffer.add(5);
            indexBuffer.add(6);
            indexBuffer.add(4);
            indexBuffer.add(6);
            indexBuffer.add(7);

            // Top face
            indexBuffer.add(1);
            indexBuffer.add(5);
            indexBuffer.add(6);
            indexBuffer.add(1);
            indexBuffer.add(6);
            indexBuffer.add(2);

            // Bottom face
            indexBuffer.add(0);
            indexBuffer.add(4);
            indexBuffer.add(7);
            indexBuffer.add(0);
            indexBuffer.add(7);
            indexBuffer.add(3);

            // Right face
            indexBuffer.add(0);
            indexBuffer.add(1);
            indexBuffer.add(5);
            indexBuffer.add(0);
            indexBuffer.add(5);
            indexBuffer.add(4);

            // Left face
            indexBuffer.add(3);
            indexBuffer.add(2);
            indexBuffer.add(6);
            indexBuffer.add(3);
            indexBuffer.add(6);
            indexBuffer.add(7);

            // Add parts for each face
            partBuffer.add(new Part(0, 6, TopologyType.TRIANGLE_LIST)); // Front face
            partBuffer.add(new Part(6, 6, TopologyType.TRIANGLE_LIST)); // Back face
            partBuffer.add(new Part(12, 6, TopologyType.TRIANGLE_LIST)); // Top face
            partBuffer.add(new Part(18, 6, TopologyType.TRIANGLE_LIST)); // Bottom face
            partBuffer.add(new Part(24, 6, TopologyType.TRIANGLE_LIST)); // Right face
            partBuffer.add(new Part(30, 6, TopologyType.TRIANGLE_LIST)); // Left face
        } else {
            // Front face
            indexBuffer.add(0);
            indexBuffer.add(1);
            indexBuffer.add(1);
            indexBuffer.add(2);
            indexBuffer.add(2);
            indexBuffer.add(3);
            indexBuffer.add(3);
            indexBuffer.add(0);

            // Back face
            indexBuffer.add(4);
            indexBuffer.add(5);
            indexBuffer.add(5);
            indexBuffer.add(6);
            indexBuffer.add(6);
            indexBuffer.add(7);
            indexBuffer.add(7);
            indexBuffer.add(4);

            // Connecting edges
            indexBuffer.add(0);
            indexBuffer.add(4);
            indexBuffer.add(1);
            indexBuffer.add(5);
            indexBuffer.add(2);
            indexBuffer.add(6);
            indexBuffer.add(3);
            indexBuffer.add(7);

            // Add parts for wireframe
            partBuffer.add(new Part(0, 8, TopologyType.LINE_LIST)); // Front face
            partBuffer.add(new Part(8, 8, TopologyType.LINE_LIST)); // Back face
            partBuffer.add(new Part(16, 8, TopologyType.LINE_LIST)); // Connecting edges
        }

        colors.add(new Col(0x00FF00));
    }

    public Cube(boolean fillable) {
        this.fillable = fillable;

        vertexBuffer.add(new Point3D(1, 0, 0));
        vertexBuffer.add(new Point3D(1, 1, 0));
        vertexBuffer.add(new Point3D(0, 1, 0));
        vertexBuffer.add(new Point3D(0, 0, 0));
        vertexBuffer.add(new Point3D(1, 0, 1));
        vertexBuffer.add(new Point3D(1, 1, 1));
        vertexBuffer.add(new Point3D(0, 1, 1));
        vertexBuffer.add(new Point3D(0, 0, 1));

        if (this.fillable) {
            // Front face
            indexBuffer.add(0);
            indexBuffer.add(1);
            indexBuffer.add(2);
            indexBuffer.add(0);
            indexBuffer.add(2);
            indexBuffer.add(3);

            // Back face
            indexBuffer.add(4);
            indexBuffer.add(5);
            indexBuffer.add(6);
            indexBuffer.add(4);
            indexBuffer.add(6);
            indexBuffer.add(7);

            // Top face
            indexBuffer.add(1);
            indexBuffer.add(5);
            indexBuffer.add(6);
            indexBuffer.add(1);
            indexBuffer.add(6);
            indexBuffer.add(2);

            // Bottom face
            indexBuffer.add(0);
            indexBuffer.add(4);
            indexBuffer.add(7);
            indexBuffer.add(0);
            indexBuffer.add(7);
            indexBuffer.add(3);

            // Right face
            indexBuffer.add(0);
            indexBuffer.add(1);
            indexBuffer.add(5);
            indexBuffer.add(0);
            indexBuffer.add(5);
            indexBuffer.add(4);

            // Left face
            indexBuffer.add(3);
            indexBuffer.add(2);
            indexBuffer.add(6);
            indexBuffer.add(3);
            indexBuffer.add(6);
            indexBuffer.add(7);

            // Add parts for each face
            partBuffer.add(new Part(0, 6, TopologyType.TRIANGLE_LIST)); // Front face
            partBuffer.add(new Part(6, 6, TopologyType.TRIANGLE_LIST)); // Back face
            partBuffer.add(new Part(12, 6, TopologyType.TRIANGLE_LIST)); // Top face
            partBuffer.add(new Part(18, 6, TopologyType.TRIANGLE_LIST)); // Bottom face
            partBuffer.add(new Part(24, 6, TopologyType.TRIANGLE_LIST)); // Right face
            partBuffer.add(new Part(30, 6, TopologyType.TRIANGLE_LIST)); // Left face
        } else {
            // Front face
            indexBuffer.add(0);
            indexBuffer.add(1);
            indexBuffer.add(1);
            indexBuffer.add(2);
            indexBuffer.add(2);
            indexBuffer.add(3);
            indexBuffer.add(3);
            indexBuffer.add(0);

            // Back face
            indexBuffer.add(4);
            indexBuffer.add(5);
            indexBuffer.add(5);
            indexBuffer.add(6);
            indexBuffer.add(6);
            indexBuffer.add(7);
            indexBuffer.add(7);
            indexBuffer.add(4);

            // Connecting edges
            indexBuffer.add(0);
            indexBuffer.add(4);
            indexBuffer.add(1);
            indexBuffer.add(5);
            indexBuffer.add(2);
            indexBuffer.add(6);
            indexBuffer.add(3);
            indexBuffer.add(7);

            // Add parts for wireframe
            partBuffer.add(new Part(0, 8, TopologyType.LINE_LIST)); // Front face
            partBuffer.add(new Part(8, 8, TopologyType.LINE_LIST)); // Back face
            partBuffer.add(new Part(16, 8, TopologyType.LINE_LIST)); // Connecting edges
        }

        colors.add(new Col(0x00FF00));
    }
}