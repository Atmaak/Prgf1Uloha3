package objectdata;
import transforms.Col;
import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

public class Cube extends Object3D {
    public Cube() {
        this.fillable = true;
        vertexBuffer.add(new Point3D(1, 0, 0));
        vertexBuffer.add(new Point3D(1, 1, 0));
        vertexBuffer.add(new Point3D(0, 1, 0));
        vertexBuffer.add(new Point3D(0, 0, 0));
        vertexBuffer.add(new Point3D(1, 0, 1));
        vertexBuffer.add(new Point3D(1, 1, 1));
        vertexBuffer.add(new Point3D(0, 1, 1));
        vertexBuffer.add(new Point3D(0, 0, 1));

        colors.add(new Col(0xFF00FF));
    }

    @Override
    public List<Integer> getIndexBuffer() {
        indexBuffer.clear();
        if (this.filled) {
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
        }
        return indexBuffer;
    }

    @Override
    public ArrayList<Part> getPartBuffer() {
        partBuffer.clear();
        if (this.filled) {
            // Add parts for each face
            partBuffer.add(new Part(0, 6, TopologyType.TRIANGLE_LIST)); // Front face
            partBuffer.add(new Part(6, 6, TopologyType.TRIANGLE_LIST)); // Back face
            partBuffer.add(new Part(12, 6, TopologyType.TRIANGLE_LIST)); // Top face
            partBuffer.add(new Part(18, 6, TopologyType.TRIANGLE_LIST)); // Bottom face
            partBuffer.add(new Part(24, 6, TopologyType.TRIANGLE_LIST)); // Right face
            partBuffer.add(new Part(30, 6, TopologyType.TRIANGLE_LIST)); // Left face
        } else {
            // Add parts for wireframe
            partBuffer.add(new Part(0, 8, TopologyType.LINE_LIST)); // Front face
            partBuffer.add(new Part(8, 8, TopologyType.LINE_LIST)); // Back face
            partBuffer.add(new Part(16, 8, TopologyType.LINE_LIST)); // Connecting edges
        }

        return partBuffer;
    }
}