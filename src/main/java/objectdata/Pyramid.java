// package objectdata;

// import transforms.Col;
// import transforms.Point3D;

// public class Pyramid extends Object3D {
//     public Pyramid() {
//         indexBuffer.add(1);
//         indexBuffer.add(2);
//         indexBuffer.add(2);
//         indexBuffer.add(3);
//         indexBuffer.add(3);
//         indexBuffer.add(4);
//         indexBuffer.add(1);
//         indexBuffer.add(4);
//         indexBuffer.add(0);
//         indexBuffer.add(1);
//         indexBuffer.add(0);
//         indexBuffer.add(2);
//         indexBuffer.add(0);
//         indexBuffer.add(3);
//         indexBuffer.add(0);
//         indexBuffer.add(4);

//         vertexBuffer.add(new Point3D(0, 0, 5));     //0
//         vertexBuffer.add(new Point3D(-2, -2, 0));   //1
//         vertexBuffer.add(new Point3D(-2, 2, 0));    //2
//         vertexBuffer.add(new Point3D(2, 2, 0));     //3
//         vertexBuffer.add(new Point3D(2, -2, 0));    //4


//         colors.add(new Col(0x00FFFF));
//     }
// }

package objectdata;

import transforms.Col;
import transforms.Point3D;

public class Pyramid extends Object3D {
    public Pyramid() {
        this.fillable = true;

        // Define the indices for the pyramid faces
        indexBuffer.add(0); indexBuffer.add(1); indexBuffer.add(2); // Base triangle 1
        indexBuffer.add(0); indexBuffer.add(2); indexBuffer.add(3); // Base triangle 2
        indexBuffer.add(0); indexBuffer.add(3); indexBuffer.add(4); // Base triangle 3
        indexBuffer.add(0); indexBuffer.add(4); indexBuffer.add(1); // Base triangle 4
        indexBuffer.add(1); indexBuffer.add(2); indexBuffer.add(3); // Side triangle 1
        indexBuffer.add(1); indexBuffer.add(3); indexBuffer.add(4); // Side triangle 2

        // Define the vertices for the pyramid
        vertexBuffer.add(new Point3D(0, 0, 5));     // 0 - Apex
        vertexBuffer.add(new Point3D(-2, -2, 0));   // 1 - Base corner 1
        vertexBuffer.add(new Point3D(-2, 2, 0));    // 2 - Base corner 2
        vertexBuffer.add(new Point3D(2, 2, 0));     // 3 - Base corner 3
        vertexBuffer.add(new Point3D(2, -2, 0));    // 4 - Base corner 4

        // Define the color for the pyramid
        colors.add(new Col(0x00FFFF));

        // Adding parts into partBuffer
        partBuffer.add(new Part(0, 3, TopologyType.TRIANGLE_LIST)); // Base triangle 1
        partBuffer.add(new Part(3, 3, TopologyType.TRIANGLE_LIST)); // Base triangle 2
        partBuffer.add(new Part(6, 3, TopologyType.TRIANGLE_LIST)); // Base triangle 3
        partBuffer.add(new Part(9, 3, TopologyType.TRIANGLE_LIST)); // Base triangle 4
        partBuffer.add(new Part(12, 3, TopologyType.TRIANGLE_LIST)); // Side triangle 1
        partBuffer.add(new Part(15, 3, TopologyType.TRIANGLE_LIST)); // Side triangle 2
    }
}