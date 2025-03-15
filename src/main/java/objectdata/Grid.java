package objectdata;

import transforms.Col;
import transforms.Point3D;

public class Grid extends Object3D {
    private int n = 5, m = 5;
    public Grid() {
        colors.add(new Col(0xB3F800));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                vertexBuffer.add(new Point3D(i * 1f, j * 1f, 0));
            }
        }

        for (int i = 0; i < n; i++) {
            indexBuffer.add(i);
            indexBuffer.add(i + (n * (m - 1)));
            indexBuffer.add(i * m);
            indexBuffer.add(i * m + n - 1);
        }
    }
}